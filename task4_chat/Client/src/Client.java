import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client implements Runnable
{
    private final int BUF_SIZE = 256;

    private final SocketChannel client;
    private ByteBuffer buffer;
    private final String name;
    private static final String exit_cmd = "/exit";

    public Client(String name, InetSocketAddress address) throws IOException{
        this.name = name;
        this.client = SocketChannel.open();
        this.client.connect(address);
        this.buffer = ByteBuffer.allocate(BUF_SIZE);
    }

    @Override
    public void run() {
        try {
            sendMessage(this.name, MessageType.INFO_TYPE);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
                response = this.sendMessage(msg, MessageType.TEXT_TYPE);
            } catch (IOException e) {
                System.out.println("ERROR : Failed sending message");
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("ERROR : Class not found");
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

    public String sendMessage(String msg, MessageType type) throws IOException, ClassNotFoundException {
        Message message = new Message(type, msg);
        this.buffer = ByteBuffer.wrap(Message.convertToBytes(message));

        try {
            this.client.write(this.buffer);
        } catch (IOException e) {
            System.out.println("ERROR : Cannot write message, server is down.");
            throw e;
        }
        this.buffer.clear();
        try {
            this.client.read(this.buffer);
        } catch (IOException e) {
            System.out.println("ERROR : Cannot read answer, server is down.");
            throw e;
        }
        Message resp = (Message) Message.convertFromBytes (this.buffer.array());
        String resp_text = resp.getMessage();
        if(resp_text.equals(exit_cmd)) {
            throw new IOException();
        }
        buffer.clear();

        return resp_text;
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