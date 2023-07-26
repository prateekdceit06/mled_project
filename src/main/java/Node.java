
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Node {
    private final int layerID;
    private final int nodeID;
    private final String nodeName;
    private final int fragmentationParameter;
    private final ErrorDetectionMethod errorDetectionMethod;
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
    private int actualUndetectedErrorsCount = 0;
    private List<Packet> undetectedErrors = new ArrayList<>();
    private int retransmittedBytes = 0;

    private boolean enableErrorDetection;



    public Node(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod,
                ErrorModel errorModel, int MTU, boolean enableErrorDetection) {

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
        this.enableErrorDetection = enableErrorDetection;
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

    public int getActualUndetectedErrorsCount() {
        return actualUndetectedErrorsCount;
    }

    public void setActualUndetectedErrorsCount(int actualUndetectedErrorsCount) {
        this.actualUndetectedErrorsCount = actualUndetectedErrorsCount;
    }

    public List<Packet> getUndetectedErrors() {
        return undetectedErrors;
    }

    public int getRetransmittedBytes() {
        return retransmittedBytes;
    }

    public void setRetransmittedBytes(int retransmittedBytes) {
        this.retransmittedBytes = retransmittedBytes;
    }

    public boolean isEnableErrorDetection() {
        return enableErrorDetection;
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
                ", Fragmentation Parameter=" + fragmentationParameter +
                ", parentNode=" + parentNodeName +
                ", childNode=" + childNodeName +
                ", errorModel=" + errorModel +
                ", MTU=" + MTU +
                ",enableErrorDetection=" + enableErrorDetection +
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
        //todo: change to toggle between bit level errors and byte level errors.
//        addBitLevelErrors(packet);
        addByteLevelErrors(packet);
    }

    public void addBitLevelErrors(Packet packet) {
        byte[] data = packet.getData();
        boolean errorAdded = false;

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
            CommonFunctions.logAddedError(packet, this );
        }
    }

    public void addByteLevelErrors(Packet packet) {
        boolean errorAdded = false;
        byte[] data = packet.getData();

        Random rand = new Random(Constants.SEED);

// Process bytes
        for (int i = 0; i < data.length; i++) {
            if (errorModel.isError()) {
                char c = (char) data[i];
                if (c >= '1' && c <= '9') {
                    char newChar;
                    do {
                        newChar = (char) (rand.nextInt(9) + 1 + '0'); // Random number between 1 and 9, then converted to ASCII
                    } while (newChar == c);

                    data[i] = (byte) newChar;
                    errorAdded = true;
                }
            }
        }

        if (errorAdded) {
            errorAddedCount++;
            CommonFunctions.logAddedError(packet, this );
        }


    }


    public abstract void receivePacket(Packet packet);




}
