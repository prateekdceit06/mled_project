import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyseNodesForErrorDetection {
    public void analyseNodesForErrorDetection(int layerNum, List<Layer> layers) {
        HashMap<Node, List<String>> nodeUndetectedErrorMap = new HashMap<>();
        for (int i = layerNum; i >= 1; i--) {
            for (Node thisNode : layers.get(i - 1).getNodes()) {
                if (!(thisNode instanceof NodeA) && !(thisNode instanceof NodeC)) {
                    String nodeNameForErrorCheck = thisNode.getNodeNameForErrorCheck();
                    Node nodeForErrorCheck = CommonFunctions.getNodeByName(nodeNameForErrorCheck, layerNum, layers);
                    List<String> detectedErrorsForThisNode = thisNode.getErrorDetectedInPackets();
                    List<String> addedErrorsByErrorCheckNode = nodeForErrorCheck.getErrorAddedToPackets();
                    addedErrorsByErrorCheckNode.removeAll(detectedErrorsForThisNode);
                    List<String> undetectedErrors = new ArrayList<>(addedErrorsByErrorCheckNode);
                    //todo:change print statements
//                    System.out.println("Node Name: " + thisNode.getNodeName() + " " + undetectedErrors);
                    nodeUndetectedErrorMap.put(thisNode, undetectedErrors);
                }
            }
        }
        //todo:change print statements
//        System.out.println(PrintColor.printInGreen(PrintColor.divider()));


        HashMap<Node, Integer> nodeUndetectedErrorCountMap = new HashMap<>();

        for (Map.Entry<Node, List<String>> entry : nodeUndetectedErrorMap.entrySet()) {
            Node thisNode = entry.getKey();
            if (thisNode instanceof NodeE) {
                List<String> undetectedErrorsPacketName = entry.getValue();
                if (!undetectedErrorsPacketName.isEmpty()) {
                    for (String undetectedErrorPacketName : undetectedErrorsPacketName) {
                        for (int i = thisNode.getLayerID() - 1; i >= 1; i--) {
                            List<Integer> nodeIDsofLayerAbove = layers.get(i - 1).getNodeIDs();
                            String nodeIDOfHigherLayerNodeToCheck =
                                    String.valueOf(
                                            getNodeIDOfHigherLayerNodeToCheck(nodeIDsofLayerAbove, thisNode.getNodeID())
                                    );
                            String nodeNameOfHigherLayerNodeToCheck = i + "-" + nodeIDOfHigherLayerNodeToCheck;
                            //todo:change print statements
                            System.out.println("Node Name: " + thisNode.getNodeName() +
                                    " nodeNameOfHigherLayerNodeToCheck: " + nodeNameOfHigherLayerNodeToCheck);
                            Node nodeOfHigherLayerToCheck =
                                    CommonFunctions.getNodeByName(nodeNameOfHigherLayerNodeToCheck, layerNum, layers);

                            String packetNameToCheckInUndetectedPacketsOnLayerAbove =
                                    getPacketNameToCheckInUndetectedPacketsOnLayerAbove(undetectedErrorPacketName, i);
                            //todo:change print statements
                            System.out.println("Node Name: " + thisNode.getNodeName() +
                                    " packetNameToCheckInUndetectedPacketsOnLayerAbove: " +
                                    packetNameToCheckInUndetectedPacketsOnLayerAbove);
                            if (nodeOfHigherLayerToCheck.getErrorDetectedInPackets() != null) {
                                if (nodeOfHigherLayerToCheck.getErrorDetectedInPackets()
                                        .contains(packetNameToCheckInUndetectedPacketsOnLayerAbove)) {
                                    nodeUndetectedErrorCountMap.put(
                                            nodeOfHigherLayerToCheck,
                                            nodeUndetectedErrorCountMap
                                                    .getOrDefault(nodeOfHigherLayerToCheck, 0) + 1);
                                    //todo:change print statements

                                    String output = String.format("Node %s could not find error in packet %s " +
                                                    "but node %s could find error in packet %s",
                                            thisNode.getNodeName(),
                                            undetectedErrorPacketName,
                                            nodeOfHigherLayerToCheck.getNodeName(),
                                            packetNameToCheckInUndetectedPacketsOnLayerAbove
                                    );
                                    System.out.println(PrintColor.printInGreenBack(output));
                                    System.out.println(PrintColor.printInGreen(PrintColor.divider()));

                                    break;
                                } else{
                                    //todo:change print statements

                                    String output = String.format("Node %s could not find error in packet %s formed by " +
                                                    "packet %s. The error was initially missed by node %s.",
                                            nodeOfHigherLayerToCheck.getNodeName(),
                                            packetNameToCheckInUndetectedPacketsOnLayerAbove,
                                            undetectedErrorPacketName,
                                            thisNode.getNodeName()
                                    );
                                    System.out.println(PrintColor.printInRedBack(output));
                                    System.out.println(PrintColor.printInGreen(PrintColor.divider()));
                                }
                            }
                        }
                    }
                }
            }


        }
        //todo:change print statements (Not necessary)

//        for (Map.Entry<Node, Integer> entry : nodeUndetectedErrorCountMap.entrySet()) {
//            String output = String.format("Node %s could find %d undetected error in packets",
//                    entry.getKey().getNodeName(),
//                    entry.getValue()
//            );
//            System.out.println(PrintColor.printInGreenBack(output));
//        }
//        System.out.println(PrintColor.printInGreen(PrintColor.divider()));
//
//        for (int i = layerNum; i >= 1; i--) {
//            for (Node thisNode : layers.get(i - 1).getNodes()) {
//                System.out.println("Node Name: " + thisNode.getNodeName() + " Correct Checksum: " + thisNode.getCheckSumCorrect());
//                System.out.println("Node Name: " + thisNode.getNodeName() + " Incorrect Checksum: " + thisNode.getCheckSumIncorrect());
//            }
//            System.out.println(PrintColor.printInGreen(PrintColor.divider()));
//
//        }
//
//        System.out.println(PrintColor.printInGreen(PrintColor.divider()));

    }


    private int getNodeIDOfHigherLayerNodeToCheck(List<Integer> nodeIDsofLayerAbove, int thisNodeID) {
        Integer minGreater = null;
        for (Integer num : nodeIDsofLayerAbove) {
            if (num.equals(thisNodeID)) {
                return num;
            } else if (num > thisNodeID) {
                minGreater = (minGreater == null) ? num : Math.min(minGreater, num);
            }
        }
        return minGreater;
    }

    private String getPacketNameToCheckInUndetectedPacketsOnLayerAbove(String undetectedErrorPacketName, int layerID) {
        int index = -1;
        int dotCount = 0;
        for (int i = 0; i < undetectedErrorPacketName.length(); i++) {
            if (undetectedErrorPacketName.charAt(i) == '.') {
                dotCount++;
                if (dotCount == layerID + 1) {
                    index = i;
                    break;
                }
            }
        }
        return undetectedErrorPacketName.substring(0, index);
    }
}
