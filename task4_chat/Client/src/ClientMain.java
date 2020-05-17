import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class ClientMain {
    public static void main(String[] args) {
        try {
            System.out.println("Enter your name:");
            String name = readName();
            Client client = new Client(name, new InetSocketAddress("localhost", 5454));
            Thread clientThread = new Thread(client);
            System.out.println("Starting client...");
            clientThread.start();
            clientThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readName() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        return consoleReader.readLine();
    }
}

