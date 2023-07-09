// Concrete class for first node of layers other than layer 1 which will have functionality of receiving data
// from parent node, generate an error check and sending it to child node.

public class NodeC extends Node {
    public NodeC(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, port, MTU, errorDetectionMethod);
    }

    public void receivePacket(Packet packet) {
        // Generate a hash for the packet's data
        String packetData = packet.getData();
        byte[] packetDataBytes = packetData.getBytes();
        String packetHash = this.getErrorDetectionMethod().calculate(packetDataBytes);

        // Attach the new hash to the packet
        packet.addToNodeNameValueMap(this.getNodeName(), packetHash);

        // Send the packet to the child node
        this.getChildNode().receivePacket(packet);
    }
}


