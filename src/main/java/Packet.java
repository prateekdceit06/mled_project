import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Packet {

    private byte[] data;

    private String sentFromNodeName;
    private HashMap<String, PacketHeader> packetHeaders = new HashMap<>();



    public Packet(byte[] data, PacketHeader packetHeaders) {
        this.data = data;
        this.packetHeaders.put(packetHeaders.getPacketCreatingNodeName(), packetHeaders);
        this.sentFromNodeName = packetHeaders.getSentFrom();
    }

    public HashMap<String, PacketHeader> getPacketHeaders() {
        return packetHeaders;
    }

    public byte[] getData() {
        return data;
    }

    public String getSentFromNodeName() {
        return sentFromNodeName;
    }

    //override tostring
    @Override
    public String toString() {
        return "Packet{" +
                "data='" + data + '\'' +
                ", packetHeaders=" + packetHeaders +
                '}';

    }
}
