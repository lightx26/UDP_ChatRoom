package App;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import POJO.Client;
import View.ServerGUI;

public class Server {

    private Set<Client> clients = new HashSet<Client>();
    private ClientHandler clientHandler;

    private int serverPort = 2626;
    private DatagramSocket serverSocket;
    // private boolean serverOn;
    private ServerGUI serverGUI;

    public Server() {
        super();
    }

    public Server(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }

    public int getPortNumber() {
        return this.serverPort;
    }

    public void setPortNumber(int port) {
        this.serverPort = port;
    }

    void openGUI() {
        serverGUI = new ServerGUI(this);
    }

    public void start() {
        try {
            serverSocket = new DatagramSocket(serverPort);
            log("Server is running at port " + serverPort);

            this.serverGUI.settingServerOn();

            clientHandler = new ClientHandler(this, serverSocket);
            clientHandler.start();
        } catch (IOException e1) {
            System.out.println("Cannot open server socket: " + e1.getMessage());
            log("Cannot start server: " + e1.getMessage());
            serverGUI.settingServerOff();
        }

    }

    public void stopServer() throws Exception {

        clientHandler.interrupt();
        clients.clear();

        if (!serverSocket.isClosed())
            serverSocket.close();
    }

    public void deliverChat(String s) throws IOException {
        for (Client client : clients) {
            clientHandler.sendChat(s, client);
        }
    }

    public void accept() {

    }

    public void addClient(Client client) {
        clients.add(client);

    }

    public void removeClient(InetAddress IP, int port) throws Exception {
        for (Client client : clients) {
            // if (client.getIPAddress() == IP && client.getPort() == port) {
            // clients.remove(client);
            // break;
            // }

            if (client.getPort() == port) {
                clients.remove(client);
                break;
            }
        }

    }

    public void log(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        serverGUI.log("[" + LocalDateTime.now().format(formatter) + "] " + s);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.openGUI();
    }
}
