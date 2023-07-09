import java.io.Serializable;
import java.util.HashMap;

public class Packet implements Serializable {
    private String packetID;
    private String data;
    private HashMap<String, String> nodeNameValueMap;
    private String sendTo;
    private String sentFrom;
    private int seqnum;
    private int acknum;
    private int size;


    public Packet(String packetID, String data, String sendTo, String sentFrom,
                  int seqnum, int acknum) {
        this.packetID = packetID;
        this.data = data;
        this.sendTo = sendTo;
        this.sentFrom = sentFrom;
        this.seqnum = seqnum;
        this.acknum = acknum;
        this.size = data.length();
    }

    public String getPacketID() {
        return packetID;
    }

    public void setPacketID(String packetID) {
        this.packetID = packetID;
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

    public void setNodeNameValueMap(HashMap<String, String> nodeNameValueMap) {
        this.nodeNameValueMap = nodeNameValueMap;
    }

    public void addToNodeNameValueMap(String nodeName, String value) {
        this.nodeNameValueMap.put(nodeName, value);
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

    public int getSeqnum() {
        return seqnum;
    }

    public void setSeqnum(int seqnum) {
        this.seqnum = seqnum;
    }

    public int getAcknum() {
        return acknum;
    }

    public void setAcknum(int acknum) {
        this.acknum = acknum;
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
                + nodeNameValueMap + ", sendTo=" + sendTo + ", sentFrom=" + sentFrom + ", seqnum=" + seqnum
                + ", acknum=" + acknum + ", size=" + size + "]";
    }
}
