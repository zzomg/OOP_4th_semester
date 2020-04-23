package Supplier;

import Factory.Factory;
import Storage.Storage;

public class Supplier<T> extends Thread
{
    private long cooldown;
    private Storage<T> storage;
    private Class<T> item;

    public Supplier(Storage<T> storage, Class<T> item, long cooldown) {
        this.storage = storage;
        this.item = item;
        this.cooldown = cooldown;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                storage.add(item.getDeclaredConstructor().newInstance());
                Factory.logger.info(String.format("Supplier added new %s to storage.", item.getName()));
                sleep(cooldown);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // preserve interruption status
                Factory.logger.warn("Supplier thread was interrupted.");
                break;
            } catch (Exception ex) {
                Thread.currentThread().interrupt(); // preserve interruption status
                Factory.logger.error("Unexpected exception : stopping supplier thread.");
                ex.printStackTrace();
                break;
            }
        }
    }
}
