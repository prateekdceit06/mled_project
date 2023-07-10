// Concrete class for first node of layer 1 which will have functionality of reading data from file,
// generate an error check and sending it to child node

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class NodeA extends Node {
    public NodeA(int layerID, int nodeID, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, MTU, errorDetectionMethod);
    }

    public void readFileAndSendPackets() {
        int packetID = 0;
        try {
            // Read file
            String data = new String(Files.readAllBytes(Paths.get("astroMLDataLarge.txt")));

            // Calculate the hash of the entire file content
            byte[] dataBytes = data.getBytes();
            int totalFileSize = dataBytes.length;
            String valueToCheckOnWholeFile = this.getErrorDetectionMethod().calculate(dataBytes);
            Node sendToNode = getSendToNode();

            // Split into packets and send
            LinkedList<String> packets = splitIntoPackets(data, MledSimulator.getInstance().getMSS());
            for (String packetData : packets) {
                packetID++;
                Packet packet = new Packet(String.valueOf(packetID), packetData, sendToNode.getNodeName(), this.getNodeName(), packetID, 0);
                this.getReceivedData().add(packet);
                packet.getPath().add(this.getNodeName());
                String valueToCheck = this.getErrorDetectionMethod().calculate(packetData.getBytes());
                packet.addToNodeNameValueMap(this.getNodeName(), valueToCheck); // Attach the file hash to the packet
                sendToNode.receivePacket(packet, totalFileSize, valueToCheckOnWholeFile);
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
    public void receivePacket(Packet packet, int totalFileSize, String valueToCheckonWholeFile) {
        // Do nothing
    }

    public Node getSendToNode(){
        Node node = null;
        if (this.getChildNode() != null) {
            node = this.getChildNode();
        } else {
            node = MledSimulator.getInstance().getLayers().get(this.getLayerID()-1).getNodes().get(this.getNodeID());
        }
        return node;
    }
}
