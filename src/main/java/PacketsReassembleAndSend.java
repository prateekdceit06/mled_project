
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PacketsReassembleAndSend {
    public void reassembleAndSend(List<Packet> packetBuffer, Node thisNode, Node sendToNode,
                                  int mtu, PacketHeader packetHeaderToCheck, Node nodeToCheckValue) {

        int receivedDataSize = packetBuffer.stream().mapToInt(p -> p.getData().length).sum();
        boolean isLastBatch = packetHeaderToCheck.isLastBatch();
        int lastBatchSize = packetHeaderToCheck.getLastBatchSize();
        String packetValueToCheck = packetHeaderToCheck.getValueToCheck();

        HashMap<String, PacketHeader> oldPacketHeaders = new HashMap<>();

        if (receivedDataSize >= mtu || (isLastBatch && receivedDataSize == lastBatchSize)) {

            //add path
            List<String> path = new ArrayList<>();
            path = packetBuffer.get(0).getPath();
            if (!path.get(path.size() - 1).equals(thisNode.getNodeName())) {
                path.add(thisNode.getNodeName());
            }
            // If we have all the data, join all packets' data into one string
            byte[] receivedData;
            if (isLastBatch && receivedDataSize == lastBatchSize) {
                receivedData = new byte[lastBatchSize];
            } else {
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

            String packetID = packetHeaderToCheck.getPacketID();

            PacketHeader newPacketHeader = new PacketHeader(packetID, thisNode.getNodeName(), sendTo,
                    thisNode.getNodeName(), packetHeaderToCheck.getSeqNum(), 0, receivedData.length,
                    valueToCheck, isLastBatch, lastBatchSize);
            Packet newPacket = new Packet(receivedData, newPacketHeader, path);

            for (String key : oldPacketHeaders.keySet()) {
                if (!key.equals(thisNode.getNodeName())) {
                    newPacket.getPacketHeaders().put(key, oldPacketHeaders.get(key));
                }
            }

            newPacket.getPacketHeaders().entrySet().removeIf(entry -> entry.getValue().equals(packetHeaderToCheck));

            boolean isCorrect = thisNode.getErrorDetectionMethod().verify(receivedData, packetValueToCheck);


            if (!isCorrect) {
                handleIncorrectPacket(thisNode, nodeToCheckValue, newPacket, receivedData, packetID);
            } else {
                handleCorrectPacket(thisNode, nodeToCheckValue, newPacket, receivedData, packetID);
            }


            addError(thisNode, newPacket, packetHeaderToCheck);

            if (sendToNode != null) {
                sendToNode.receivePacket(newPacket);
            } else {
                ApplicationReceiver applicationReceiver = ApplicationReceiver.getInstance();
                applicationReceiver.receivePacketAndWriteToFile(newPacket);
            }
            packetBuffer.clear();
        }

    }


    private void addError(Node thisNode, Packet newPacket, PacketHeader packetHeaderToCheck) {
        Packet tempPacket = new Packet(newPacket.getData(), newPacket.getPacketHeaders(), newPacket.getPath());
        thisNode.getSentDataBeforeError().add(tempPacket);

        if (thisNode instanceof NodeA || thisNode instanceof NodeC ||
                (thisNode instanceof NodeE && thisNode.getChildNode() == null && thisNode.getParentNode() == null)
                || (thisNode instanceof NodeE && thisNode.getChildNode() != null && thisNode.getParentNode() == null)) {
            //todo: change if you need an error in a single packet ---> addError() method
            if (thisNode.getNodeName().equals("4-6") && packetHeaderToCheck.getPacketID().equals("SENDER.1-1-1.2-1-1.3-5-1.4-5-1")) {
                thisNode.addError(newPacket);
            }
            //todo: change if you need an error in a single packet ---> addError() method
//            thisNode.addError(newPacket);
        }

        tempPacket = new Packet(newPacket.getData(), newPacket.getPacketHeaders(), newPacket.getPath());

        thisNode.getSentDataAfterError().add(tempPacket);

    }

    private void handleIncorrectPacket(Node thisNode, Node nodeToCheckValue, Packet newPacket, byte[] receivedData, String packetID) {
        byte[] recoveredData = tryRecoverData(thisNode, nodeToCheckValue, newPacket, packetID, receivedData);

        if (!Arrays.equals(recoveredData, receivedData)) {
            updatePacketWithRecoveredData(thisNode, recoveredData, newPacket);
        }
    }

    private byte[] tryRecoverData(Node thisNode, Node nodeToCheckValue, Packet newPacket, String packetID, byte[] receivedData) {
        for (Packet packet : nodeToCheckValue.getSentDataBeforeError()) {
            if (packet.getPacketHeaders().get(nodeToCheckValue.getNodeName()).getPacketID().equals(packetID)) {
                CommonFunctions.logErrorPacket(newPacket, thisNode.getErrorDetectedCount() + 1, thisNode);
                thisNode.setErrorDetectedCount(thisNode.getErrorDetectedCount() + 1);
                return packet.getData();
            }
        }
        return new byte[receivedData.length];
    }

    private void updatePacketWithRecoveredData(Node thisNode, byte[] recoveredData, Packet newPacket) {
        String recoveredValueToCheck = thisNode.getErrorDetectionMethod().calculate(recoveredData);
        newPacket.getPacketHeaders().get(thisNode.getNodeName()).setValueToCheck(recoveredValueToCheck);
        newPacket.setData(recoveredData);
        thisNode.setRetransmissionCount(thisNode.getRetransmissionCount() + 1);
    }

    private void handleCorrectPacket(Node thisNode, Node nodeToCheckValue, Packet newPacket, byte[] receivedData, String packetID) {
        for (Packet packet : nodeToCheckValue.getSentDataBeforeError()) {
            if (packet.getPacketHeaders().get(nodeToCheckValue.getNodeName()).getPacketID().equals(packetID)) {
                if (!Arrays.equals(packet.getData(), receivedData)) {
                    increaseActualUndetectedErrors(thisNode, newPacket);
                }
            }
        }
    }

    private void increaseActualUndetectedErrors(Node thisNode, Packet newPacket) {
        thisNode.setActualUndetectedErrorsCount(thisNode.getActualUndetectedErrorsCount() + 1);
        Packet tempPacket = new Packet(newPacket.getData(), newPacket.getPacketHeaders(), newPacket.getPath());
        thisNode.getUndetectedErrors().add(tempPacket);
    }

}
