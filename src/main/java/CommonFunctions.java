import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
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


    public static void printNetwork(List<Layer> layers){
        int layerNum = layers.size();
        System.out.println(PrintColor.printInGreen("==================================================Network " +
                "Topology========================================================"));
        CellVirtualConnection virtualConnectionCell = new CellVirtualConnection();
        for (int i = 1; i<= layerNum; i++){
            if(i<10){
                System.out.print(PrintColor.printInGreen("Layer 0" + i + ":"));
            } else{
                System.out.print(PrintColor.printInGreen("Layer " + i + ":"));
            }

            int loopEnd = (int) Math.pow(2, layerNum-1) + 1;
            for(int j=1; j<=loopEnd;j++){
                if(layers.get(i-1).getNodeIDs().contains(j)){
                    CellNode nodeCell = new CellNode(j);
                    nodeCell.printCell();
                } else {
                    virtualConnectionCell.printCell();
                }
            }
            System.out.print(PrintColor.printInGreen("  -------> "));
            System.out.print(layers.get(i-1));
            System.out.println();
        }
        System.out.println(PrintColor.printInGreen("=====================================================================================================" +
                "====================="));

    }

    public static void logErrorPacket(Packet packet, int errorCount) {
        try (PrintWriter out = new PrintWriter(new FileWriter("errorsFound.txt", true))) {
            out.println("Error: "+ errorCount + " found by Node: " + packet.getSentFromNodeName() + "\n");
            out.println(packet);
            String str = new String(packet.getData(), StandardCharsets.US_ASCII);
            out.println("\nData: "+ str + "\n-----------------------------------------------------------------\n");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
