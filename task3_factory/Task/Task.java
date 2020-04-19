package Task;

import Car.Car;
import Details.AccessoriesDetail;
import Details.CarcassDetail;
import Details.EngineDetail;
import Storage.Storage;

// TODO: ЭТО ТАСК (а не воркер)
// TODO: Контроллер подписан на события склада готовых изделий, он смотрит не осталось ли там мало машин
// если осталось мало (<20%), он генерит таски для тредпула (генерить = executor.submit(task))
// TODO: рандомная задержка запросов у дилеров

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
            System.out.println("Assembling a car...");
            car.setCarcass(this.carcassStorage.get());
            car.setEngine(this.engineStorage.get());
            for (int i = 0; i < n_accessories; ++i) {
                car.setAccessories(this.accessoriesStorage.get());
            }
            System.out.println("Car is assembled.");
            this.carStorage.add(car);
            System.out.println("Added new car to car storage.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
