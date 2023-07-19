import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    private final int layerID;
    private final int nodeID;
    private final String nodeName;
    private final int fragmentationParameter;
    private final ErrorDetectionMethod errorDetectionMethod;
    private List<Packet> receivedData = new ArrayList<>();
    private List<Packet> sentDataBeforeError = new ArrayList<>();
    private List<Packet> sentDataAfterError = new ArrayList<>();
    private Node parentNode;
    private Node childNode;

    private int errorDetectedCount = 0;
    private int errorAddedCount = 0;

    private final ErrorModel errorModel;

    private int MTU;

    private List<String> errorAddedToPackets;
    private List<String> errorDetectedInPackets;

    private List<Packet> checkSumCorrect = new ArrayList<>();
    private List<Packet> checkSumIncorrect = new ArrayList<>();


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
        this.errorAddedToPackets = new ArrayList<>();
        this.errorDetectedInPackets = new ArrayList<>();
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

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    public int getErrorDetectedCount() {
        return errorDetectedCount;
    }

    public void setErrorDetectedCount(int errorDetectedCount) {
        this.errorDetectedCount = errorDetectedCount;
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

    public int getErrorAddedCount() {
        return errorAddedCount;
    }

    public List<String> getErrorAddedToPackets() {
        return errorAddedToPackets;
    }

    public List<String> getErrorDetectedInPackets() {
        return errorDetectedInPackets;
    }

    public List<Packet> getSentDataBeforeError() {
        return sentDataBeforeError;
    }

    public List<Packet> getSentDataAfterError() {
        return sentDataAfterError;
    }

    public List<Packet> getCheckSumCorrect() {
        return checkSumCorrect;
    }

    public List<Packet> getCheckSumIncorrect() {
        return checkSumIncorrect;
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
//    public abstract Node getSendToNode();

    private void logAddedError(Packet packet) {
        String packetID = packet.getPacketHeaders().get(packet.getPath().get(packet.getPath().size() - 1)).getPacketID();
        errorAddedToPackets.add(packetID);

        String directoryName = CommonFunctions.createFolder("output");
        String fileName = directoryName + File.separator + "errorsAdded.txt";


        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            out.println("Error: " + errorAddedCount + " added by Node: " + this.getNodeName() + "\n");
            out.println(packet);
            String str = new String(packet.getData(), StandardCharsets.US_ASCII);
            out.println("\nData: " + str + "\n-----------------------------------------------------------------\n");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
