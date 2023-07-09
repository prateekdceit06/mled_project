// Concrete class for first node of layer 1 which will have functionality of reading data from file,
// generate an error check and sending it to child node

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class NodeA extends Node {
    public NodeA(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, port, MTU, errorDetectionMethod);
    }

    public void readFileAndSendPackets() {
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
                this.getChildNode().receivePacket(packet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinkedList<String> splitIntoPackets(String data, int packetSize) {
        LinkedList<String> packets = new LinkedList<>();
        int index = 0;
        while (index < data.length()) {
            packets.add(data.substring(index, Math.min(index + packetSize,data.length())));
            index += packetSize;
        }
        return packets;
    }

    @Override
    public void receivePacket(Packet packet) {
        // Do nothing
    }
}
