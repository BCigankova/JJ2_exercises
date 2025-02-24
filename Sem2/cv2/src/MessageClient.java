import java.io.*;
import java.net.Socket;

public class MessageClient {

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 4242;

        try (Socket socket = new Socket(host, port);
             InputStream inpStream = socket.getInputStream();
             OutputStream outStream = socket.getOutputStream();
             BufferedReader rd = new BufferedReader(new InputStreamReader(inpStream));
             BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(outStream))) {

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String line = in.readLine();
                if (line == null)
                    break;
                System.out.println("Command: " + line);

                wr.write(line);
                wr.write('\n');
                wr.flush();


                String response = rd.readLine();
                while(response != null && !response.isEmpty()) {
                    System.out.println("Response: " + response);
                    if(response.equalsIgnoreCase("OK") || response.equalsIgnoreCase("ERR"))
                        break;
                    response = rd.readLine();
                }


                if (response == null)
                    break;
            }
        } catch (IOException e) {
            System.out.println("unable to initialize client");
        }
    }
}

