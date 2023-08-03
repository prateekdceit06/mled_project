import java.io.File;
import java.util.List;
import java.util.Scanner;

public class CommonFunctions {

    public static void pause() {
        System.out.print("[MESSAGE] Press enter to continue.\n");
        Scanner s = new Scanner(System.in);
        s.nextLine();
    }

    public static String byteArrayToString(byte[] byteArray) {
        StringBuilder binaryString = new StringBuilder();
        for (byte b : byteArray) {
            String binaryByte = Integer.toBinaryString(b & 0xFF);
            while (binaryByte.length() < 8) {
                binaryByte = "0" + binaryByte;
            }
            binaryString.append(binaryByte);
        }
        return binaryString.toString();
    }


    public static void printNetwork(List<Layer> layers) {
        int layerNum = layers.size();
        System.out.println(PrintColor.printInGreen("==================================================Network " +
                "Topology========================================================"));
        CellVirtualConnectionEnabled virtualConnectionEnabledCell = new CellVirtualConnectionEnabled();
        CellVirtualConnectionDisabled virtualConnectionDisabledCell = new CellVirtualConnectionDisabled();
        for (int i = 1; i <= layerNum; i++) {
            int virtualLayerID = layerNum - i + 1;
            if (i < 10) {
                System.out.print(PrintColor.printInGreen("Layer 0" + virtualLayerID + ":"));
            } else {
                System.out.print(PrintColor.printInGreen("Layer " + virtualLayerID + ":"));
            }

            int loopEnd = (int) Math.pow(2, layerNum - 1) + 1;
            for (int j = 1; j <= loopEnd; j++) {
                if (layers.get(i - 1).getNodeIDs().contains(j)) {
                    if (layers.get(i - 1).isEnableErrorDetection()) {
                        CellNode nodeCell = new CellNode(j);
                        nodeCell.printCell();
                    } else {
                        CellNodeDisabled nodeCell = new CellNodeDisabled(j);
                        nodeCell.printCell();
                    }

                } else {
                    if (layers.get(i - 1).isEnableErrorDetection()) {
                        virtualConnectionEnabledCell.printCell();
                    } else {
                        virtualConnectionDisabledCell.printCell();
                    }

                }
            }
            //todo:change print statements to print layer information
//            System.out.print(PrintColor.printInGreen("  -------> "));
//            System.out.print(layers.get(i - 1));
            System.out.println();
        }
        System.out.println(PrintColor.printInGreen("=====================================================================================================" +
                "====================="));

    }

    //todo: log error packet
    public static void logErrorPacket(Packet packet, int errorCount, Node thisNode) {

        String packetID = packet.getPacketHeaders().get(packet.getPath().get(packet.getPath().size() - 1)).getPacketID();
        thisNode.getErrorDetectedInPackets().add(packetID);

        String directoryName = createFolder("output");
        String fileName = directoryName + File.separator + "errorsFound.txt";


//        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
//            out.println("Error: " + errorCount + " found by Node: " + packet.getSentFromNodeName() + "\n");
//            out.println(packet);
//            String str = new String(packet.getData(), StandardCharsets.US_ASCII);
//            out.println("\nData: " + str + "\n-----------------------------------------------------------------\n");
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //todo: log added error
    public static void logAddedError(Packet packet, Node thisNode) {
        String packetID = packet.getPacketHeaders().get(packet.getPath().get(packet.getPath().size() - 1)).getPacketID();
        thisNode.getErrorAddedToPackets().add(packetID);

        String directoryName = CommonFunctions.createFolder("output");
        String fileName = directoryName + File.separator + "errorsAdded.txt";


//        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
//            out.println("Error: " + thisNode.getErrorAddedCount() + " added by Node: " + thisNode.getNodeName() + "\n");
//            out.println(packet);
//            String str = new String(packet.getData(), StandardCharsets.US_ASCII);
//            out.println("\nData: " + str + "\n-----------------------------------------------------------------\n");
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static String createFolder(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }


        String currentWorkingDirectory = System.getProperty("user.dir");
        File directory = new File(currentWorkingDirectory + File.separator + "output");
        return directory.getName();
    }


    public static void printStats(List<Layer> layers) {

        for (Layer layer : layers) {
            for (Node node : layer.getNodes()) {
//                String output = String.format(
//                        "Node Name: %-15s Errors Added: %-7d Errors Detected: %-7d ",
//                        node.getNodeName(),
//                        node.getErrorAddedCount(),
//                        node.getErrorDetectedCount()
//                );
//todo:Change print statement for recovery

                String output = String.format(
                        "Node Name: %-15s Errors Added: %-7d Errors Detected: %-7d Actual Undetected Errors: %-7d",
                        node.getNodeName(),
                        node.getErrorAddedCount(),
                        node.getErrorDetectedCount(),
                        node.getActualUndetectedErrorsCount()
                );

                System.out.println(PrintColor.printInRedBack(output));


            }

            System.out.println(PrintColor.printInGreen(PrintColor.divider()));
        }

    }


    public static void printInterestingPacketNames(List<Layer> layers) {
        for (Layer layer : layers) {
            for (Node node : layer.getNodes()) {
                String output = String.format(
                        "Node Name: %-15s Errors Added: %-7d ",
                        node.getNodeName(),
                        node.getErrorAddedCount()
                );
                System.out.print(PrintColor.printInRedBack(output));

                if (!node.getErrorAddedToPackets().isEmpty()) {
                    System.out.println(PrintColor.printInRedBack(node.getErrorAddedToPackets().toString()));
                } else {
                    System.out.println();
                }
                output = String.format(
                        "Node Name: %-15s Errors Detected: %-4d ",
                        node.getNodeName(),
                        node.getErrorDetectedCount()
                );

                System.out.print(PrintColor.printInGreenBack(output));
                if (!node.getErrorDetectedInPackets().isEmpty()) {
                    System.out.println(PrintColor.printInGreenBack(node.getErrorDetectedInPackets().toString()));
                } else {
                    System.out.println();
                }
            }
        }
        System.out.println(PrintColor.printInGreen(PrintColor.divider()));


    }

    public static Node getNodeByName(String nodeNameForErrorCheck, int layerNum, List<Layer> layers) {
        for (int i = layerNum; i >= 1; i--) {
            for (Node node : layers.get(i - 1).getNodes()) {
                if (node.getNodeName().equals(nodeNameForErrorCheck)) {
                    return node;
                }
            }
        }
        return null;
    }

    public static boolean checkFileExistsInFolder(String folder, String fileName) {
        String rootDirectoryPath = System.getProperty("user.dir");
        String directoryPath;
        if (folder != null && folder.length() > 0) {
            directoryPath = rootDirectoryPath + File.separator + folder;
        } else {
            directoryPath = rootDirectoryPath;
        }
        File file = new File(directoryPath, fileName);
        return file.exists();
    }

}
