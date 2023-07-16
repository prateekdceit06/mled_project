
import java.util.HashMap;
import java.util.List;

public class PacketsReassembleAndSend {
    public void reassembleAndSend(List<Packet> packetBuffer, Node thisNode, Node sendToNode,
                                  int mtu, PacketHeader packetHeaderToCheck) {

        int receivedDataSize = packetBuffer.stream().mapToInt(p -> p.getData().length).sum();
        boolean isLastBatch = packetHeaderToCheck.isLastBatch();
        int lastBatchSize = packetHeaderToCheck.getLastBatchSize();
        String packetValueToCheck = packetHeaderToCheck.getValueToCheck();

        HashMap<String, PacketHeader> oldPacketHeaders = new HashMap<>();

        if (receivedDataSize >= mtu || (isLastBatch && receivedDataSize == lastBatchSize)) {
            // If we have all the data, join all packets' data into one string
            byte[] receivedData;
            if(isLastBatch && receivedDataSize == lastBatchSize){
                receivedData = new byte[lastBatchSize];
            } else{
                receivedData = new byte[mtu];
            }

            int bytesReceived = 0;
            for (Packet p : packetBuffer) {
                int receivedPacketSize = p.getData().length;
                System.arraycopy(p.getData(), 0, receivedData, bytesReceived, receivedPacketSize);
                bytesReceived += receivedPacketSize;

                oldPacketHeaders.putAll(p.getPacketHeaders());
            }

            String valueToCheck = thisNode.getErrorDetectionMethod().calculate(receivedData);

            String sendTo = "";
            if (sendToNode == null) {
                sendTo = Constants.applicationLevel.RECEIVER.toString();
            } else {
                sendTo = sendToNode.getNodeName();
            }

            String receivedFromNodeName = packetBuffer.get(0).getSentFromNodeName();

            List<String> path = packetHeaderToCheck.getPath();

            PacketHeader newPacketHeader = new PacketHeader(thisNode.getNodeName(), sendTo,
                    thisNode.getNodeName(), packetHeaderToCheck.getSeqNum(), 0, receivedData.length,
                    valueToCheck, path, isLastBatch, lastBatchSize);
            Packet newPacket = new Packet(receivedData, newPacketHeader);

            for (String key : oldPacketHeaders.keySet()) {
                if (!key.equals(thisNode.getNodeName())) {
                    newPacket.getPacketHeaders().put(key, oldPacketHeaders.get(key));
                }
            }

            newPacket.getPacketHeaders().entrySet().removeIf(entry -> entry.getValue().equals(packetHeaderToCheck));
            thisNode.addError(newPacket);
            // Verify the hash
            boolean isCorrect = thisNode.getErrorDetectionMethod().verify(receivedData, packetValueToCheck);
            if (!isCorrect) {
                // If the hash doesn't match, log the packet in errorsFound.txt
                CommonFunctions.logErrorPacket(newPacket, thisNode.getErrorCount() + 1);
                thisNode.setErrorCount(thisNode.getErrorCount() + 1);
            }

            if (sendToNode != null && receivedFromNodeName.equals(sendToNode.getNodeName())) {
                PacketsSplitAndSend packetsSplitAndSend = new PacketsSplitAndSend();
                packetsSplitAndSend.splitAndSend(newPacket, thisNode, sendToNode, receivedFromNodeName);
            } else if (sendToNode != null) {
                sendToNode.receivePacket(newPacket);
            } else {
                ApplicationReceiver applicationReceiver = ApplicationReceiver.getInstance();
                applicationReceiver.receivePacketAndWriteToFile(newPacket);
            }
            packetBuffer.clear();
        }

    }
}
