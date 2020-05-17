import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client implements Runnable
{
    private final int BUF_SIZE = 256;

    private final SocketChannel client;
    private ByteBuffer buffer;

    private static final String exit_cmd = "/exit";

    public Client(InetSocketAddress address) throws IOException{
        this.client = SocketChannel.open();
        this.client.connect(address);
        this.buffer = ByteBuffer.allocate(BUF_SIZE);
    }

    @Override
    public void run() {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String msg = "";
        String response = "";
        while (true) {
            System.out.println("Enter message:");

            try {
                msg = consoleReader.readLine();
            } catch (IOException e) {
                System.out.println("ERROR : Failed reading from console");
                break;
            }
            try {
                response = this.sendMessage(msg);
            } catch (IOException e) {
                break;
            }
            System.out.println(String.format("Got response from server: %s", response));
        }
        cleanUp();
    }

    public void cleanUp() {
        close(this.client);
        this.buffer = null;
    }

    public String sendMessage(String msg) throws IOException {
        this.buffer = ByteBuffer.wrap(msg.getBytes());
        String response;

        try {
            this.client.write(this.buffer);
        } catch (IOException e) {
            System.out.println("ERROR : Cannot write message, server is down.");
            throw e;
        }
        this.buffer.clear();
        try {
            client.read(buffer);
        } catch (IOException e) {
            System.out.println("ERROR : Cannot read answer, server is down.");
            throw e;
        }
        response = new String(buffer.array()).trim();
        if(response.equals(exit_cmd)) {
            throw new IOException();
        }
        buffer.clear();

        return response;
    }

    private void close(Closeable... closeables) {
        try {
            for (Closeable closeable : closeables)
                closeable.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
