import Factory.Factory;

import java.io.IOException;

//TODO: добавить рандомную инициализацию кулдаунов для дилеров

public class Main
{
    public static void main(String[] args) throws IOException {
        Factory factory = new Factory();
        factory.initialize();
        factory.start();
    }
}
