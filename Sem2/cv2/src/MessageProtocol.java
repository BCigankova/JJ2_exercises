import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MessageProtocol {
    private String loggedInUser =  null;
    private boolean stopServer = false;
    private final HashMap<String, Password> users = new HashMap<>();
    private final HashMap<String, List<String>> messages = new HashMap<>();


    public MessageProtocol() throws IOException {
        //loaduje loginy
        try (Reader reader = new FileReader("/home/barbora/LS/JJ2/Sem2/users_pasw.txt");
            BufferedReader brd = new BufferedReader(reader)) {
            String line = brd.readLine();
            while(line != null && !line.isEmpty()) {
                String[] parts = line.split(" ");
                Password ps = new Password(parts[1], parts[2]);
                users.put(parts[0], ps);
                line = brd.readLine();
            }
        }

        //loaduje zpravy
        try (Reader reader = new FileReader("/home/barbora/LS/JJ2/Sem2/msgs.txt");
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
        while (run) {
            String line = rd.readLine();

            if (line == null)
                break;
            System.out.println("Request: " + line);

            String[] input = line.split(" ");

            if (input[0].trim().equalsIgnoreCase("signup")
                    && input.length == 3) {
                signUpUser(input, wr);
                saveUsers();
            } else if (input[0].trim().equalsIgnoreCase("connect")
                    && input.length == 3) {
                connectUser(input, wr);
            } else if (input[0].trim().equalsIgnoreCase("msg")
                    && input.length > 3 && input[1].equalsIgnoreCase("for") && input[2].endsWith(":") && this.loggedInUser != null) {
                messageUser(input, wr);
                saveMessages();
            } else if (input[0].trim().equalsIgnoreCase("read")
                    && input.length == 1 && this.loggedInUser != null) {
                readMessages(wr);
                saveMessages();
            } else if (input[0].trim().equalsIgnoreCase("logout")
                    && input.length == 1 && this.loggedInUser != null) {
                sendResponse("OK", wr);
                this.loggedInUser = null;
            } else if (input[0].trim().equalsIgnoreCase("quit")
                    && input.length == 1) {
                sendResponse("OK", wr);
                run = false;
                stopServer = true;
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

    private void signUpUser(String[] input, BufferedWriter wr) throws IOException, NoSuchAlgorithmException {
        if(users.containsKey(input[1])) {
            System.out.println("User " + input[1] + " already exists");
            sendResponse("ERR", wr);
        } else {
            Password ps = new Password();
            ps.hashPassword(input[2]);
            users.put(input[1], ps);    //vytvorim heslo, ktere ma tu sul udelanou

            System.out.println("User " + input[1] + " created");
            sendResponse("OK", wr);
        }
    }

    private void connectUser(String[] input, BufferedWriter wr) throws IOException, NoSuchAlgorithmException {
        String salt = users.get(input[1]).getSalt();
        String plaintextPassw = input[2];
        Password ps = new Password();
        String checkedHashedPassword = ps.getHashedPasswordWithSalt(plaintextPassw, salt);

        if(users.get(input[1]).getHashedPassword().equals(checkedHashedPassword)) {
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
        String destinationUser = input[2].substring(0, input[2].length() - 1);  //odebere :
        if(!isValidMessage(input)) {
            System.out.println("Invalid message");
            sendResponse("ERR", wr);
            return;
        }
        if (users.containsKey(destinationUser)) {
            StringBuilder message = new StringBuilder();

            for(int i = 3; i < input.length; i++)
                message.append(input[i]).append(" ");

            if(messages.get(destinationUser) == null)
                messages.put(destinationUser, new LinkedList<>());

            messages.get(destinationUser).add(loggedInUser + ": " + message);
            sendResponse("OK", wr);
        }
        else {
            System.out.println("User " + destinationUser + " doesnt exist");
            sendResponse("ERR", wr);
        }
    }

    private void readMessages(BufferedWriter wr) throws IOException {
        String response = "";
        if(!messages.isEmpty() && messages.get(loggedInUser) != null) {
            while (!messages.get(loggedInUser).isEmpty()) {
                response = response.concat("FROM " + messages.get(loggedInUser).getFirst() + "\n");
                messages.get(loggedInUser).removeFirst();
            }
        }
            sendResponse(response + "OK", wr);
    }

    private boolean isValidMessage(String[] input) {
        for(int i = 3; i < input.length; i++) {
            if (input[i].contains("\r") || input[i].contains("\n")) {
                return false;
            }
        }
        return true;
    }

    private void sendInvalidInputMessage(BufferedWriter wr) throws IOException {
        System.out.println("Invalid input, use: SIGNUP <usrname> <passw>, CONNECT <usrname> <password>, MSG FOR <usrname>, READ, LOGOUT, QUIT");
        sendResponse("ERR", wr);
    }

    private void saveUsers() throws IOException {
        try (FileWriter wr = new FileWriter("/home/barbora/LS/JJ2/Sem2/users_pasw.txt");
             PrintWriter pwr = new PrintWriter(wr)) {
            for(String username: users.keySet()) {
                Password ps = users.get(username);
                pwr.write(username + " " + ps.getHashedPassword() + " " + ps.getSalt() + "\n");
            }
        }
    }

    private void saveMessages() throws IOException {
        try (FileWriter wr = new FileWriter("/home/barbora/LS/JJ2/Sem2/msgs.txt");
             PrintWriter pwr = new PrintWriter(wr)) {

            for(String username: messages.keySet()) {
                if(!messages.get(username).isEmpty()) {   //pokud je prazdny tak at nic nepise
                    pwr.write(username);
                    for (String msg : messages.get(username)) {
                        pwr.write(";;" + msg);
                    }
                    pwr.write("\n");
                }
            }
        }
    }
}
