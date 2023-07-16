
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    private final int layerID;
    private final int nodeID;
    private final String nodeName;
    private List<Packet> receivedData = new ArrayList<>();
    private final int fragmentationParameter;
    private final ErrorDetectionMethod errorDetectionMethod;

    private Node parentNode;
    private Node childNode;

    private int errorCount = 0;
    private int errorAddedCount = 0;

    private ErrorModel errorModel;

    private int MTU;

    public Node(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod,
                ErrorModel errorModel, int MTU) {
        this.layerID = layerID;
        this.nodeID = nodeID;
        this.nodeName = layerID + "-" + nodeID;
        this.fragmentationParameter = fragmentationParameter;
        this.errorDetectionMethod = errorDetectionMethod;
        this.parentNode = null;
        this.childNode = null;
        this.errorModel = errorModel;
        this.MTU = MTU;
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


    public int getFragmentationParameter() {
        return fragmentationParameter;
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

    public ErrorModel getErrorModel() {
        return errorModel;
    }

    public int getMTU() {
        return MTU;
    }

    public void setMTU(int MTU) {
        this.MTU = MTU;
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
                ", Fragmentation Parameter=" + fragmentationParameter +
                ", parentNode=" + parentNodeName +
                ", childNode=" + childNodeName +
                ", errorModel=" + errorModel +
                '}';
    }

    public String getNodeNameForErrorCheck() {
        int layerIDForNodeCheck = this.getLayerID();
        int nodeIDForNodeCheck = (int) (this.getNodeID() -
                Math.pow(2, MledSimulator.getInstance().getLayerNum() - layerIDForNodeCheck));
        String nodeName = layerIDForNodeCheck + "-" + nodeIDForNodeCheck;
        return nodeName;
    }

    public void addError(Packet packet) {
        boolean errorAdded = false;
        byte[] data = packet.getData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 8; j++) {
                if (errorModel.isError()) {
                    // Flip j-th bit of data[i]
                    data[i] = (byte) (data[i] ^ (1 << j));
                    errorAdded = true;

                }
            }
        }
        if (errorAdded) {
            errorAddedCount++;
            logAddedError(packet);
        }
    }

    public abstract void receivePacket(Packet packet);

    private void logAddedError(Packet packet) {
        try (PrintWriter out = new PrintWriter(new FileWriter("errorsAdded.txt", true))) {
            out.println("Error: " + errorAddedCount + " added by Node: " + this.getNodeName() + "\n");
            out.println(packet);
            String str = new String(packet.getData(), StandardCharsets.US_ASCII);
            out.println("\nData: " + str + "\n-----------------------------------------------------------------\n");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
