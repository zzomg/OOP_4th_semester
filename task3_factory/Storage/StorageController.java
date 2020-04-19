package Storage;

import Car.Car;
import Details.AccessoriesDetail;
import Details.CarcassDetail;
import Details.EngineDetail;
import Task.Task;

import java.util.concurrent.ThreadPoolExecutor;

// observer
public class StorageController implements Observer
{
    private ThreadPoolExecutor executor;
    private Storage<EngineDetail> engineStorage;
    private Storage<CarcassDetail> carcassStorage;
    private Storage<AccessoriesDetail> accessoriesStorage;
    private int n_accessories;
    private Storage<Car> carStorage;

    public StorageController(
            ThreadPoolExecutor executor,
            Storage<EngineDetail> engineStorage,
            Storage<CarcassDetail> carcassStorage,
            Storage<AccessoriesDetail> accessoriesStorage,
            int n_accessories,
            Storage<Car> carStorage
        )
    {
        this.executor = executor;
        this.engineStorage = engineStorage;
        this.carcassStorage = carcassStorage;
        this.accessoriesStorage = accessoriesStorage;
        this.n_accessories = n_accessories;
        this.carStorage = carStorage;
    }

    @Override
    public void update(long size, long capacity) {
        System.out.println("Checking car storage...");
        System.out.println(String.format("Car storage size: %d", size));

        // try and fill storage up to 80% of capacity
        if(size < (long)(capacity * 0.2)) {
            for(int i = 0; i < (long)(capacity * 0.8) - size; ++i) {
                Task task = new Task(engineStorage, carcassStorage, accessoriesStorage, n_accessories, carStorage);
                executor.execute(task);
            }
            System.out.println(String.format("Controller : Added %d tasks to workflow.",
                    (long)(capacity * 0.8) - size));
        }
    }
}
