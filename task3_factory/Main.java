import Factory.Factory;

public class Main {
    public static void main(String[] args) {
        Factory factory = new Factory();
        factory.initialize(10, 10, 30, 10,
                1000, 1500, 500, 5000,
                3, 6, 3);
        factory.start();
    }
}
