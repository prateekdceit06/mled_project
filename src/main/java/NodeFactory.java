public class NodeFactory {
    public Node getNewNode(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod){
        int lastNodeID = MledSimulator.getInstance().getLastNodeID();
        Node node = null;
        if (nodeID == 1 && layerID == 1) {
            node =  new NodeA(layerID, nodeID, port, MTU, errorDetectionMethod);
        } else if (nodeID == lastNodeID && layerID == 1) {
            node = new NodeB(layerID, nodeID, port, MTU, errorDetectionMethod);
        } else if (nodeID == 1){
            node = new NodeC(layerID, nodeID, port, MTU, errorDetectionMethod);
        } else if (nodeID == lastNodeID){
            node = new NodeD(layerID, nodeID, port, MTU, errorDetectionMethod);
        } else {
            node = new NodeE(layerID, nodeID, port, MTU, errorDetectionMethod);
        }
        return node;
    }
}
