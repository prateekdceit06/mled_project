public class NodeFactory {
    public Node getNewNode(int layerID, int nodeID, int fragmentationParameter, ErrorDetectionMethod errorDetectionMethod, ErrorModel errorModel){
        int lastNodeID = MledSimulator.getInstance().getLastNodeID();
        Node node = null;
        if (nodeID == 1 && layerID == 1) {
            node =  new NodeA(layerID, nodeID,  fragmentationParameter, errorDetectionMethod, errorModel);
        } else if (nodeID == lastNodeID && layerID == 1) {
            node = new NodeB(layerID, nodeID,  fragmentationParameter, errorDetectionMethod, errorModel);
        } else if (nodeID == 1){
            node = new NodeC(layerID, nodeID,  fragmentationParameter, errorDetectionMethod, errorModel);
        } else if (nodeID == lastNodeID){
            node = new NodeD(layerID, nodeID,  fragmentationParameter, errorDetectionMethod, errorModel);
        } else {
            node = new NodeE(layerID, nodeID,  fragmentationParameter, errorDetectionMethod, errorModel);
        }
        return node;
    }
}
