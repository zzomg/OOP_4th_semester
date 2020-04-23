package Task;

import Car.Car;
import Details.AccessoriesDetail;
import Details.CarcassDetail;
import Details.EngineDetail;
import Factory.Factory;
import Storage.Storage;

public class Task implements Runnable
{
    private Storage<EngineDetail> engineStorage;
    private Storage<CarcassDetail> carcassStorage;
    private Storage<AccessoriesDetail> accessoriesStorage;
    private int n_accessories;
    private Storage<Car> carStorage;

    public Task(Storage<EngineDetail> engineStorage,
                Storage<CarcassDetail> carcassStorage,
                Storage<AccessoriesDetail> accessoriesStorage,
                int n_accessories,
                Storage<Car> carStorage)
    {
        this.engineStorage = engineStorage;
        this.carcassStorage = carcassStorage;
        this.accessoriesStorage = accessoriesStorage;
        this.n_accessories = n_accessories;
        this.carStorage = carStorage;
    }

    @Override
    public void run() {
        try {
            Car car = new Car();
            Factory.logger.info("Assembling a car...");
            car.setCarcass(this.carcassStorage.get());
            car.setEngine(this.engineStorage.get());
            for (int i = 0; i < n_accessories; ++i) {
                car.setAccessories(this.accessoriesStorage.get());
            }
            Factory.logger.info("Car is assembled.");
            this.carStorage.add(car);
            Factory.logger.info("Added new car to car storage.");
        } catch (InterruptedException ex) {
            Factory.logger.warn("Interrupting workflow...");
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            Thread.currentThread().interrupt(); // preserve interruption status
            Factory.logger.error("Unexpected exception : stopping workflow thread.");
            ex.printStackTrace();
        }
    }
}
