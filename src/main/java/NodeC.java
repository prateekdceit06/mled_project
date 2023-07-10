// Concrete class for first node of layers other than layer 1 which will have functionality of receiving data
// from parent node, generate an error check and sending it to child node.

public class NodeC extends Node {
    public NodeC(int layerID, int nodeID,  int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID,  MTU, errorDetectionMethod);
    }

    public void receivePacket(Packet packet, int totalFileSize, String valueToCheckOnWholeFile) {
        this.getReceivedData().add(packet);
        Node sendToNode = this.getSendToNode();
        packet.setSendTo(sendToNode.getNodeName());
        packet.setSentFrom(this.getNodeName());
        packet.getPath().add(this.getNodeName());
        // Generate a hash for the packet's data
        String packetData = packet.getData();
        byte[] packetDataBytes = packetData.getBytes();
        String packetValue = this.getErrorDetectionMethod().calculate(packetDataBytes);

        // Attach the new hash to the packet
        packet.addToNodeNameValueMap(this.getNodeName(), packetValue);
        // Send the packet to the child node
        sendToNode.receivePacket(packet, totalFileSize, valueToCheckOnWholeFile);
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

