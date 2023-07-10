
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Node {

    private final int layerID;
    private final int nodeID;
    private final String nodeName;
    private List<Packet> receivedData = new ArrayList<>();
    private final int MTU;
    private final ErrorDetectionMethod errorDetectionMethod;

    private Node parentNode;
    private Node childNode;

    private int errorCount = 0;

    public Node(int layerID, int nodeID, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        this.layerID = layerID;
        this.nodeID = nodeID;
        this.nodeName = layerID + "-" + nodeID;
        this.MTU = MTU;
        this.errorDetectionMethod = errorDetectionMethod;
        this.parentNode = null;
        this.childNode = null;
    }


    public int getLayerID() {
        return layerID;
    }

    public int getNodeID() {
        return nodeID;
    }

    public String getNodeName() {
        return nodeName;
    }

    public List<Packet> getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(List<Packet> receivedData) {
        this.receivedData = receivedData;
    }


    public int getMTU() {
        return MTU;
    }

    public ErrorDetectionMethod getErrorDetectionMethod() {
        return errorDetectionMethod;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    public Node getChildNode() {
        return childNode;
    }


    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }



    //override toString method to print out node information
    @Override
    public String toString() {
        String parentNodeName = "null";
        String childNodeName = "null";
        if (parentNode != null) {
            parentNodeName = parentNode.getNodeName();
        }
        if (childNode != null) {
            childNodeName = childNode.getNodeName();
        }
        return "Node{" +
                "layerID=" + layerID +
                ", nodeID=" + nodeID +
                ", nodeName='" + nodeName + '\'' +
                ", receivedData=" + receivedData +
                ", MTU=" + MTU +
                ", parentNode=" + parentNodeName +
                ", childNode=" + childNodeName +
                '}';
    }

    public String getNodeNameForErrorCheck(){
        int layerIDForNodeCheck = this.getLayerID();
        int nodeIDForNodeCheck = (int) (this.getNodeID() -
                Math.pow(2, MledSimulator.getInstance().getLayerNum()- layerIDForNodeCheck));
        String nodeName = layerIDForNodeCheck + "-" + nodeIDForNodeCheck;
        return nodeName;
    }



    public abstract void receivePacket(Packet packet, int totalFileSize, String valueToCheckonWholeFile);
}
