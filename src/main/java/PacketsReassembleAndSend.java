import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class PacketsReassembleAndSend {
    public void reassembleAndSend(List<Packet> packetBuffer, Node sentFromNode, Node sendToNode,
                                     int packetSize, String packetValueToCheck) {
        int receivedDataSize = packetBuffer.stream().mapToInt(p -> p.getData().length).sum();
        HashMap <String, PacketHeader> oldPacketHeaders = new HashMap<>();
        if (receivedDataSize >= packetSize) {
            // If we have all the data, join all packets' data into one string
            byte[] receivedData = new byte[packetSize];
            int bytesReceived = 0;
            for (Packet p : packetBuffer) {
                int receivedPacketSize = p.getData().length;
                System.arraycopy(p.getData(), 0, receivedData, bytesReceived, receivedPacketSize);
                bytesReceived += receivedPacketSize;
                if (p.getPacketHeaders().get(p.getSentFromNodeName()).getSeqNum()==1){
                    oldPacketHeaders.putAll(p.getPacketHeaders());
                }
            }

            String sendTo = "";
            if (sendToNode == null){
                sendTo = Constants.applicationLevel.RECEIVER.toString();
            } else{
                sendTo = sendToNode.getNodeName();
            }
            String receivedFromNodeName = packetBuffer.get(0).getSentFromNodeName();
            List<String> path = packetBuffer.get(0).getPacketHeaders().get(receivedFromNodeName).getPath();
            path.add(sentFromNode.getNodeName());
            PacketHeader newPacketHeader = new PacketHeader(sentFromNode.getNodeName(), sendTo,
                    sentFromNode.getNodeName(), 1, 0, receivedData.length, "", path);
            Packet newPacket = new Packet(receivedData, newPacketHeader);
            newPacket.getPacketHeaders().putAll(oldPacketHeaders);
            sentFromNode.addError(newPacket);
            // Verify the hash
            boolean isCorrect = sentFromNode.getErrorDetectionMethod().verify(receivedData, packetValueToCheck);
            if (!isCorrect) {
                // If the hash doesn't match, log the packet in errorsFound.txt
                CommonFunctions.logErrorPacket(newPacket, sentFromNode.getErrorCount()+1);
                sentFromNode.setErrorCount(sentFromNode.getErrorCount() + 1);
            }

            if(sendToNode != null){
                sendToNode.receivePacket(newPacket);
            } else{
                ApplicationReceiver applicationReceiver = ApplicationReceiver.getInstance();
                applicationReceiver.receivePacketAndWriteToFile(newPacket);
            }
            packetBuffer.clear();
        }

    }


}
