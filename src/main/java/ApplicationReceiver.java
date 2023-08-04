import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ApplicationReceiver {
    private static ApplicationReceiver instance = null;
    private List<Packet> packetBuffer = new ArrayList<>();
    private String packetValueToCheck;
    private int packetSize;

    private ApplicationReceiver() {
    }

    //Make this class singleton
    public static ApplicationReceiver getInstance() {
        if (instance == null) {
            instance = new ApplicationReceiver();
        }
        return instance;
    }

    public void receivePacketAndWriteToFile(Packet packet) {
        String nodeNameToGetHeaderToCheckValue = Constants.applicationLevel.SENDER.toString();
        PacketHeader packetHeaderToCheck = packet.getPacketHeaders().get(nodeNameToGetHeaderToCheckValue);


        if (packetHeaderToCheck != null) {
            packetValueToCheck = packetHeaderToCheck.getValueToCheck();
            packetSize = packetHeaderToCheck.getSize();
        }
        packetBuffer.add(packet);
        int receivedDataSize = packetBuffer.stream().mapToInt(p -> p.getData().length).sum();

        if (receivedDataSize >= packetSize) {
            // If we have all the data, join all packets' data into one string
            byte[] receivedData = new byte[packetSize];
            int bytesReceived = 0;
            for (Packet p : packetBuffer) {
                int receivedPacketSize = p.getData().length;
                System.arraycopy(p.getData(), 0, receivedData, bytesReceived, receivedPacketSize);
                bytesReceived += receivedPacketSize;
            }
            ErrorDetectionMethodHash errorDetectionMethod = new ErrorDetectionMethodHash();
            boolean isCorrect = errorDetectionMethod.verify(receivedData, packetValueToCheck);
            System.out.println();
            String directoryName = CommonFunctions.createFolder("output");
            String incorrectFileName = directoryName + File.separator + "receivedData.csv";
            String correctFileName = "./astroMLFiles" + File.separator + "receivedData.csv";


            if (isCorrect) {
                createFile(correctFileName, receivedData);
                //todo: change print statements
                System.out.println(PrintColor.printInGreenBack("Received data is correct"));
            } else {
                createFile(incorrectFileName, receivedData);
                //todo: change print statements
                System.out.println(PrintColor.printInRedBack("Received data is incorrect"));
            }
            System.out.println();
            packetBuffer.clear();
        }

    }

    private void createFile(String fileName, byte[] receivedData) {
        Path path = Paths.get(fileName);
        try {
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            out.print(new String(receivedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
