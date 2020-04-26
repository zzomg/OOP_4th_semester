import Factory.Factory;
import Exception.FactoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: добавить рандомную инициализацию кулдаунов для дилеров

public class Main
{
    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        int stop;
        Factory factory = new Factory();
        try {
            factory.initialize();
            factory.start();
            do {
                stop = System.in.read();
            } while (stop != 'S' && stop != 's');
            factory.stop();
        } catch (Exception ex) {
            logger.error("Unexpected error occurred during Factory initialization.\n");
            throw new FactoryException("Unexpected error occurred during Factory initialization.\n");
        }
    }
}
