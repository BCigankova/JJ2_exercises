import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MessageProtocol {
    private String loggedInUser =  null;
    private boolean stopServer = false;
    private final HashMap<String, Password> users = new HashMap<>();
    private final HashMap<String, List<String>> messages = new HashMap<>();


    public MessageProtocol() throws IOException {
        //psw
        try (Reader reader = new FileReader("");
            BufferedReader brd = new BufferedReader(reader)) {
            String line = brd.readLine();
            while(line != null) {
                String[] parts = line.split(" ");
                Password ps = new Password(parts[1], parts[2]);
                users.put(parts[0], ps);
                line = brd.readLine();
            }
        }

        //msg
        try (Reader reader = new FileReader("");
             BufferedReader brd = new BufferedReader(reader)) {
            String line = brd.readLine(); //usr;;from bla bla;;from bla bla bla
            while(line != null) {
                String[] parts = line.split(";;");  //<usr, List(bla bla, bla bla bla...)
                List<String> msgs = new LinkedList<>(Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)));
                messages.put(parts[0], msgs);
                line = brd.readLine();
            }
        }
    }


    public boolean stopServer() {
        return this.stopServer;
    }

    public void processRequest(BufferedReader rd, BufferedWriter wr) throws IOException, NoSuchAlgorithmException {
        boolean run = true;
        while(run) {
            String line = rd.readLine();

            //posle se mu \n, vyprinti request od clienta
            if (line == null)
                break;
            System.out.println("Request: " + line);

            String[] input = line.split(" ");
            switch(input[0].toLowerCase()) {
                case "signup":
                    if(input.length == 3)
                        signUpUser(input, wr);
                    else
                        sendInvalidInputMessage(wr);
                    break;
                case "connect":
                    if(input.length == 3)
                        connectUser(input, wr);
                    else
                        sendInvalidInputMessage(wr);
                    break;
                case "msg":
                    if(input.length > 3 && input[1].equalsIgnoreCase("for") && input[2].endsWith(":") && this.loggedInUser != null)
                        messageUser(input, wr);
                    else
                        sendInvalidInputMessage(wr);
                    break;
                case "read":
                    if(input.length == 1 && this.loggedInUser != null) {
                        readMessages(wr);
                        sendResponse("OK", wr);
                    }
                    else
                        sendInvalidInputMessage(wr);
                    break;
                case "logout":
                    if(input.length == 1 && this.loggedInUser != null) {
                        sendResponse("OK", wr);
                        run = false;
                        this.loggedInUser = null;
                        saveUsers();
                        saveMessages();
                    }
                    sendInvalidInputMessage(wr);
                    break;
                case "quit":
                    if(input.length == 1) {
                        System.out.println("Thanks for all the fish!");
                        sendResponse("OK", wr);
                        run = false;
                        stopServer = true;
                        saveUsers();
                        saveMessages();
                    }
                    else
                        sendInvalidInputMessage(wr);
                    break;
                default:
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

    private void signUpUser(String[] input, BufferedWriter wr) throws IOException, NoSuchAlgorithmException {
        if(users.containsKey(input[1])) {
            System.out.println("User " + input[1] + " already exists");
            sendResponse("ERR", wr);
        } else {
            Password ps = new Password();
            ps.hashPassword(input[2]);
            users.put(input[1], ps);
            System.out.println("User " + input[1] + " created");
            sendResponse("OK", wr);
        }
    }

    private void connectUser(String[] input, BufferedWriter wr) throws IOException, NoSuchAlgorithmException {
        Password ps = new Password();
        ps.hashPassword(input[2]);
        if(users.get(input[1]).getHashedPassword().equals(ps.getHashedPassword())) {
            this.loggedInUser = input[1];
            System.out.println("Welcome " + input[1]);
            sendResponse("OK", wr);
        }
        else {
            System.out.println("Wrong username or password");
            sendResponse("ERR", wr);
        }
    }

    private void messageUser(String[] input, BufferedWriter wr) throws IOException {
        String destination = input[2].substring(0, input[2].length() - 1);  //odebere :
        if (users.containsKey(destination)) {
            messages.get(destination).add(loggedInUser + ": " + Arrays.toString(Arrays.copyOfRange(input, 4, input.length)));
            sendResponse("OK", wr);
        }
        else {
            System.out.println("User " + destination + " doesnt exist");
            sendResponse("ERR", wr);
        }
    }

    private void readMessages(BufferedWriter wr) throws IOException {
        while(!messages.get(loggedInUser).isEmpty()) {
            String response = "FROM " + messages.get(loggedInUser).getFirst();
            sendResponse(response, wr);
            messages.get(loggedInUser).removeFirst();
        }
        sendResponse("OK", wr);
    }

    private void sendInvalidInputMessage(BufferedWriter wr) throws IOException {
        System.out.println("Invalid input, use: SIGNUP <usrname> <passw>, CONNECT <usrname> <password>, MSG FOR <usrname>, READ, LOGOUT, QUIT");
        sendResponse("ERR", wr);
    }

    private void saveUsers() throws IOException {
        try (FileWriter wr = new FileWriter("");
             PrintWriter pwr = new PrintWriter(wr)) {
            for(String username: users.keySet()) {
                Password ps = users.get(username);
                pwr.write(username + " " + ps.getHashedPassword() + " " + ps.getSalt());
            }
        }
    }

    private void saveMessages() throws IOException {
        try (FileWriter wr = new FileWriter("");
             PrintWriter pwr = new PrintWriter(wr)) {
            for(String username: messages.keySet()) {
                pwr.write(username);
                for (String msg : messages.get(username))
                    pwr.write(";;" + msg);
            }
        }
    }
}
