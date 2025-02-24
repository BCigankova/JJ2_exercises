import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;

public class MessageServer {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        ServerSocket srvSocket = new ServerSocket(4242);
        MessageProtocol prl =  new MessageProtocol();

        while(!prl.stopServer()) {
            System.out.println("Waiting for a client");
            try (Socket clientSocket = srvSocket.accept();
                 BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
                prl.processRequest(rd, wr);
            }
        }

        System.out.println("So long!");
        srvSocket.close();
    }
}
