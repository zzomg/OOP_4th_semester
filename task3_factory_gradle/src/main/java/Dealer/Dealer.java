package Dealer;

import Storage.Storage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dealer<T> extends Thread
{
    private static final Logger logger = LogManager.getLogger(Dealer.class.getName());

    private long cooldown;
    private Storage<T> storage;
    private Class<T> item;

    public Dealer(Storage<T> storage, Class<T> item, long cooldown) {
        this.storage = storage;
        this.item = item;
        this.cooldown = cooldown;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                storage.get();
                logger.info(String.format("Dealer got new %s from storage.", item.getName()));
                sleep(cooldown);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // preserve interruption status
                logger.warn("Dealer thread was interrupted.");
                break;
            } catch (Exception ex) {
                Thread.currentThread().interrupt(); // preserve interruption status
                logger.error("Unexpected exception : stopping dealer thread.");
                break;
            }
        }
    }
}
