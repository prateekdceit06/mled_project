import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PacketHeader {
    private String packetCreatingNodeName;
    private String packetID;
    private String valueToCheck;
    private String sendTo;
    private String sentFrom;
    private int seqNum;
    private int ackNum;
    private int size;
    private List<String> path = new ArrayList<>();

    public PacketHeader(String packetCreatingNodeName, String sendTo, String sentFrom,
                        int seqNum, int ackNum, int size, String valueToCheck, List<String> path){
        this.packetCreatingNodeName = packetCreatingNodeName;
        this.packetID = packetCreatingNodeName + "-" + seqNum;
        this.sendTo = sendTo;
        this.sentFrom = sentFrom;
        this.seqNum = seqNum;
        this.ackNum = ackNum;
        this.size = size;
        this.path.add(sentFrom);
        this.valueToCheck = valueToCheck;
        if (seqNum==1){
            this.path = path;
        }

    }

    public String getPacketID() {
        return packetID;
    }

    public void setPacketID(String packetID) {
        this.packetID = packetID;
    }

    public String getValueToCheck() {
        return valueToCheck;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public int getAckNum() {
        return ackNum;
    }

    public void setAckNum(int ackNum) {
        this.ackNum = ackNum;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPacketCreatingNodeName() {
        return packetCreatingNodeName;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    //override tostring
    @Override
    public String toString() {
        return "PacketHeader{" +
                "packetID='" + packetID + '\'' +
                ", valueToCheck=" + valueToCheck +
                ", sendTo='" + sendTo + '\'' +
                ", sentFrom='" + sentFrom + '\'' +
                ", seqNum=" + seqNum +
                ", ackNum=" + ackNum +
                ", size=" + size +
                ", path=" + path +
                '}';
    }
}
