import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Server server = new Server(new InetSocketAddress("localhost", 5454));
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
