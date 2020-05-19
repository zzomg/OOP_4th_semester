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

    private final int intSize = 4;

    private final Selector selector;
    private final ServerSocketChannel serverSocket;

    private List< Pair<SelectionKey, String> > clients;

    public Server(InetSocketAddress address) throws IOException {
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

                if (key.isValid() && key.isReadable()) {
                    System.out.println("Reading from client...");
                    try {
                        readFromClient(key);
                    } catch (IOException ex1) {
                        System.out.println("INFO : Client unexpectedly disconnected.");
                        close(key.channel());
                        clients.removeIf(client -> client.getKey() == key);
                    } catch (ClassNotFoundException ex1) {
                        System.out.println("ERROR : Class not found");
                        close(key.channel());
                        clients.removeIf(client -> client.getKey() == key);
                    }
                }
                iter.remove();
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

    private void readFromClient(SelectionKey key)
            throws IOException, ClassNotFoundException
    {
        System.out.println("Getting message from client...");

        SocketChannel client = (SocketChannel) key.channel();

        // получаем от клиента сколько байт займет сообщение, которое он собирается отправить
        ByteBuffer msgCapacityBuffer = ByteBuffer.allocate(intSize);
        client.read(msgCapacityBuffer);
        msgCapacityBuffer.flip();
        int msgBufCapacity = msgCapacityBuffer.getInt();
        System.out.println(String.format("Will get %d bytes in the next message from client", msgBufCapacity));

        // создаем буфер такого размера для получения сообщения
        System.out.println("Creating new buffer for this message...");
        ByteBuffer buffer = ByteBuffer.allocate(msgBufCapacity);

        try {
            // пока не получим все нужные байты, читаем от клиента
            int gotBytes = client.read(buffer);
            System.out.println(String.format("Got %d bytes from client", gotBytes));
            while (gotBytes < msgBufCapacity) {
                gotBytes += client.read(buffer);
                System.out.println(String.format("Got %d bytes from client", gotBytes));
            }
        } catch (IOException e) {
            System.out.println("ERROR : Cannot read message from client, some problems occurred.");
            throw e;
        }

        buffer.flip();
        System.out.println("Processing message...");
        Message msg = (Message) Message.convertFromBytes (buffer.array());
        MessageType msg_type = msg.getType();
        String msg_text = msg.getMessage();

        if (msg_type == MessageType.INFO_TYPE)
        {
            clients.add(new Pair<>(key, msg_text));
            System.out.println("Forming response for a client...");
            String out = "New user registered successfully: " + msg_text;
            Message registerInfo = new Message(MessageType.INFO_TYPE, out);

            respondToClient(registerInfo, client);
        }

        if (msg_type == MessageType.TEXT_TYPE)
        {
            if (msg_text.equals(exit_cmd))
            {
                System.out.println("Forming response for a client...");
                Message exitResponse = new Message(MessageType.TEXT_TYPE, exit_cmd);

                respondToClient(exitResponse, client);

                System.out.println("Disconnecting client...");
                client.close();
                close(key.channel());
                clients.removeIf(c -> c.getKey() == key);
            }
            else if(msg_text.equals(list_clients_cmd)) {
                System.out.println("Forming response for a client...");
                StringBuilder out = new StringBuilder("Clients list:\n");
                for(int i = 0; i < clients.size(); ++i) {
                    out.append(clients.get(i).getValue());
                    if (i != clients.size() - 1) {
                        out.append("\n");
                    }
                }
                Message client_list = new Message(MessageType.TEXT_TYPE, out.toString());

                respondToClient(client_list, client);
            }
            else {
                System.out.println("Forming response for a client...");
                Message echoResponse = new Message(MessageType.TEXT_TYPE, msg_text);

                respondToClient(echoResponse, client);
            }
        }
        msgCapacityBuffer.clear();
        buffer.clear();
    }

    private void respondToClient(Message respond, SocketChannel client) throws IOException
    {
        // формируем буфер для отправки обратно клиенту
        ByteBuffer responseBuffer = ByteBuffer.wrap(Message.convertToBytes(respond));
        int responseCapacity = responseBuffer.capacity();

        // сначала отправляем клиенту информацию о том, сколько байт поступит в ответе
        ByteBuffer responseCapacityBuffer = ByteBuffer.allocate(intSize);
        responseCapacityBuffer.putInt(responseCapacity);
        responseCapacityBuffer.flip();
        System.out.println(String.format("Client will get %d bytes in the response", responseCapacity));
        client.write(responseCapacityBuffer);

        try {
            // пока в буфере что-то остается, отправляем эти данные клиенту
            System.out.println("Sending response itself to client...");
            while (responseBuffer.hasRemaining()) {
                client.write(responseBuffer);
            }
        } catch (IOException e) {
            System.out.println("ERROR : Cannot send register info to client, some problems occurred.");
            throw e;
        }
        responseBuffer.clear();
        responseCapacityBuffer.clear();
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