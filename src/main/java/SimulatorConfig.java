import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.Reader;

import static java.lang.System.exit;

public class SimulatorConfig {
    public void readConfig(String configFile, MledSimulator simulator) {

        boolean errorFlag = false;

        try (Reader reader = new FileReader(configFile)) {
            JsonElement rootElement = JsonParser.parseReader(reader);
            JsonObject rootObject = rootElement.getAsJsonObject();

            JsonObject simulatorObject = rootObject.getAsJsonObject("simulator");

            int lastLayerMTU = simulatorObject.get("lastLayerMTU").getAsInt();
            int seed = simulatorObject.get("seed").getAsInt();

            errorFlag = !Validator.isIntValid(lastLayerMTU, 500, 10000);
            if(errorFlag){
               System.out.println(PrintColor.printInRedBack("Error: Invalid last layer MTU. " +
                       "Enter a value between [500-10000]. Please try again."));
               exit(0);
            }

            simulator.setLastLayerMTU(lastLayerMTU);

            JsonArray layersArray = rootObject.getAsJsonArray("layers");
            int numberOfLayers = layersArray.size();
            int lastNodeID = (int) Math.pow(2, numberOfLayers - 1) + 1;
            simulator.setLastNodeID(lastNodeID);

            simulator.setLayerNum(numberOfLayers);

            int layerID = 0;

            for (JsonElement layerElement : layersArray) {
                layerID++;
                JsonObject layerObject = layerElement.getAsJsonObject();
                int fragmentationParameter = layerObject.get("fragmentationParameter").getAsInt();
                errorFlag = !Validator.isIntValid(fragmentationParameter, 1, 10);
                if(errorFlag){
                    System.out.println(PrintColor.printInRedBack("Error: Invalid fragmentation parameter." +
                            "Enter a value between [1-10]. Please try again."));
                    exit(0);
                }

                JsonObject errorDetectionMethodObject = layerObject.getAsJsonObject("errorDetectionMethod");

                String errorDetectionMethodName = errorDetectionMethodObject.get("name").getAsString().toUpperCase();

                ErrorDetectionMethod errorDetectionMethod = null;

                if (errorDetectionMethodName.equals("CHECKSUM") ||
                        errorDetectionMethodName.equals("HASH") ||
                        errorDetectionMethodName.equals("CRC")) {
                    errorDetectionMethod =
                            ErrorDetectionMethodFactory.getErrorDetectionMethod(errorDetectionMethodName);

                } else{
                    System.out.println(PrintColor.printInRedBack("Error: Invalid error detection method. " +
                            "Enter a value from CHECKSUM, HASH or CRC" +
                            "Please try again."));
                    exit(0);
                }



                if (errorDetectionMethodName.equals(Constants.errorDetectionMethodEnum.CHECKSUM.toString())) {
                    if (errorDetectionMethodObject.has("checksumLength")) {
                        int checksumLength = errorDetectionMethodObject.get("checksumLength").getAsInt();
                        if(checksumLength < 1 || checksumLength > 10){
                            System.out.println(PrintColor.printInRedBack("Error: Invalid checksum length. " +
                                    "Enter a value between [1-10]. Please try again."));
                            exit(0);
                        } else{
                            ((ErrorDetectionMethodChecksum) errorDetectionMethod).setChecksumLength(checksumLength);
                        }
                    } else{
                        System.out.println(PrintColor.printInRedBack("Error: You have to add 'checksumLength' in the configuration file for CHECKSUM error detection method. " +
                                "Please try again."));
                        exit(0);
                    }

                } else if (errorDetectionMethodName.equals(Constants.errorDetectionMethodEnum.CRC.toString())) {
                    if (errorDetectionMethodObject.has("crcPolynomial")) {
                        String crcPolynomial = errorDetectionMethodObject.get("crcPolynomial").getAsString();
                        ((ErrorDetectionMethodCRC) errorDetectionMethod).setCrcPolynomialHex(crcPolynomial);
                        String getCrcPolynomialBinary = ((ErrorDetectionMethodCRC) errorDetectionMethod).hexToBin(crcPolynomial);
                        ((ErrorDetectionMethodCRC) errorDetectionMethod).setCrcPolynomialBinary(getCrcPolynomialBinary);
                        if (getCrcPolynomialBinary.charAt(0) != '1' || getCrcPolynomialBinary.charAt(getCrcPolynomialBinary.length() - 1) != '1') {
                            System.out.println(PrintColor.printInRedBack("Invalid generator polynomial. The first and last bits must be 1."));
                            exit(0);
                        }
                    } else{
                        System.out.println(PrintColor.printInRedBack("Error: You have to add 'crcPolynomial' in the configuration file for CRC error detection method. " +
                                "Please try again."));
                        exit(0);
                    }


                } else if (errorDetectionMethodName.equals(Constants.errorDetectionMethodEnum.HASH.toString())) {
                    if (errorDetectionMethodObject.has("algorithm")) {
                        String algorithm = errorDetectionMethodObject.get("algorithm").getAsString().toUpperCase();
                        if (algorithm.equals("MD5") ||
                                algorithm.equals("SHA1") ||
                                algorithm.equals("SHA256")) {
                            ((ErrorDetectionMethodHash) errorDetectionMethod).setAlgorithm(algorithm);
                        } else{
                            System.out.println(PrintColor.printInRedBack("Error: Invalid error detection method. " +
                                    "Enter a value from CHECKSUM, HASH or CRC" +
                                    "Please try again."));
                            exit(0);
                        }
                    } else{
                        System.out.println(PrintColor.printInRedBack("Error: You have to add 'algorithm' in the configuration file for HASH error detection method. " +
                                "Please try again."));
                        exit(0);
                    }

                }

                JsonObject errorModelObject = layerObject.getAsJsonObject("errorModel");
                double goodToBad = errorModelObject.get("goodToBad").getAsDouble();
                errorFlag = !Validator.isDoubleValid(goodToBad, 0, 1);
                if(errorFlag){
                    System.out.println(PrintColor.printInRedBack("Error: Invalid goodToBad probability. " +
                            "Enter a value between [0-1]. Please try again."));
                    exit(0);
                }
                double badToGood = errorModelObject.get("badToGood").getAsDouble();
                errorFlag = !Validator.isDoubleValid(badToGood, 0, 1);
                if(errorFlag){
                    System.out.println(PrintColor.printInRedBack("Error: Invalid badToGood probability. " +
                            "Enter a value between [0-1]. Please try again."));
                    exit(0);
                }
                double errorProbabilityGood = errorModelObject.get("errorProbabilityGood").getAsDouble();
                errorFlag = !Validator.isDoubleValid(errorProbabilityGood, 0, 1);
                if(errorFlag){
                    System.out.println(PrintColor.printInRedBack("Error: Invalid errorProbabilityGood probability. " +
                            "Enter a value between [0-1]. Please try again."));
                    exit(0);
                }
                double errorProbabilityBad = errorModelObject.get("errorProbabilityBad").getAsDouble();
                errorFlag = !Validator.isDoubleValid(errorProbabilityBad, 0, 1);
                if(errorFlag){
                    System.out.println(PrintColor.printInRedBack("Error: Invalid errorProbabilityBad probability. " +
                            "Enter a value between [0-1]. Please try again."));
                    exit(0);
                }
                ErrorModelGilbertElliot errorModel = new ErrorModelGilbertElliot(goodToBad, badToGood,
                        errorProbabilityGood, errorProbabilityBad);
                errorModel.setSeed(seed);

                Layer layer = new Layer(layerID, fragmentationParameter, errorDetectionMethodName,
                        errorDetectionMethod, errorModel);

                simulator.getLayers().add(layer);

                int numNodes = (int) Math.pow(2, layerID - 1) + 1;
                simulator.getLayers().get(layerID - 1).addNodes(numberOfLayers, numNodes, errorDetectionMethod, errorModel);
                System.out.println(PrintColor.printInGreenBack("Layer " + layerID +
                        " created with fragmentation parameter " + fragmentationParameter +
                        " and error detection method " + errorDetectionMethodName));
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Failed to load configuration: " + e.getMessage());
        }
    }
}
