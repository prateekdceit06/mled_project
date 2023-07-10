// Concrete class for last node of layer 1 which will have functionality of writing file,
// verify the error check and receiving data from parent node.

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class NodeB extends Node {
    private List<Packet> packetBuffer = new ArrayList<>();

    public NodeB(int layerID, int nodeID,  int MTU, ErrorDetectionMethod errorDetectionMethod) {
        super(layerID, nodeID, MTU, errorDetectionMethod);
    }

    public void receivePacket(Packet packet, int totalFileSize, String valueToCheckOnWholeFile) {



        // Add the received packet to our buffer
        packetBuffer.add(packet);

        // Check if we have received all the data
        int receivedDataSize = packetBuffer.stream().mapToInt(p -> p.getData().length()).sum();

        if (receivedDataSize >= totalFileSize) {
            // If we have all the data, join all packets' data into one string
            ByteArrayOutputStream receivedFile = new ByteArrayOutputStream();

            for (Packet p : packetBuffer) {
                try {
                    receivedFile.write(p.getData().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Then you can convert the receivedFile ByteArrayOutputStream to a byte array:
            byte[] receivedFileBytes = receivedFile.toByteArray();

            if (this.getErrorDetectionMethod().verify(receivedFileBytes, valueToCheckOnWholeFile)) {
                // If the hash matches, write the file to output.txt
                writeToFile(new String(receivedFileBytes));
            } else {
                // If the hash doesn't match, log the packet in errorsFound.txt
                for (Packet p : packetBuffer) {
                    String nodeName = getNodeNameForErrorCheck();
                    String packetValue = p.getNodeNameValueMap().get(nodeName);
                    String packetData = p.getData();
                    byte[] packetDataBytes = packetData.getBytes();
                    this.getReceivedData().add(p);

                    if (this.getErrorDetectionMethod().verify(packetDataBytes, packetValue)) {

                        packet.setSendTo("File");
                        packet.setSentFrom(this.getNodeName());
                        packet.getPath().add(this.getNodeName());
                    } else {
                        // Log the packet in errorsFound.txt and increment the errorFoundLayer1 counter
                        logErrorPacket(p);
                        this.setErrorCount(this.getErrorCount() + 1);
                    }
                }
            }
            // Clear the packet buffer for the next set of packets
            packetBuffer.clear();
        }
    }

    private void writeToFile(String data) {
        try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
            out.print(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logErrorPacket(Packet packet) {
        try (PrintWriter out = new PrintWriter(new FileWriter("errorsFound.txt", true))) {
            out.println(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

