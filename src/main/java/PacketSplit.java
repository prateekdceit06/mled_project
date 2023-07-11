import java.util.LinkedList;

public class PacketSplit {
    public LinkedList<String> splitPackets(String data, int fragmentationParameter) {
        LinkedList<String> packets = new LinkedList<>();
        int index = 0;
        int packetSize = (int) Math.ceil(data.length() / (double)fragmentationParameter);
        while (index < data.length()) {
            packets.add(data.substring(index, Math.min(index + packetSize,data.length())));
            index += packetSize;
        }
        return packets;
    }
}
