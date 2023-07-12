// Concrete class for first node of layers other than layer 1 which will have functionality of receiving data
// from parent node, generate an error check and sending it to child node.

public class NodeC extends Node {
    public NodeC(int layerID, int nodeID,  int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod,
                 ErrorModel errorModel, int MTU) {
        super(layerID, nodeID,  fragmentationParameter, errorDetectionMethod, errorModel, MTU);
    }

    public void receivePacket(Packet packet) {
        this.getReceivedData().add(packet);
        Node sendToNode = getSendToNode();
        PacketsSplitAndSend packetsSplitAndSend = new PacketsSplitAndSend();
        String receivedFromNodeName = packet.getSentFromNodeName();
        packetsSplitAndSend.splitAndSend(packet, this, sendToNode, receivedFromNodeName);
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


