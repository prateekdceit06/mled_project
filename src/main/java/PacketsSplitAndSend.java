import java.util.*;

public class PacketsSplitAndSend {

    public void splitAndSend(Packet packet, Node thisNode, Node sendToNode, String receivedFromNodeName) {
        byte[] data = packet.getData();

        //add path
        List<String> path;
        path = packet.getPath();
        if (!path.get(path.size() - 1).equals(thisNode.getNodeName())) {
            path.add(thisNode.getNodeName());
        }

        HashMap<String, PacketHeader> originalPacketHeaders = new HashMap<>();
        packet.getPacketHeaders().forEach((key, value) -> originalPacketHeaders.put(key, value));

        PacketHeader packetHeaderToCheck = packet.getPacketHeaders().get(receivedFromNodeName);
        int seqNum = 0;
        String packetID;
        if (data.length > thisNode.getMTU()) {
            LinkedList<byte[]> packets = splitPackets(data, thisNode.getMTU());


            for (byte[] packetDataBytes : packets) {
                boolean isLastBatch = seqNum == packets.size() - 1;
                int lastBatchSize = 0;
                if (isLastBatch) {
                    lastBatchSize = packetDataBytes.length;
                }
                String valueToCheck = thisNode.getErrorDetectionMethod().calculate(packetDataBytes);
                seqNum++;

                packetID = packetHeaderToCheck.getPacketID() + "." + thisNode.getNodeName() + "-" + seqNum;


                PacketHeader newPacketHeader = new PacketHeader(packetID, thisNode.getNodeName(), sendToNode.getNodeName(),
                        thisNode.getNodeName(), seqNum, 0, packetDataBytes.length, valueToCheck,
                        isLastBatch, lastBatchSize);
                Packet newPacket = new Packet(packetDataBytes, newPacketHeader, path);

                for (String key : originalPacketHeaders.keySet()) {
                    if (!key.equals(thisNode.getNodeName())) {
                        newPacket.getPacketHeaders().put(key, originalPacketHeaders.get(key));
                    }
                }

                addError(thisNode, newPacket);


                sendToNode.receivePacket(newPacket);

            }
        } else {
            seqNum++;
            packetID = packetHeaderToCheck.getPacketID() + "." + thisNode.getNodeName() + "-" + seqNum;

            String valueToCheck = thisNode.getErrorDetectionMethod().calculate(data);

            PacketHeader newPacketHeader = new PacketHeader(packetID, thisNode.getNodeName(), sendToNode.getNodeName(),
                    thisNode.getNodeName(), seqNum, 0, data.length, valueToCheck,
                    true, data.length);
            Packet newPacket = new Packet(data, newPacketHeader, path);
            for (String key : originalPacketHeaders.keySet()) {
                if (!key.equals(thisNode.getNodeName())) {
                    newPacket.getPacketHeaders().put(key, originalPacketHeaders.get(key));
                }
            }

            addError(thisNode, newPacket);

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

    private void addError(Node thisNode, Packet newPacket){
        Packet tempPacket = new Packet(newPacket.getData(), newPacket.getPacketHeaders(), newPacket.getPath());
        thisNode.getSentDataBeforeError().add(tempPacket);
        //todo: change if you need an error in a single packet ---> addError() method
//        thisNode.addError(newPacket);

        tempPacket = new Packet(newPacket.getData(), newPacket.getPacketHeaders(), newPacket.getPath());

        thisNode.getSentDataAfterError().add(tempPacket);
    }

}
