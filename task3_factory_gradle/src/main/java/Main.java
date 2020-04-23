import Factory.Factory;
import Exception.FactoryException;

//TODO: добавить рандомную инициализацию кулдаунов для дилеров

public class Main
{
    public static void main(String[] args) throws Exception {
        Factory factory = new Factory();
        try {
            factory.initialize();
            factory.start();
        } catch (Exception ex) {
            Factory.logger.error("Unexpected error occurred during Factory initialization.\n " +
                    "Can not start factory.");
            throw new FactoryException("Unexpected error occurred during Factory initialization.\n " +
                    "Can not start factory.");
        }
    }
}
