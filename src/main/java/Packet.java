import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Packet {

    private byte[] data;

    private String sentFromNodeName;
    private HashMap<String, PacketHeader> packetHeaders = new HashMap<>();

    private List<String> path = new ArrayList<>();


    public Packet(byte[] data, PacketHeader packetHeaders, List<String> path) {
        this.data = data;
        this.packetHeaders.put(packetHeaders.getPacketCreatingNodeName(), packetHeaders);
        this.sentFromNodeName = packetHeaders.getSentFrom();
        this.path.addAll(path);
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

    public List<String> getPath() {
        return path;
    }

    //override tostring
    @Override
    public String toString() {
        return "Packet{" +
                "data='" + data + '\'' +
                ", packetHeaders=" + packetHeaders +
                ", path=" + path +
                '}';

    }
}
