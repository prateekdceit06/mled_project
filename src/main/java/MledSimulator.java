import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class MledSimulator {
    private static MledSimulator instance = null;
    Scanner scanner = new Scanner(System.in);

    private int layerNum =0 ;

    private List<Layer> layers = new ArrayList<>();

    private int MSS = Integer.MAX_VALUE;
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

    public void initialise() {
        System.out.println(PrintColor.printInGreen("MLED Simulator starting.."));
        boolean errorFlag = false;

        do{
            if(errorFlag){
                System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
            }
            try{
                System.out.print(PrintColor.printInRed("Enter the number of error checking layers [1-99]: "));
                layerNum = scanner.nextInt();
                scanner.nextLine();
                errorFlag = !Validator.isIntValid(layerNum, 1, 99);
            } catch (Exception e){
                errorFlag = true;
            }
        } while (errorFlag);


        for (int i = 1; i <= layerNum; i++) {
            do{
                if(errorFlag){
                    System.out.println(PrintColor.printInRed("Error: Invalid input. Please try again."));
                }
                try{

                    System.out.print(PrintColor.printInRed("Enter the MTU (in Bytes) for layer " + i + " [500-5000]: "));
                    int MTU = scanner.nextInt();
                    scanner.nextLine();
                    errorFlag = !Validator.isIntValid(MTU, 500, 5000);
                    if (errorFlag){
                        continue;
                    }
                    Menu.errorCheckMethodMenu();
                    System.out.print(PrintColor.printInRed("Select the error detection method for layer " + i + ": "));
                    int edm = scanner.nextInt();
                    scanner.nextLine();
                    errorFlag = !Validator.isIntValid(edm, 1, 5);
                    if(!errorFlag){
                        String errorDetectionMethodName = "";
                        switch(edm) {
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
                                errorDetectionMethodName = Constants.errorDetectionMethodEnum.NONE.toString();
                                break;
                            case 5:
                                exit(1);
                            default:
                                System.out.println(PrintColor.printInRed("Invalid input"));
                                errorFlag = true;
                        }
                        ErrorDetectionMethod errorDetectionMethod = ErrorDetectionMethodFactory.getErrorDetectionMethod(errorDetectionMethodName);
                        errorDetectionMethod.configure();
                        layers.add(new Layer(i, MTU, errorDetectionMethodName, errorDetectionMethod));
                        // Number of nodes in each layer is 2^(i-1) + 1
                        int nodeNum = (int) Math.pow(2, i-1) + 1;
                        layers.get(i-1).addNodes(layerNum, nodeNum, errorDetectionMethod);
                        System.out.println(PrintColor.printInGreen("Layer " + i + " created with MTU " + MTU + " and error detection method " + errorDetectionMethodName + '\n'));
                    }

                } catch (Exception e){
                    errorFlag = true;
                }
            } while (errorFlag);
        }
        CommonFunctions.printNetwork(layers);
        createRoute();
        createConnections();
        calculateSmallestMTU();
        System.out.println(PrintColor.printInGreen("MLED Simulator started successfully."));


    }

    public void createRoute() {

        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            List<Node> nodes = layer.getNodes();

            for (int j = 0; j < nodes.size(); j++) {
                Node node = nodes.get(j);
                if (node.getLayerID() == 1) {
                    continue;
                }
                int parentLayerID = node.getLayerID() - 1;
                List<Integer> nodeIDs = layers.get(parentLayerID-1).getNodeIDs();
                int parentNodeID = node.getNodeID();
                if(nodeIDs.contains(parentNodeID)){
                    for (Node parentNode: layers.get(parentLayerID-1).getNodes()) {
                        if(parentNode.getNodeID() == parentNodeID){
                            node.setParentNode(parentNode);
                            parentNode.setChildNode(node);
                            break;
                        }
                    }
                }
                if (node.getNodeID()==1) {
                    node.createClientSocket(node.getParentNode().getIP(), node.getParentNode().getPort());
                }


            }
        }
    }

    public void createConnections(){
        int lastNodeID = (int) Math.pow(2, layers.size()-1) + 1;
        for (Layer layer : layers){
            List<Node> nodes = layer.getNodes();
            for (int j = 0; j < nodes.size(); j++) {
                Node node = nodes.get(j);

                //for the first node in every layer, parent will send data to child so the parent will be the server
                if (node.getNodeID()==1 && node.getParentNode()!=null) {
                    if (node.createClientSocket(node.getParentNode().getIP(), node.getParentNode().getPort())) {
                        System.out.println(PrintColor.printInGreen("Connection established between server node " + node.getParentNode().getNodeName() + " and client node " + node.getNodeName()));
                    } else {
                        System.out.println(PrintColor.printInRed("Connection failed between server node " + node.getParentNode().getNodeName() + " and client node " + node.getNodeName()));
                    }
                }

                //for the last node in every layer, child will send data to parent  so the child will be the server
                if (node.getNodeID() == lastNodeID && node.getChildNode()!=null) {
                    if(node.createClientSocket(node.getChildNode().getIP(), node.getChildNode().getPort())){
                        System.out.println(PrintColor.printInGreen("Connection established between server node " + node.getChildNode().getNodeName() + " and client node " + node.getNodeName()));
                    } else {
                        System.out.println(PrintColor.printInRed("Connection failed between server node " + node.getChildNode().getNodeName() + " and client node " + node.getNodeName()));
                    }
                }

                //In last layer every node will send data to the next node, so the previous node will be the server
                if (node.getLayerID() == layerNum && node.getNodeID()!=1){
                    if(node.createClientSocket(nodes.get(nodes.indexOf(node)-1).getIP(), nodes.get(nodes.indexOf(node)-1).getPort())){
                        System.out.println(PrintColor.printInGreen("Connection established between server node " + nodes.get(nodes.indexOf(node)-1).getNodeName() + " and client node " + node.getNodeName()));
                    } else {
                        System.out.println(PrintColor.printInRed("Connection failed between server node " + nodes.get(nodes.indexOf(node)-1).getNodeName() + " and client node " + node.getNodeName() ));
                    }
                    if (node.getNodeID()!=lastNodeID){
                        Node tempNode = node;
                        while (tempNode.getParentNode()!=null){
                            if(tempNode.createClientSocket(tempNode.getParentNode().getIP(), tempNode.getParentNode().getPort())){
                                System.out.println(PrintColor.printInGreen("Connection established between server node " + tempNode.getParentNode().getNodeName() + " and client node " + tempNode.getNodeName() ));
                            } else {
                                System.out.println(PrintColor.printInRed("Connection failed between server node " + tempNode.getParentNode().getNodeName()  + " and client node " + tempNode.getNodeName() ));
                            }

                            if (tempNode.getParentNode().createClientSocket(tempNode.getIP(), tempNode.getPort())){
                                System.out.println(PrintColor.printInGreen("Connection established between server node " +  tempNode.getNodeName() + " and client node " +  tempNode.getParentNode().getNodeName()));
                            } else {
                                System.out.println(PrintColor.printInRed("Connection failed between server node " + tempNode.getNodeName() + " and client node " +  tempNode.getParentNode().getNodeName()));
                            }
                            tempNode = tempNode.getParentNode();
                        }

                    }
                }


            }
        }
    }

    public void calculateSmallestMTU(){
        for (Layer layer: layers) {
            if(layer.getMTU() < MSS){
                MSS = layer.getMTU();
            }
        }
    }



}
