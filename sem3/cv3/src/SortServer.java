import java.io.*;
import java.net.*;
import java.util.Arrays;

public class SortServer {

    private static boolean stopServer = false;

    public static void main(String[] args) throws IOException {
        ServerSocket srvSocket = new ServerSocket(4242);

        while (!isStopServer()) {
            System.out.println("Waiting for a client");
            Socket clientSocket = srvSocket.accept();
            if(isStopServer()){
                clientSocket.close();
                break;
            }
            Thread serverThread = new ServerThread(clientSocket);
            serverThread.start();
        }
        System.out.println("So long!");
        srvSocket.close();
    }


    private static class ServerThread extends Thread {

        private Socket clientSocket;
        private SortProtocol sp = new SortProtocol();

        public ServerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                sp.processRequests(rd, wr);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class SortProtocol {

        public void processRequests(BufferedReader rd, BufferedWriter wr) throws IOException {
            while (true) {
                String line = rd.readLine();
                if (line == null) break;

                System.out.println("Request: " + line);
                String[] input = line.split(" ");

                if (input.length == 2) {
                    try {
                        int x = Integer.parseInt(input[0]);
                        int y = Integer.parseInt(input[1]);

                        long before = System.currentTimeMillis();
                        ParalelMergesort pms = new ParalelMergesort(x, y);

                        System.out.println(Arrays.toString(pms.getArr()));
                        pms.sort();
                        System.out.println(Arrays.toString(pms.getArr()));

                        long after = System.currentTimeMillis();
                        sendResponse("t " + (after - before), wr);
                    } catch (NumberFormatException e) {
                        sendInvalidInputMessage(wr);
                    }
                } else if (input[0].trim().equalsIgnoreCase("quit")
                        && input.length == 1) {
                    sendResponse("OK", wr);
                    break;
                } else if (input[0].trim().equalsIgnoreCase("stopserver")
                        && input.length == 1) {
                    sendResponse("OK", wr);
                    setStopServer(true);
                } else {
                    sendInvalidInputMessage(wr);
                }
            }
        }

        private void sendResponse(String resp, BufferedWriter wr) throws IOException {
            System.out.println("Response: " + resp);
            wr.write(resp);
            wr.write('\n');
            wr.flush();
        }


        private void sendInvalidInputMessage(BufferedWriter wr) throws IOException {
            System.out.println("Invalid input, use: <size of array> <number of threads> OR quit OR stopserver");
            sendResponse("undefined", wr);
        }
    }


    private static synchronized void setStopServer(boolean value) {
        stopServer = value;
    }

    private static synchronized boolean isStopServer() {
        return stopServer;
    }

}
