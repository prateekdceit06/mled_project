// Concrete class for first node of layer 1 which will have functionality of reading data from file,
// generate an error check and sending it to child node

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class NodeA extends Node {
    public NodeA(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod, ErrorModel errorModel) {
        super(layerID, nodeID, fragmentationParameter, errorDetectionMethod, errorModel);
    }

    public void readFileAndSendPackets() {
        int seqNum = 0;
        int MTU = MledSimulator.getInstance().getFirstLayerMTU();
        try {
            // Read file
            String data = new String(Files.readAllBytes(Paths.get("astroMLData.txt")));

            // Calculate the hash of the entire file content
            byte[] dataBytes = data.getBytes();
            int totalFileSize = dataBytes.length;
            String valueToCheckOnWholeFile = this.getErrorDetectionMethod().calculate(dataBytes);
            Node sendToNode = getSendToNode();
            // Split into packets and send
            LinkedList<String> packets = splitFile(data, MTU);
            for (String packetData : packets) {
                seqNum++;
                Packet packet = new Packet(this.getNodeName(), packetData, sendToNode.getNodeName(), this.getNodeName(), seqNum, 0);
                packet.getPath().add(this.getNodeName());
                String valueToCheck = this.getErrorDetectionMethod().calculate(packetData.getBytes());
                packet.addToNodeNameValueMap(this.getNodeName(), valueToCheck); // Attach the file hash to the packet
                sendToNode.receivePacket(packet, totalFileSize, valueToCheckOnWholeFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private LinkedList<String> splitFile(String data, int packetSize) {
        LinkedList<String> packets = new LinkedList<>();
        int index = 0;
        while (index < data.length()) {
            packets.add(data.substring(index, Math.min(index + packetSize,data.length())));
            index += packetSize;
        }
        return packets;
    }
}
