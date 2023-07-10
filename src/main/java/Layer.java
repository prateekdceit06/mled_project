import java.util.ArrayList;
import java.util.List;

public class Layer {
    private int layerID;
    private int MTU;
    private String errorDetectionMethodName;

    private ErrorDetectionMethod errorDetectionMethod;
    private List<Node> nodes = new ArrayList<>();

    private List<Integer> nodeIDs = new ArrayList<>();

    private ErrorModel errorModel;

    public Layer(int layerID, int MTU, String errorDetectionMethodName, ErrorDetectionMethod errorDetectionMethod, ErrorModel errorModel) {
        this.layerID = layerID;
        this.MTU = MTU;
        this.errorDetectionMethodName = errorDetectionMethodName;
        this.errorDetectionMethod = errorDetectionMethod;
        this.errorModel = errorModel;
    }

    public int getLayerID() {
        return layerID;
    }


    public int getMTU() {
        return MTU;
    }


    public String getErrorDetectionMethodName() {
        return errorDetectionMethodName;
    }


    public List<Node> getNodes() {
        return nodes;
    }


    public void addNode(Node node) {
        nodes.add(node);
    }

    public List<Integer> getNodeIDs() {
        return nodeIDs;
    }

    public void addNodes(int layerNum, int nodeNum, ErrorDetectionMethod errorDetectionMethod, ErrorModel errorModel){
        int increment  = (int) Math.pow(2, layerNum - layerID);
        int nodeID = 1;
        Node node;
        NodeFactory nodeFactory = new NodeFactory();
        for (int i = 1; i <= nodeNum; i++ ) {
            node = nodeFactory.getNewNode(layerID, nodeID,  MTU, errorDetectionMethod, errorModel);
//            System.out.println(PrintColor.printInPurple("Node Name" + node.getNodeName() + ", Node Class: " +
//                    node.getClass().getName()));
            addNode(node);
            nodeIDs.add(nodeID);
            nodeID += increment;
        }
    }

    //override toString method to print out layer information
    @Override
    public String toString() {
        return "Layer{" +
                "layerID=" + layerID +
                ", MTU=" + MTU +
                ", errorDetectionMethodName='" + errorDetectionMethodName + '\'' +
                ", nodes=" + nodes +
                ", nodeIDs=" + nodeIDs +
                ", errorDetectionMethod=" + errorDetectionMethod +
                ", errorModel=" + errorModel +
                '}';
    }


}
