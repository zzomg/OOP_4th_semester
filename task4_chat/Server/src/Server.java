import javafx.util.Pair;

import javax.swing.text.IconView;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Server implements Runnable
{
    private static final String exit_cmd = "/exit";
    private static final String list_clients_cmd = "/who";

    private final int BUF_SIZE = 256;

    private final Selector selector;
    private final ServerSocketChannel serverSocket;
    private final ByteBuffer buffer;

    private List< Pair<SelectionKey, String> > clients;

    public Server(InetSocketAddress address) throws IOException {
        this.buffer = ByteBuffer.allocate(BUF_SIZE);
        this.selector = Selector.open();
        this.serverSocket = serverSocket(address);
        this.clients = new ArrayList<>();
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
        while (true) {
            try {
                this.selector.select();
            } catch (IOException ex) {
                System.out.println("ERROR : Server selector cannot select keys");
                break;
            }

            Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {

                SelectionKey key = iter.next();
//                System.out.println("Clients: " + clients);
//                System.out.println("Selector keys 1: ");
//                for (var k : selector.keys()) {
//                    System.out.println(k);
//                }

                if (key.isAcceptable()) {
//                    System.out.println("Key is" + key.toString());
                    System.out.println("Registering new client...");
                    try {
                        register(selector, serverSocket);
                    } catch (IOException e) {
                        System.out.println("WARNING : Cannot register new client");
                        close(key.channel());
                        continue;
                    }

                }

                if (key.isValid() && key.isReadable()) {
//                    System.out.println("Key is " + key.toString());
                    System.out.println("Reading from client...");
                    try {
                        readFromClient(buffer, key);
                    } catch (IOException ex1) {
                        System.out.println("INFO : Client disconnected.");
                        close(key.channel());
                        clients.removeIf(client -> client.getKey() == key);
                    } catch (ClassNotFoundException ex1) {
                        System.out.println("ERROR : Class not found");
                        close(key.channel());
                        clients.removeIf(client -> client.getKey() == key);
                    }
                }
                iter.remove();

//                System.out.println("Selector keys 2: ");
//                for (var k : selector.keys()) {
//                    System.out.println(k);
//                }
            }
        }
        cleanUp();
    }

    private void cleanUp() {
        for (SelectionKey key : this.selector.keys()) {
            close(key.channel());
        }
        close(this.selector);
    }

    private void readFromClient(ByteBuffer buffer, SelectionKey key)
            throws IOException, ClassNotFoundException {
        System.out.println("Getting message from client...");
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        Message msg = (Message) Message.convertFromBytes (buffer.array());
        MessageType msg_type = msg.getType();
        String msg_text = msg.getMessage();
        if (msg_type == MessageType.INFO_TYPE) {
            System.out.println(String.format("New user: %s", msg_text));
            clients.add(new Pair<>(key, msg_text));
            buffer.flip();
            client.write(buffer);
            buffer.clear();
        }
        if (msg_type == MessageType.TEXT_TYPE) {
            if (msg_text.equals(exit_cmd)) {
                client.close();
                System.out.println("Disconnecting client...");
                buffer.flip();
                client.write(buffer);
                buffer.clear();
            } else if(msg_text.equals(list_clients_cmd)) {
                /////////////////// Если оставить только эту строчку - все работает
                StringBuilder out = new StringBuilder(clients.get(0).getValue());
                /////////////////// Если с этим циклом - при чтении сообщения на клиенте в методе convertFromBytes
                // возникает EOFException
                for(int i = 0; i < clients.size(); ++i) {
                    out.append(clients.get(0).getValue());
                }
                Message client_list = new Message(MessageType.TEXT_TYPE, out.toString());
                buffer.flip();
                client.write(ByteBuffer.wrap(Message.convertToBytes(client_list)));
                buffer.clear();
            } else {
                buffer.flip();
                client.write(buffer);
                buffer.clear();
            }
        }

    }

    private void register(Selector selector,
                                 ServerSocketChannel serverSocket)
            throws IOException
    {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
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