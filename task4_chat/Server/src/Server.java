import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server implements Runnable
{
    private static final String exit_cmd = "/exit";

    private final int BUF_SIZE = 256;
    private volatile boolean running;

    private final Selector selector;
    private final ServerSocketChannel serverSocket;
    private final InetSocketAddress address;
    private final ByteBuffer buffer;

    Thread executionThread;

    public Server(InetSocketAddress address) throws IOException {
        this.address = address;
        this.buffer = ByteBuffer.allocate(BUF_SIZE);
        this.selector = Selector.open();
        this.serverSocket = serverSocket(address);
    }

    private ServerSocketChannel serverSocket (InetSocketAddress address) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(address);
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_ACCEPT);
        return channel;
    }

    @Override
    public void run() {
        while (this.running) {
            runMainLoop();
        }
        cleanUp();
    }

    private void cleanUp() {
        for (SelectionKey key : this.selector.keys()) {
            close(key.channel());
        }
        close(this.selector);
        this.executionThread.interrupt();
//        try {
//            this.executionThread.join();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
    }

    private void runMainLoop() {
        try {
            this.selector.select();
        } catch (IOException ex) {
            System.out.println("ERROR : Server selector cannot select keys");
            this.stop();
            return;
        }

        Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
        Iterator<SelectionKey> iter = selectedKeys.iterator();

        while (iter.hasNext()) {

            SelectionKey key = iter.next();

            if (key.isAcceptable()) {
                System.out.println("Registering new client...");
                try {
                    register(selector, serverSocket);
                } catch (IOException e) {
                    System.out.println("WARNING : Cannot register new client");
                    close(key.channel());
                    continue;
                }
            }

            if (key.isReadable()) {
                System.out.println("Reading from client...");
                try {
                    readFromClient(buffer, key);
                } catch (IOException ex1) {
                    System.out.println("INFO : Client disconnected.");
                    close(key.channel());
                }
            }
            iter.remove();
        }
    }

    private static void readFromClient(ByteBuffer buffer, SelectionKey key)
            throws IOException {
        System.out.println("Getting message from client...");
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        if (new String(buffer.array()).trim().equals(exit_cmd)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        }

        buffer.flip();
        client.write(buffer);
        buffer.clear();
    }

    private static void register(Selector selector,
                                 ServerSocketChannel serverSocket)
            throws IOException
    {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    public synchronized void start() {
        System.out.println("Starting server...");
        this.executionThread = new Thread(this);
        this.running = true;
        this.executionThread.start();
    }

    public void stop() {
        System.out.println("Stopping server...");
        running = false;
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