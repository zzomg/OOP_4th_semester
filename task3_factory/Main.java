import Factory.Factory;

public class Main {
    public static void main(String[] args) {
        Factory factory = new Factory();
        factory.initialize(10, 10, 30, 10,
                1000, 1500, 500, 5000,
                3, 6, 3);
        /*engineStorageCapacity = 10
        * carcassStorageCapacity = 10
        * accessoriesStorageCapacity = 30
        * carStorageCapacity = 10
        * engineSupCooldown = 1000
        * carcassSupCooldown = 1500
        * accessoriesSupCooldown = 500
        * dealerCooldown = 5000
        * n_accessoriesSuppliers = 3
        * n_workers = 6
        * n_dealers = 3
        * */
        factory.start();
    }
}
