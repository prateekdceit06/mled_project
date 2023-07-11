import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class MledSimulator {
    private static MledSimulator instance = null;
    Scanner scanner = new Scanner(System.in);

    private int layerNum = 0;

    private List<Layer> layers = new ArrayList<>();

    private int lastNodeID;

    private ErrorModel errorModel;

    private int lastLayerMTU;
    private int firstLayerMTU;

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


    public int getLastNodeID() {
        return lastNodeID;
    }

    public int getFirstLayerMTU() {
        return firstLayerMTU;
    }

    public void initialise() {
        System.out.println(PrintColor.printInGreenBack("MLED Simulator starting.."));
        boolean errorFlag = false;
        do {
            if (errorFlag) {
                System.out.println(PrintColor.printInRedBack("Error: Invalid input. Please try again."));
            }
            try {
                System.out.print(PrintColor.printInGreen("Enter the number of error checking layers [1-99]: "));
                layerNum = scanner.nextInt();
                scanner.nextLine();
                errorFlag = !Validator.isIntValid(layerNum, 1, 99);
                if (!errorFlag) {
                    lastNodeID = (int) Math.pow(2,  layerNum- 1) + 1;
                } else{
                    continue;
                }
                System.out.print(PrintColor.printInBlue("Enter the number MTU for the last layer [500-10000]: "));
                lastLayerMTU = scanner.nextInt();
                scanner.nextLine();
                errorFlag = !Validator.isIntValid(lastLayerMTU, 500, 10000);
            } catch (Exception e) {
                errorFlag = true;
            }
        } while (errorFlag);


        for (int i = 1; i <= layerNum; i++) {
            int fragmentationParameter = 1;
            do {
                if (errorFlag) {
                    System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
                }
                try {
                    if (i!=1){
                        System.out.print(PrintColor.printInYellow("Enter the fragmentation parameter for layer " + i + " [1-10]: "));
                        fragmentationParameter = scanner.nextInt();
                        scanner.nextLine();
                        errorFlag = !Validator.isIntValid(fragmentationParameter, 1, 10);
                        if (errorFlag) {
                            continue;
                        }
                    }
                    Menu.errorModelMenu();
                    System.out.print(PrintColor.printInPurple("Enter the error model for layer " + i + ": "));
                    int edModel = scanner.nextInt();
                    scanner.nextLine();
                    errorFlag = !Validator.isIntValid(edModel, 1, 3);
                    switch (edModel) {
                        case 1:
                            errorModel = new ErrorModelGilbertElliot();
                            break;
                        case 2:
                            errorModel = new ErrorModelGilbertElliot();
                            errorModel.configure();
                            break;
                        case 3:
                            exit(1);
                        default:
                            System.out.println(PrintColor.printInRedBack("Invalid input"));
                            errorFlag = true;
                    }
                    if (errorFlag) {
                        continue;
                    }
                    Menu.errorCheckMethodMenu();
                    System.out.print(PrintColor.printInCyan("Select the error detection method for layer " + i + ": "));
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
                        layers.add(new Layer(i, fragmentationParameter, errorDetectionMethodName, errorDetectionMethod, errorModel));
                        // Number of nodes in each layer is 2^(i-1) + 1
                        int numNodes = (int) Math.pow(2, i - 1) + 1;
                        layers.get(i - 1).addNodes(layerNum, numNodes, errorDetectionMethod, errorModel);
                        System.out.println(PrintColor.printInGreenBack("Layer " + i + " created with fragmentation parameter " + fragmentationParameter + " and error detection method " + errorDetectionMethodName + '\n'));
                    }

                } catch (Exception e) {
                    errorFlag = true;
                }
            } while (errorFlag);
        }
        calculateFirstLayerMTU();
        createRoute();
        CommonFunctions.printNetwork(layers);
        runNetwork();
    }

    private void createRoute() {

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
        System.out.println(PrintColor.printInGreenBack("MLED Simulator started successfully."));
    }

    private void calculateFirstLayerMTU(){
        int firstLayerMTU = lastLayerMTU;
        for (int i = layerNum; i > 1; i--) {
            firstLayerMTU = firstLayerMTU * layers.get(i - 1).getFragmentationParameter();
        }
        this.firstLayerMTU = firstLayerMTU;
    }

    public void runNetwork() {
        ((NodeA) layers.get(0).getNodes().get(0)).readFileAndSendPackets();
    }




}
