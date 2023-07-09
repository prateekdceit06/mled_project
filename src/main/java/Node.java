import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Node implements Runnable {

    private final int layerID;
    private final int nodeID;
    private final String nodeName;
    private final String IP;
    private final int port;
    private Socket clientSocket;
    private List<Packet> receivedData = new ArrayList<>();
    private Queue<Packet> packetQueue = new LinkedList<Packet>();
    private final int MTU;
    private final ErrorDetectionMethod errorDetectionMethod;

    private Node parentNode;
    private Node childNode;

    public Node(int layerID, int nodeID, int port, int MTU, ErrorDetectionMethod errorDetectionMethod){
        this.layerID = layerID;
        this.nodeID = nodeID;
        this.nodeName = layerID + "-" + nodeID;
        this.IP = CommonFunctions.getIP();
        this.port = port;
        this.MTU = MTU;
        this.errorDetectionMethod = errorDetectionMethod;
        this.parentNode = null;
        this.childNode = null;
        startReceivingPackets();
    }

    public void startReceivingPackets() {
        new Thread(this).start();
    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);  // Create a server socket
            Socket ss = serverSocket.accept();   // Accept an incoming client connection request
//            File file = new File("astroMLData.txt");
//            FileInputStream fileInputStream = new FileInputStream(file);
//
//            OutputStream outputStream = clientSocket.getOutputStream();
//
//            byte[] buffer = new byte[1024]; // this is your MTU
//            int bytesRead;
//
//            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            fileInputStream.close();
//            outputStream.close();
//            clientSocket.close();
//            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getLayerID() {
        return layerID;
    }

    public int getNodeID() {
        return nodeID;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getIP() {
        return IP;
    }

    public int getPort() {
        return port;
    }

    public List<Packet> getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(List<Packet> receivedData) {
        this.receivedData = receivedData;
    }

    public Queue<Packet> getPacketQueue() {
        return packetQueue;
    }

    public void setPacketQueue(Queue<Packet> packetQueue) {
        this.packetQueue = packetQueue;
    }

    public int getMTU() {
        return MTU;
    }

    public ErrorDetectionMethod getErrorDetectionMethod() {
        return errorDetectionMethod;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    public Node getChildNode() {
        return childNode;
    }


    public Socket getClientSocket() {
        return clientSocket;
    }



    //override toString method to print out node information
    @Override
    public String toString() {
        String parentNodeName = "null";
        String childNodeName = "null";
        if (parentNode != null) {
            parentNodeName = parentNode.getNodeName();
        }
        if (childNode != null) {
            childNodeName = childNode.getNodeName();
        }
        return "Node{" +
                "layerID=" + layerID +
                ", nodeID=" + nodeID +
                ", nodeName='" + nodeName + '\'' +
                ", IP='" + IP + '\'' +
                ", port=" + port +
                ", receivedData=" + receivedData +
                ", packetQueue=" + packetQueue +
                ", MTU=" + MTU +
                ", parentNode=" + parentNodeName +
                ", childNode=" + childNodeName +
                '}';
    }

    public boolean createClientSocket(String IP, int port){
        try{
            this.clientSocket = new Socket(IP, port);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
