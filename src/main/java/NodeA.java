// Concrete class for first node of layer 1 which will have functionality of reading data from file,
// generate an error check and sending it to child node


public class NodeA extends Node {
    public NodeA(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod,
                 ErrorModel errorModel, int MTU) {
        super(layerID, nodeID, fragmentationParameter, errorDetectionMethod, errorModel, MTU);
    }

    public void receivePacket(Packet packet) {
        this.getReceivedData().add(packet);
        Node sendToNode = getSendToNode();
        PacketsSplitAndSend packetsSplitAndSend = new PacketsSplitAndSend();
        String receivedFromNodeName = packet.getSentFromNodeName();
        packetsSplitAndSend.splitAndSend(packet, this, sendToNode, receivedFromNodeName);
    }

    public Node getSendToNode() {
        Node node = null;
        if (this.getChildNode() != null) {
            node = this.getChildNode();
        } else {
            node = MledSimulator.getInstance().getLayers().get(this.getLayerID() - 1).getNodes().get(this.getNodeID());
        }
        return node;
    }


}
