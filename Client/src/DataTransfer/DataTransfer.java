package DataTransfer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DataTransfer {

    private final DatagramSocket socket;
    private final InetAddress serverIP;
    private final int serverPort;
    private boolean isclose;

    public DataTransfer(DatagramSocket socket, InetAddress IPAddress, int port) {
        this.socket = socket;
        this.serverIP = IPAddress;
        this.serverPort = port;
    }

    public void send(String str) {
        byte[] sendData = new byte[2048];
        sendData = str.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
        try {
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String receive() {
        byte[] receiveData = new byte[2048];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        String res;
        try {
            socket.receive(receivePacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        res = new String(receivePacket.getData()).trim();

        return res;
    }

    public void close() {
        if (!(this.socket.isClosed())) {
            try {
                isclose = true;
                this.socket.close();
            } catch (Exception e) {
                System.err.println("Cannot close connection.");
                e.printStackTrace();
            }
        }
    }

    public boolean isClose() {
        return isclose;
    }
}
