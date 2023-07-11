// Concrete class for all other nodes which will have functionality of receiving data from parent node,
// generate an error check, verify the error check and sending data to child node.

import java.io.FileWriter;
import java.io.PrintWriter;


public class NodeE extends Node {
    public NodeE(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod, ErrorModel errorModel) {
        super(layerID, nodeID, fragmentationParameter, errorDetectionMethod, errorModel);
    }

    public void receivePacket(Packet packet, int totalFileSize, String valueToCheckOnWholeFile) {

        // Retrieve the attached hash from parent node

        String nodeName = getNodeNameForErrorCheck();
        String packetValue = packet.getNodeNameValueMap().get(nodeName);

        // Verify the hash
        String packetData = packet.getData();
        byte[] packetDataBytes = packetData.getBytes();

        if (this.getErrorDetectionMethod().verify(packetDataBytes, packetValue)) {

            // If the hash matches, add the packet to receivedData, generate new hash and send it to the child node
            this.getReceivedData().add(packet);
            Node sendToNode = this.getSendToNode(packet.getSentFrom());
            packet.setSendTo(sendToNode.getNodeName());
            packet.setSentFrom(this.getNodeName());
            String newPacketValue = this.getErrorDetectionMethod().calculate(packetDataBytes);
            packet.addToNodeNameValueMap(this.getNodeName(), newPacketValue);
            packet.getPath().add(this.getNodeName());
            sendToNode.receivePacket(packet, totalFileSize, valueToCheckOnWholeFile);
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

    public Node getSendToNode(String packetSentFrom){
        Node node = null;
        if (this.getParentNode()!=null && !packetSentFrom.equals(this.getParentNode().getNodeName())){
            node  = this.getParentNode();
        } else if (this.getChildNode() != null) {
            node = this.getChildNode();
        } else {
            node = MledSimulator.getInstance().getLayers().get(this.getLayerID()-1).getNodes().get(this.getNodeID());
        }
        return node;
    }
}


