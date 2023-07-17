import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.Reader;

//todo: create read me.. describe configuration file.. add validation checks for configuration... describe config file.
public class SimulatorConfig {
    public void readConfig(String configFile, MledSimulator simulator) {
        try (Reader reader = new FileReader(configFile)) {
            JsonElement rootElement = JsonParser.parseReader(reader);
            JsonObject rootObject = rootElement.getAsJsonObject();

            JsonObject simulatorObject = rootObject.getAsJsonObject("simulator");
            int numberOfLayers = simulatorObject.get("numberOfLayers").getAsInt();
            int lastLayerMTU = simulatorObject.get("lastLayerMTU").getAsInt();
            int seed = simulatorObject.get("seed").getAsInt();

            int lastNodeID = (int) Math.pow(2, numberOfLayers - 1) + 1;
            simulator.setLastNodeID(lastNodeID);

            simulator.setLayerNum(numberOfLayers);
            simulator.setLastLayerMTU(lastLayerMTU);

            for (JsonElement layerElement : rootObject.getAsJsonArray("layers")) {
                JsonObject layerObject = layerElement.getAsJsonObject();
                int id = layerObject.get("id").getAsInt();
                int fragmentationParameter = layerObject.get("fragmentationParameter").getAsInt();

                JsonObject errorDetectionMethodObject = layerObject.getAsJsonObject("errorDetectionMethod");
                String errorDetectionMethodName = errorDetectionMethodObject.get("name").getAsString();

                ErrorDetectionMethod errorDetectionMethod =
                        ErrorDetectionMethodFactory.getErrorDetectionMethod(errorDetectionMethodName);

                if (errorDetectionMethodName.equals(Constants.errorDetectionMethodEnum.CHECKSUM.toString())) {
                    int checksumLength = errorDetectionMethodObject.get("checksumLength").getAsInt();
                    ((ErrorDetectionMethodChecksum) errorDetectionMethod).setChecksumLength(checksumLength);
                } else if (errorDetectionMethodName.equals(Constants.errorDetectionMethodEnum.CRC.toString())) {
                    String crcPolynomial = errorDetectionMethodObject.get("crcPolynomial").getAsString();
                    ((ErrorDetectionMethodCRC) errorDetectionMethod).setCrcPolynomialHex(crcPolynomial);
                    String getCrcPolynomialBinary = ((ErrorDetectionMethodCRC) errorDetectionMethod).hexToBin(crcPolynomial);
                    ((ErrorDetectionMethodCRC) errorDetectionMethod).setCrcPolynomialBinary(getCrcPolynomialBinary);
                    if (getCrcPolynomialBinary.charAt(0) != '1' || getCrcPolynomialBinary.charAt(getCrcPolynomialBinary.length() - 1) != '1') {
                        System.out.println(PrintColor.printInRedBack("Invalid generator polynomial. The first and last bits must be 1."));
                        break;
                    }
                } else if (errorDetectionMethodName.equals(Constants.errorDetectionMethodEnum.HASH.toString())) {
                    String algorithm = errorDetectionMethodObject.get("algorithm").getAsString();
                    ((ErrorDetectionMethodHash) errorDetectionMethod).setAlgorithm(algorithm);
                }

                JsonObject errorModelObject = layerObject.getAsJsonObject("errorModel");
                Double goodToBad = errorModelObject.get("goodToBad").getAsDouble();
                Double badToGood = errorModelObject.get("badToGood").getAsDouble();
                Double errorProbabilityGood = errorModelObject.get("errorProbabilityGood").getAsDouble();
                Double errorProbabilityBad = errorModelObject.get("errorProbabilityBad").getAsDouble();
                ErrorModelGilbertElliot errorModel = new ErrorModelGilbertElliot(goodToBad, badToGood,
                        errorProbabilityGood, errorProbabilityBad);
                errorModel.setSeed(seed);

                Layer layer = new Layer(id, fragmentationParameter, errorDetectionMethodName,
                        errorDetectionMethod, errorModel);

                simulator.getLayers().add(layer);

                int numNodes = (int) Math.pow(2, id - 1) + 1;
                simulator.getLayers().get(id - 1).addNodes(numberOfLayers, numNodes, errorDetectionMethod, errorModel);
                System.out.println(PrintColor.printInGreenBack("Layer " + id +
                        " created with fragmentation parameter " + fragmentationParameter +
                        " and error detection method " + errorDetectionMethodName));
                System.out.println();


            }
        } catch (Exception e) {
            System.out.println("Failed to load configuration: " + e.getMessage());
        }
    }
}
