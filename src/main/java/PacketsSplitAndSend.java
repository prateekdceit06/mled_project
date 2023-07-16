import java.util.*;

public class PacketsSplitAndSend {

    public void splitAndSend(Packet packet, Node thisNode, Node sendToNode, String receivedFromNodeName) {
        byte[] data = packet.getData();


        List<String> path = new ArrayList<>();
        path = packet.getPath();
        if (!path.get(path.size() - 1).equals(thisNode.getNodeName())) {
            path.add(thisNode.getNodeName());
        }

        HashMap<String, PacketHeader> originalPacketHeaders = new HashMap<>();
        packet.getPacketHeaders().forEach((key, value) -> originalPacketHeaders.put(key, value));

        PacketHeader previousPacketHeader = packet.getPacketHeaders().get(receivedFromNodeName);

        if (data.length > thisNode.getMTU()) {
            LinkedList<byte[]> packets = splitPackets(data, thisNode.getMTU());
            int seqNum = 0;

            for (byte[] packetDataBytes : packets) {
                boolean isLastBatch = seqNum == packets.size() - 1;
                int lastBatchSize = 0;
                if (isLastBatch) {
                    lastBatchSize = packetDataBytes.length;
                }
                String valueToCheck = thisNode.getErrorDetectionMethod().calculate(packetDataBytes);
                seqNum++;


                PacketHeader newPacketHeader = new PacketHeader(thisNode.getNodeName(), sendToNode.getNodeName(),
                        thisNode.getNodeName(), seqNum, 0, packetDataBytes.length, valueToCheck,
                        isLastBatch, lastBatchSize);
                Packet newPacket = new Packet(packetDataBytes, newPacketHeader, path);

                for (String key : originalPacketHeaders.keySet()) {
                    if (!key.equals(thisNode.getNodeName())) {
                        newPacket.getPacketHeaders().put(key, originalPacketHeaders.get(key));
                    }
                }


                thisNode.addError(newPacket);
                sendToNode.receivePacket(newPacket);

            }
        } else {

            String valueToCheck = thisNode.getErrorDetectionMethod().calculate(data);


            PacketHeader newPacketHeader = new PacketHeader(thisNode.getNodeName(), sendToNode.getNodeName(),
                    thisNode.getNodeName(), previousPacketHeader.getSeqNum(), 0, data.length, valueToCheck,
                    true, data.length);
            Packet newPacket = new Packet(data, newPacketHeader, path);
            for (String key : originalPacketHeaders.keySet()) {
                if (!key.equals(thisNode.getNodeName())) {
                    newPacket.getPacketHeaders().put(key, originalPacketHeaders.get(key));
                }
            }
            thisNode.addError(newPacket);

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
