package POJO;

import java.net.InetAddress;

public class Client {
    private final InetAddress IPAddress;
    private final int Port;

    public Client(InetAddress ip, int port) {
        this.IPAddress = ip;
        this.Port = port;
    }

    public InetAddress getIPAddress() {
        return this.IPAddress;
    }

    public int getPort() {
        return this.Port;
    }
}
