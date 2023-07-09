// Concrete class for last node of layer 1 which will have functionality of writing file,
// verify the error check and receiving data from parent node.

import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeB extends Node {
    public NodeB(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, port, MTU, errorDetectionMethod);
    }

    public void receiveAndSendPacket() {
        Socket parentSocket = this.getParentNode().getClientSocket();

        // Check if the socket is connected before trying to receive the packet
        if (parentSocket != null && !parentSocket.isClosed()) {
            try {
                // Create an ObjectInputStream on the client socket
                ObjectInputStream in = new ObjectInputStream(parentSocket.getInputStream());

                // Read a Packet object from the ObjectInputStream
                Packet packet = (Packet) in.readObject();

                // Now you can do something with this packet, such as verify the hash
                int layerIDForNodeCheck = this.getLayerID();
                int nodeIDForNodeCheck = (int) (this.getNodeID() -
                        Math.pow(2, MledSimulator.getInstance().getLayerNum()) - layerIDForNodeCheck);
                String nodeName = layerIDForNodeCheck + "-" + nodeIDForNodeCheck;
                String packetHash = packet.getNodeNameValueMap().get(nodeName);
                String packetData = packet.getData();
                byte[] packetDataBytes = packetData.getBytes();

                if (this.getErrorDetectionMethod().verify(packetDataBytes, packetHash)) {
                    this.getReceivedData().add(packet);
                    writeToFile(packetData);
                } else {
                    // Log the packet in errorsFound.txt and increment the errorFoundLayer1 counter
                    logErrorPacket(packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
