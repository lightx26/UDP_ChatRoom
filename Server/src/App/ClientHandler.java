package App;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import POJO.Client;

public class ClientHandler extends Thread {
    private final DatagramSocket socket;
    private final Server server;

    private InetAddress _ClientIPAddress;
    private int _ClientPort;

    public ClientHandler(Server server, DatagramSocket socket) {
        this.socket = socket;
        this.server = server;
    }

    // 0 for leave
    // 1 for enter
    // 2 for chat
    private static String getMessageCode(String message) {
        return message.substring(0, 1);
    }

    private static String getMessagePayload(String message) {
        return message.substring(2).trim();
    }

    @Override
    public void run() {
        while (true) {
            String s = null;
            try {
                s = receiveChat();
            } catch (IOException e) {
                // TODO: catch exception
                // e.printStackTrace();
                System.err.println("Cannot receive: " + e.getMessage());
                return;
            }

            String code = getMessageCode(s);

            if (code.equals("0")) {
                try {
                    server.removeClient(_ClientIPAddress, _ClientPort);
                    server.deliverChat("2 " + getMessagePayload(s) + " leave the conversation.");
                    server.log(getMessagePayload(s) + " leave the conversation.");
                } catch (Exception e) {
                    // TODO: handle exception
                }

            } else if (code.equals("1")) {
                try {
                    server.addClient(new Client(_ClientIPAddress, _ClientPort));
                    server.deliverChat("1 " + getMessagePayload(s) + " enter the conversation.");
                    server.log(getMessagePayload(s) + " enter the conversation.");
                } catch (IOException e) {
                    System.out.println("Cannot send confirmation.");
                }
            } else if (code.equals("2")) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    server.deliverChat(
                            "2 " + "[" + LocalDateTime.now().format(formatter) + "] " +
                                    getMessagePayload(s));
                } catch (IOException e) {
                    System.out.println("Cannot send data.");
                    return;
                }
            }
        }
    }

    // private void close() {
    // try {
    // clientSocket.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    public String receiveChat() throws IOException {
        byte[] receiveData = new byte[2048];
        if (!socket.isClosed()) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String request = new String(receivePacket.getData());

            _ClientIPAddress = receivePacket.getAddress();
            _ClientPort = receivePacket.getPort();

            return request;
        } else {
            throw new IOException("Socket is closed.");
        }
    }

    public void sendChat(String chat, Client client) throws IOException {
        byte[] sendData = new byte[2048];
        if (!socket.isClosed()) {
            DatagramPacket sendPacket;
            sendData = chat.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, client.getIPAddress(), client.getPort());
            socket.send(sendPacket);
        } else {
            System.out.println("Socket is closed.");
        }
    }
}
