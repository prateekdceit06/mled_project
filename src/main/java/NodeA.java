// Concrete class for first node of layer 1 which will have functionality of reading data from file,
// generate an error check and sending it to child node

import com.sun.xml.internal.bind.v2.TODO;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;


public class NodeA extends Node {
    public NodeA(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, port, MTU, errorDetectionMethod);
    }

    public void receiveAndSendPacket() {
        int packetID = 0;
        try {
            // Read file
            String data = new String(Files.readAllBytes(Paths.get("AstroMLData.txt")));

            // Calculate the hash of the entire file content
            byte[] dataBytes = data.getBytes();
            String fileHash = this.getErrorDetectionMethod().calculate(dataBytes);

            // Split into packets and send
            LinkedList<String> packets = splitIntoPackets(data, MledSimulator.getInstance().getMSS());
            for (String packetData : packets) {
                packetID++;
                Packet packet = new Packet(String.valueOf(packetID), packetData, this.getChildNode().getNodeName(), this.getNodeName(), packetID, 0);
                packet.addToNodeNameValueMap(this.getNodeName(), fileHash); // Attach the file hash to the packet
                sendPacket(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinkedList<String> splitIntoPackets(String data, int packetSize) {
        LinkedList<String> packets = new LinkedList<>();
        int index = 0;
        while (index < data.length()) {
            packets.add(data.substring(index, Math.min(index + packetSize, data.length())));
            index += packetSize;
        }
        return packets;
    }

    public void sendPacket(Packet packet) {
        Socket childSocket = this.getChildNode().getClientSocket();

        // Check if the socket is connected before trying to send the packet
        if (childSocket != null && !childSocket.isClosed()) {
            try {
                // Create an ObjectOutputStream on the client socket
                ObjectOutputStream out = new ObjectOutputStream(childSocket.getOutputStream());

                // Write the packet object to the ObjectOutputStream
                out.writeObject(packet);

                // Flush the ObjectOutputStream to make sure the packet isn't sitting in a buffer somewhere
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


