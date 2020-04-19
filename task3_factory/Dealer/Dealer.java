package Dealer;

import Storage.Storage;

public class Dealer<T> extends Thread
{
    private long cooldown;
    private Storage<T> storage;
    private Class<T> item;

    private int threadNum;

    public Dealer(Storage<T> storage, Class<T> item, long cooldown, int threadNum) {
        this.storage = storage;
        this.item = item;
        this.cooldown = cooldown;
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                storage.get();
                System.out.println(String.format("Got new %s from storage... from thread %s",
                        item.getName(), this.threadNum));
                sleep(cooldown);
            } catch (Exception ex) {
                ex.printStackTrace();
                break;
            }
        }
    }
}
