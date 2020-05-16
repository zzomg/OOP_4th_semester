import java.io.IOException;
import java.net.InetSocketAddress;

// TODO: разобраться с пустыми сообщениями
// TODO: каждому клиенту свой айдишник/имя
// TODO: научить понимать команды через слэш

public class ClientMain {
    public static void main(String[] args) {
        try {
            Client client = new Client(new InetSocketAddress("localhost", 5454));
            client.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

