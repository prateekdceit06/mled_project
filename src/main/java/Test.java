import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Test {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost", 50021);

            InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream("clientReceivedFile.txt");

            byte[] buffer = new byte[1024]; // this is your MTU
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                String str = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                System.out.println(str);
                CommonFunctions.pause();
            }

            fileOutputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}



