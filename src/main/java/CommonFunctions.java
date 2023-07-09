import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class CommonFunctions {

    public static void pause() {
        System.out.print("[MESSAGE] Press enter to continue.\n");
        Scanner s = new Scanner(System.in);
        s.nextLine();
    }


    public static String getIP() {
        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            String hostAddress = ipAddress.getHostAddress();
            return hostAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
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





}
