package Dealer;

import Factory.Factory;
import Storage.Storage;

public class Dealer<T> extends Thread
{
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
                Factory.logger.info(String.format("Dealer got new %s from storage.", item.getName()));
                sleep(cooldown);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // preserve interruption status
                Factory.logger.warn("Dealer thread was interrupted.");
                break;
            } catch (Exception ex) {
                Thread.currentThread().interrupt(); // preserve interruption status
                Factory.logger.error("Unexpected exception : stopping dealer thread.");
                break;
            }
        }
    }
}
