import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ApplicationSender {
    public void readFileAndSendData() {
        try {
            //todo: Change file name
            Constants.fileNameToRead = "astroMLDataTest.csv";
            String data = new String(Files.readAllBytes(Paths.get("./astroMLFiles/" + Constants.fileNameToRead)));
            // Calculate the hash of the entire file content
            ErrorDetectionMethodHash errorDetectionMethod = new ErrorDetectionMethodHash();
            byte[] dataBytes = data.getBytes();
            int totalFileSize = dataBytes.length;
            String hashOnWholeFile = errorDetectionMethod.calculate(dataBytes);
            List<String> path = new ArrayList<>();
            path.add(Constants.applicationLevel.SENDER.toString());
            PacketHeader packetHeader = new PacketHeader(Constants.applicationLevel.SENDER.toString(),
                    Constants.applicationLevel.SENDER.toString(),
                    Constants.applicationLevel.RECEIVER.toString(),
                    Constants.applicationLevel.SENDER.toString(),
                    1, 0, totalFileSize, hashOnWholeFile, true, totalFileSize);
            Packet packet = new Packet(dataBytes, packetHeader, path);
            Constants.totalFileSize = totalFileSize;
            // Send the packet to the first layer
            MledSimulator.getInstance().getLayers().get(0).getNodes().get(0).receivePacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
