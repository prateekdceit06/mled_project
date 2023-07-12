// Concrete class for last node of layer 1 which will have functionality of writing file,
// verify the error check and receiving data from parent node.

import java.util.ArrayList;
import java.util.List;

public class NodeB extends Node {
    private List<Packet> packetBuffer = new ArrayList<>();

    public NodeB(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod,
                 ErrorModel errorModel, int MTU) {
        super(layerID, nodeID, fragmentationParameter, errorDetectionMethod, errorModel, MTU);
    }

    public void receivePacket(Packet packet) {
        this.getReceivedData().add(packet);
        String nodeName = getNodeNameForErrorCheck();
        PacketHeader packetHeader = packet.getPacketHeaders().get(nodeName);
        String packetValueToCheck = packetHeader.getValueToCheck();
        int packetSize = packetHeader.getSize();
        packetBuffer.add(packet);
        Node sendToNode = null;
        PacketsReassembleAndSend packetsReassembleAndSend = new PacketsReassembleAndSend();
        packetsReassembleAndSend.reassembleAndSend(packetBuffer, this, sendToNode,
                packetSize, packetValueToCheck);
    }


}

