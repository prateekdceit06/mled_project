import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Packet {
    private String packetID;
    private String data;
    private HashMap<String, String> nodeNameValueMap;
    private String sendTo;
    private String sentFrom;
    private int seqNum;
    private int ackNum;
    private int size;

    private List<String> path;


    public Packet(String packetID, String data, String sendTo, String sentFrom, int seqNum, int ackNum) {
        this.packetID = packetID;
        this.data = data;
        this.sendTo = sendTo;
        this.sentFrom = sentFrom;
        this.seqNum = seqNum;
        this.ackNum = ackNum;
        this.size = data.length();
        this.nodeNameValueMap = new HashMap<>();
        this.path = new ArrayList<>();
    }

    public String getPacketID() {
        return packetID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public HashMap<String, String> getNodeNameValueMap() {
        return nodeNameValueMap;
    }

    public void addToNodeNameValueMap(String nodeName, String value) {
        this.nodeNameValueMap.put(nodeName, value);
    }

    public String getSendTo() {
        return sendTo;
    }

    public List<String> getPath() {
        return path;
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


    public int getAckNum() {
        return ackNum;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }



    //override tostring
    @Override
    public String toString() {
        return "Packet [packetID=" + packetID + ", data=" + data + ",  nodeNameValueMap="
                + nodeNameValueMap + ", sendTo=" + sendTo + ", sentFrom=" + sentFrom + ", seqNum=" + seqNum
                + ", ackNum=" + ackNum + ", size=" + size + ", path=" + path + "]";
    }
}
