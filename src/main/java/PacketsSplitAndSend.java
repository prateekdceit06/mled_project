import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PacketsSplitAndSend {

    public void splitAndSend(Packet packet, Node sentFromNode, Node sendToNode, String receivedFromNodeName) {
        byte[] data = packet.getData();
        LinkedList<byte[]> packets = splitPackets(data, sentFromNode.getMTU());
        int seqNum = 0;
        for (byte[] packetDataBytes : packets) {
            String valueToCheck = sentFromNode.getErrorDetectionMethod().calculate(packetDataBytes);
            seqNum++;
            List<String> path = new ArrayList<>();
            if (packet.getPacketHeaders().get(receivedFromNodeName).getPath() != null) {
                path = packet.getPacketHeaders().get(receivedFromNodeName).getPath();
            }
            path.add(sentFromNode.getNodeName());
            PacketHeader newPacketHeader = new PacketHeader(sentFromNode.getNodeName(), sendToNode.getNodeName(),
                    sentFromNode.getNodeName(), seqNum, 0, packetDataBytes.length, valueToCheck, path);
            Packet newPacket = new Packet(packetDataBytes, newPacketHeader);
            packet.getPacketHeaders().forEach((key, value) -> newPacket.getPacketHeaders().put(key, value));

            if(sentFromNode instanceof NodeE && !packet.getSentFromNodeName().equals(sentFromNode.getParentNode())){
                String nodeName = sentFromNode.getNodeNameForErrorCheck();
                PacketHeader packetHeader = packet.getPacketHeaders().get(nodeName);
                String packetValueToCheck = packetHeader.getValueToCheck();
                boolean isCorrect = sentFromNode.getErrorDetectionMethod().verify(packetDataBytes, packetValueToCheck);
                if (!isCorrect) {
                    // If the hash doesn't match, log the packet in errorsFound.txt
                    CommonFunctions.logErrorPacket(newPacket, sentFromNode.getErrorCount() + 1);
                    sentFromNode.setErrorCount(sentFromNode.getErrorCount() + 1);
                }
            }

            sentFromNode.addError(newPacket);
            sendToNode.receivePacket(newPacket);

        }
    }

    private LinkedList<byte[]> splitPackets(byte[] dataBytes, int packetSize) {
        LinkedList<byte[]> packets = new LinkedList<>();
        int index = 0;
        while (index < dataBytes.length) {
            byte[] packet = Arrays.copyOfRange(dataBytes, index, Math.min(index + packetSize, dataBytes.length));
            packets.add(packet);
            index += packetSize;
        }
        return packets;
    }

}
