import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client implements Runnable
{
    private final int intSize = 4;
    private static final String exit_cmd = "/exit";

    private final SocketChannel client;
    private final String name;

    public Client(String name, InetSocketAddress address) throws IOException{
        this.name = name;
        this.client = SocketChannel.open();
        this.client.connect(address);
    }

    @Override
    public void run() {
        try {
            sendMessage(this.name, MessageType.INFO_TYPE);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String msg;
        while (true) {
            System.out.println("Enter message:");

            try {
                msg = consoleReader.readLine();
            } catch (IOException e) {
                System.out.println("ERROR : Failed reading from console");
                break;
            }
            try {
                this.sendMessage(msg, MessageType.TEXT_TYPE);
            } catch (IOException e) {
                System.out.println("ERROR : Failed sending message");
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("ERROR : Class not found");
                break;
            }
        }
        cleanUp();
    }

    public void cleanUp() {
        close(this.client);
    }

    public void sendMessage(String msg, MessageType type) throws IOException, ClassNotFoundException
    {
        Message message = new Message(type, msg);

        // смотрим сколько байт в отправляемом сообщении
        ByteBuffer buffer = ByteBuffer.wrap(Message.convertToBytes(message));
        int bufCapacity = buffer.capacity();

        // отправляем на сервер это количество байт, чтобы он знал, сколько нужно будет считать
        ByteBuffer capacityBuffer = ByteBuffer.allocate(intSize);
        capacityBuffer.putInt(bufCapacity);
        capacityBuffer.flip();
        System.out.println(String.format("Server will get %d bytes in the message", bufCapacity));
        this.client.write(capacityBuffer);

        try {
            System.out.println("Sending message itself to server");
            // пока в буфере что-то остается, отправляем эти данные серверу
            while (buffer.hasRemaining()) {
                this.client.write(buffer);
            }
        } catch (IOException e) {
            System.out.println("ERROR : Cannot write message, server is down or other problems occurred.");
            throw e;
        }

        // читаем ответ от сервера, сколько байт будет в сообщении
        ByteBuffer responseCapacityBuffer = ByteBuffer.allocate(intSize);
        this.client.read(responseCapacityBuffer);
        responseCapacityBuffer.flip();
        int responseBufCapacity = responseCapacityBuffer.getInt();
        System.out.println(String.format("Will get %d bytes in the response from server", responseBufCapacity));

        // создаем новый буфер на это количество байт
        ByteBuffer responseBuffer = ByteBuffer.allocate(responseBufCapacity);

        try {
            // пока не получим все нужные байты, читаем от сервера
            int gotBytes = this.client.read(responseBuffer);
            System.out.println(String.format("Got %d bytes from server", gotBytes));
            while (gotBytes < responseBufCapacity) {
                gotBytes += this.client.read(responseBuffer);
                System.out.println(String.format("Got %d bytes from server", gotBytes));
            }
        } catch (IOException e) {
            System.out.println("ERROR : Cannot read answer, server is down or other problems occurred.");
            throw e;
        }
        responseBuffer.flip();
        Message resp = (Message) Message.convertFromBytes (responseBuffer.array());
        String resp_text = resp.getMessage();
        System.out.println(resp_text);
        if(resp_text.equals(exit_cmd)) {
            throw new IOException();
        }
        buffer.clear();
        capacityBuffer.clear();
        responseCapacityBuffer.clear();
        responseBuffer.clear();
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