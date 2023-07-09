// Concrete class for all other nodes which will have functionality of receiving data from parent node,
// generate an error check, verify the error check and sending data to child node.

import java.io.FileWriter;
import java.io.PrintWriter;


public class NodeE extends Node {
    public NodeE(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, port, MTU, errorDetectionMethod);
    }

    public void receivePacket(Packet packet) {
        // Retrieve the attached hash from parent node

        String nodeName = getNodeNameForErrorCheck();
        String packetHash = packet.getNodeNameValueMap().get(nodeName);

        // Verify the hash
        String packetData = packet.getData();
        byte[] packetDataBytes = packetData.getBytes();

        if (this.getErrorDetectionMethod().verify(packetDataBytes, packetHash)) {
            // If the hash matches, add the packet to receivedData, generate new hash and send it to the child node
            this.getReceivedData().add(packet);
            String newPacketHash = this.getErrorDetectionMethod().calculate(packetDataBytes);
            packet.addToNodeNameValueMap(this.getNodeName(), newPacketHash);
            this.getChildNode().receivePacket(packet);
        } else {
            // If the hash doesn't match, log the packet in errorsFound.txt
            logErrorPacket(packet);
            this.setErrorCount(this.getErrorCount() + 1);
        }
    }

    private void logErrorPacket(Packet packet) {
        try (PrintWriter out = new PrintWriter(new FileWriter("errorsFound.txt", true))) {
            out.println(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


