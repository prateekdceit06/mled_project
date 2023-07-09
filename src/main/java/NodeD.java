// Concrete class for last node of layers other than layer 1 which will have functionality of receiving data
// from parent node, verify the error check and sending data to child node.

import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeD extends Node {
    public NodeD(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, port, MTU, errorDetectionMethod);
    }

    public void receiveAndSendPacket() {
        Socket parentSocket = this.getParentNode().getClientSocket();

        // Check if the socket is connected before trying to receive the packet
        if (parentSocket != null && !parentSocket.isClosed()) {
            try {
                // Create an ObjectInputStream on the parent's socket
                ObjectInputStream in = new ObjectInputStream(parentSocket.getInputStream());

                // Read a Packet object from the ObjectInputStream
                Packet packet = (Packet) in.readObject();

                // Retrieve the attached hash
                int layerIDForNodeCheck = this.getLayerID();
                int nodeIDForNodeCheck = (int) (this.getNodeID() -
                        Math.pow(2, MledSimulator.getInstance().getLayerNum()) - layerIDForNodeCheck);
                String nodeName = layerIDForNodeCheck + "-" + nodeIDForNodeCheck;
                String packetHash = packet.getNodeNameValueMap().get(nodeName);

                // Verify the hash
                String packetData = packet.getData();
                byte[] packetDataBytes = packetData.getBytes();

                if (this.getErrorDetectionMethod().verify(packetDataBytes, packetHash)) {
                    // If the hash matches, add the packet to receivedData
                    this.getReceivedData().add(packet);

                    // Send the packet to the child node through its socket
                    Socket childSocket = this.getChildNode().getClientSocket();
                    ObjectOutputStream out = new ObjectOutputStream(childSocket.getOutputStream());
                    out.writeObject(packet);
                    out.flush();
                } else {
                    // Log the packet in errorsFound.txt
                    logErrorPacket(packet);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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

