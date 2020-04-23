package Storage;

import javafx.beans.binding.ObjectBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Storage<T>
{
    private long capacity;
    private LinkedList<T> storage = new LinkedList<>();

    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    public void updateObservers() {
        for(Observer o : observers) {
            o.update(this.storage.size(), this.capacity);
        }
    }

    public Storage(long capacity) {
        this.capacity = capacity;
    }

    public synchronized void add(T item) throws InterruptedException {
        while (true) {
            if(storage.size() < capacity) {
                storage.add(item);
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
                updateObservers();
                this.notify();
                return item;
            }
            this.wait();
        }
    }
}
