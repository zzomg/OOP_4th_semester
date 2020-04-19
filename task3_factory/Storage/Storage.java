package Storage;

import java.util.LinkedList;

public class Storage<T> implements Observable
{
    private long capacity;
    private LinkedList<T> storage = new LinkedList<>();

    public Storage(long capacity) {
        this.capacity = capacity;
    }

    public synchronized void add(T item) throws InterruptedException {
        while (true) {
            if(storage.size() < capacity) {
                storage.add(item);
//                System.out.println(String.format("Item added to storage. Storage size: %d", storage.size()));
                this.notify();
                return;
            }
            this.wait();
        }
    }

    public synchronized T get() throws InterruptedException {
        while (true) {
            if(storage.size() > 0) {
                T item = storage.pop();
//                System.out.println(String.format("Got an item from storage. Storage size: %d", storage.size()));
                this.notify();
                return item;
            }
            this.wait();
        }
    }

    @Override
    public long getSize() {
        return storage.size();
    }

    @Override
    public long getCapacity() {
        return capacity;
    }
}
