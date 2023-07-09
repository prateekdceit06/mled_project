// Concrete class for last node of layers other than layer 1 which will have functionality of receiving data
// from parent node, verify the error check and sending data to child node.

import java.io.FileWriter;
import java.io.PrintWriter;


public class NodeD extends Node {
    public NodeD(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, port, MTU, errorDetectionMethod);
    }

    public void receivePacket(Packet packet) {
        // Retrieve the attached hash

        String nodeName = getNodeNameForErrorCheck();
        String packetHash = packet.getNodeNameValueMap().get(nodeName);

        // Verify the hash
        String packetData = packet.getData();
        byte[] packetDataBytes = packetData.getBytes();

        if (this.getErrorDetectionMethod().verify(packetDataBytes, packetHash)) {
            // If the hash matches, add the packet to receivedData and send it to the child node
            this.getReceivedData().add(packet);
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


