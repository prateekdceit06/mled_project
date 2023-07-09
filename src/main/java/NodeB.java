// Concrete class for last node of layer 1 which will have functionality of writing file,
// verify the error check and receiving data from parent node.

import java.io.FileWriter;
import java.io.PrintWriter;

public class NodeB extends Node {
    public NodeB(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, port, MTU, errorDetectionMethod);
    }

    public void receivePacket(Packet packet) {

        String nodeName = getNodeNameForErrorCheck();
        String packetHash = packet.getNodeNameValueMap().get(nodeName);
        String packetData = packet.getData();
        byte[] packetDataBytes = packetData.getBytes();

        if (this.getErrorDetectionMethod().verify(packetDataBytes, packetHash)) {
            this.getReceivedData().add(packet);
            writeToFile(packetData);
        } else {
            // Log the packet in errorsFound.txt and increment the errorFoundLayer1 counter
            logErrorPacket(packet);
            this.setErrorCount(this.getErrorCount() + 1);
        }
    }

    private void writeToFile(String data) {
        try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
            out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
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

