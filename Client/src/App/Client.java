package App;

import java.net.DatagramSocket;
import java.net.InetAddress;

import DataTransfer.DataTransfer;
import View.ClientGUI;

public class Client {
    private DatagramSocket socket;
    private InetAddress serverIP;
    private int serverPort;
    private DataTransfer dataTransfer;

    private String username;
    private boolean _isConnected;

    public Client() {
        try {
            socket = new DatagramSocket();
            serverIP = InetAddress.getByName("localhost");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        serverPort = 2626;
    }

    public void setServerPort(int port) {
        serverPort = port;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void send(String chat) {
        dataTransfer.send(chat);
    }

    public String receive() {
        return dataTransfer.receive();
    }

    void start() {
        dataTransfer = new DataTransfer(socket, serverIP, serverPort);
        new ClientGUI(this);
        // clientGUI = new ClientGUI(this);
    }

    public boolean isConnected() {
        return this._isConnected;
    }

    public void stop() {
        _isConnected = false;
        dataTransfer.close();
    }

    public static void main(String[] args) throws Exception {
        new Client().start();
    }
}
