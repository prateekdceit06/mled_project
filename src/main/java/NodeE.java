// Concrete class for all other nodes which will have functionality of receiving data from parent node,
// generate an error check, verify the error check and sending data to child node.

import java.util.ArrayList;
import java.util.List;


public class NodeE extends Node {
    public NodeE(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod,
                 ErrorModel errorModel, int MTU) {
        super(layerID, nodeID, fragmentationParameter, errorDetectionMethod, errorModel, MTU);
    }

    private List<Packet> packetBuffer = new ArrayList<>();

    public void receivePacket(Packet packet) {
        this.getReceivedData().add(packet);
        Node sendToNode = this.getSendToNode(packet.getSentFromNodeName());
        String receivedFromNodeName = packet.getSentFromNodeName();

        if (!packet.getPacketHeaders().containsKey(this.getNodeName())) {
            // Retrieve the attached hash from parent node

            String nodeNameToGetHeaderToCheckValue = getNodeNameForErrorCheck();
            PacketHeader packetHeaderToCheck = packet.getPacketHeaders().get(nodeNameToGetHeaderToCheckValue);
            Node nodeToCheckValue = CommonFunctions.getNodeByName(nodeNameToGetHeaderToCheckValue,
                    MledSimulator.getInstance().getLayerNum(),
                    MledSimulator.getInstance().getLayers());
            packetBuffer.add(packet);

            int mtu = this.getMTU();

            PacketsReassembleAndSend packetsReassembleAndSend = new PacketsReassembleAndSend();
            packetsReassembleAndSend.reassembleAndSend(packetBuffer, this, sendToNode,
                    mtu, packetHeaderToCheck, nodeToCheckValue);

        } else {
            PacketsSplitAndSend packetsSplitAndSend = new PacketsSplitAndSend();
            packetsSplitAndSend.splitAndSend(packet, this, sendToNode, receivedFromNodeName);
        }
    }

    public Node getSendToNode(String packetSentFrom) {
        Node node = null;
        if (this.getParentNode() != null && !packetSentFrom.equals(this.getParentNode().getNodeName())) {
            node = this.getParentNode();
        } else if (this.getChildNode() != null) {
            node = this.getChildNode();
        } else {
            node = MledSimulator.getInstance().getLayers().get(this.getLayerID() - 1).getNodes().get(this.getNodeID());
        }
        return node;
    }
}


