import java.net.InetSocketAddress;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Server server = new Server(new InetSocketAddress("localhost", 5454));
            Thread serverThread = new Thread(server);
            System.out.println("Starting server...");
            serverThread.start();
            serverThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
