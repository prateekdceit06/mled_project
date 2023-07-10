import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class MledSimulator {
    private static MledSimulator instance = null;
    Scanner scanner = new Scanner(System.in);

    private int layerNum = 0;

    private List<Layer> layers = new ArrayList<>();

    private int MSS = Integer.MAX_VALUE;

    private int lastNodeID;

    private MledSimulator() {
    }

    public static MledSimulator getInstance() {
        if (instance == null) {
            instance = new MledSimulator();
        }
        return instance;
    }

    public int getLayerNum() {
        return layerNum;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public int getMSS() {
        return MSS;
    }

    public int getLastNodeID() {
        return lastNodeID;
    }

    public void initialise() {
        System.out.println(PrintColor.printInBlue("MLED Simulator starting.."));
        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
            }
            try {
                System.out.print(PrintColor.printInRed("Enter the number of error checking layers [1-99]: "));
                layerNum = scanner.nextInt();
                scanner.nextLine();
                errorFlag = !Validator.isIntValid(layerNum, 1, 99);
                if (!errorFlag) {
                    lastNodeID = (int) Math.pow(2,  layerNum- 1) + 1;
                }
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);


        for (int i = 1; i <= layerNum; i++) {
            do {
                if (errorFlag) {
                    System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
                }
                try {

                    System.out.print(PrintColor.printInRed("Enter the MTU (in Bytes) for layer " + i + " [500-5000]: "));
                    int MTU = scanner.nextInt();
                    scanner.nextLine();
                    errorFlag = !Validator.isIntValid(MTU, 500, 5000);
                    if (errorFlag) {
                        continue;
                    }
                    Menu.errorCheckMethodMenu();
                    System.out.print(PrintColor.printInRed("Select the error detection method for layer " + i + ": "));
                    int edm = scanner.nextInt();
                    scanner.nextLine();
                    errorFlag = !Validator.isIntValid(edm, 1, 4);
                    if (!errorFlag) {
                        String errorDetectionMethodName = "";
                        switch (edm) {
                            case 1:
                                errorDetectionMethodName = Constants.errorDetectionMethodEnum.CHECKSUM.toString();
                                break;
                            case 2:
                                errorDetectionMethodName = Constants.errorDetectionMethodEnum.CRC.toString();
                                break;
                            case 3:
                                errorDetectionMethodName = Constants.errorDetectionMethodEnum.HASH.toString();
                                break;
                            case 4:
                                exit(1);
                            default:
                                System.out.println(PrintColor.printInRed("Invalid input"));
                                errorFlag = true;
                        }
                        ErrorDetectionMethod errorDetectionMethod = ErrorDetectionMethodFactory.getErrorDetectionMethod(errorDetectionMethodName);
                        errorDetectionMethod.configure();
                        layers.add(new Layer(i, MTU, errorDetectionMethodName, errorDetectionMethod));
                        // Number of nodes in each layer is 2^(i-1) + 1
                        int numNodes = (int) Math.pow(2, i - 1) + 1;
                        layers.get(i - 1).addNodes(layerNum, numNodes, errorDetectionMethod);
                        System.out.println(PrintColor.printInGreen("Layer " + i + " created with MTU " + MTU + " and error detection method " + errorDetectionMethodName + '\n'));
                    }

                } catch (Exception e) {
                    errorFlag = true;
                }
            } while (errorFlag);
        }
        createRoute();
        calculateSmallestMTU();
        CommonFunctions.printNetwork(layers);
        runNetwork();
    }

    public void createRoute() {

        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            List<Node> nodes = layer.getNodes();

            for (int j = 0; j < nodes.size(); j++) {
                Node node = nodes.get(j);
                if (node.getLayerID() != 1) {
                    int parentLayerID = node.getLayerID() - 1;
                    List<Integer> nodeIDs = layers.get(parentLayerID - 1).getNodeIDs();
                    int parentNodeID = node.getNodeID();
                    if (nodeIDs.contains(parentNodeID)) {
                        for (Node parentNode : layers.get(parentLayerID - 1).getNodes()) {
                            if (parentNode.getNodeID() == parentNodeID) {
                                node.setParentNode(parentNode);
                                parentNode.setChildNode(node);
                                break;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(PrintColor.printInGreen("MLED Simulator started successfully."));
    }

    public void runNetwork() {
        ((NodeA) layers.get(0).getNodes().get(0)).readFileAndSendPackets();
    }


    public void calculateSmallestMTU() {
        for (Layer layer : layers) {
            if (layer.getMTU() < MSS) {
                MSS = layer.getMTU();
            }
        }
    }


}
