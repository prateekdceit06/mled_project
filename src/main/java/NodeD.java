// Concrete class for last node of layers other than layer 1 which will have functionality of receiving data
// from parent node, verify the error check and sending data to child node.

import java.util.ArrayList;
import java.util.List;


public class NodeD extends Node {
    private List<Packet> packetBuffer = new ArrayList<>();

    public NodeD(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod,
                 ErrorModel errorModel, int MTU) {
        super(layerID, nodeID, fragmentationParameter, errorDetectionMethod, errorModel, MTU);
    }

    public void receivePacket(Packet packet) {
//        this.getReceivedData().add(packet);
        String nodeNameToGetHeaderToCheckValue = getNodeNameForErrorCheck();
        PacketHeader packetHeaderToCheck = packet.getPacketHeaders().get(nodeNameToGetHeaderToCheckValue);
        packetBuffer.add(packet);
        Node sendToNode = this.getSendToNode();
        int mtu = this.getMTU();

//        System.out.println("NodeD: " + this.getNodeName()+" Packet: "+packetSize);

        PacketsReassembleAndSend packetsReassembleAndSend = new PacketsReassembleAndSend();
        packetsReassembleAndSend.reassembleAndSend(packetBuffer, this, sendToNode,
                mtu, packetHeaderToCheck);
    }


    public Node getSendToNode() {
        return this.getParentNode();
    }

}



