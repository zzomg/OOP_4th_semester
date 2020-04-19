package Factory;

import Car.Car;
import Dealer.Dealer;
import Details.AccessoriesDetail;
import Details.CarcassDetail;
import Details.EngineDetail;
import Storage.Storage;
import Storage.StorageController;
import Supplier.Supplier;
import Task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Factory
{
    private int n_accessoriesSuppliers;
    private int n_dealers;

    private ThreadPoolExecutor workers;

    public Storage<EngineDetail> engineDetailStorage;
    public Storage<CarcassDetail> carcassDetailStorage;
    public Storage<AccessoriesDetail> accessoriesDetailStorage;
    public Storage<Car> carStorage;

    public Supplier<EngineDetail> engineDetailSupplier;
    public Supplier<CarcassDetail> carcassDetailSupplier;
    public List< Supplier<AccessoriesDetail> > accessoriesDetailSuppliers;

    public List< Dealer<Car> > dealers;

    // TODO: singleton constructor

    public void initialize(
            int engineStorageCapacity,
            int carcassStorageCapacity,
            int accessoriesStorageCapacity,
            int carStorageCapacity,
            int engineSupCooldown,
            int carcassSupCooldown,
            int accessoriesSupCooldown,
            int dealerCooldown,
            int n_accessoriesSuppliers,
            int n_workers,
            int n_dealers
            )
    {
        this.engineDetailStorage = new Storage<EngineDetail>(engineStorageCapacity);
        this.carcassDetailStorage = new Storage<CarcassDetail>(carcassStorageCapacity);
        this.accessoriesDetailStorage = new Storage<AccessoriesDetail>(accessoriesStorageCapacity);
        this.carStorage = new Storage<Car>(carStorageCapacity);

        this.n_accessoriesSuppliers = n_accessoriesSuppliers;
        this.n_dealers = n_dealers;

        this.engineDetailSupplier = new Supplier<EngineDetail>(engineDetailStorage,
                EngineDetail.class, engineSupCooldown, 1);
        this.carcassDetailSupplier = new Supplier<CarcassDetail>(carcassDetailStorage,
                CarcassDetail.class, carcassSupCooldown, 2);

        this.accessoriesDetailSuppliers = new ArrayList<>();
        for (int i = 0; i < n_accessoriesSuppliers; ++i) {
            this.accessoriesDetailSuppliers.add(new Supplier<AccessoriesDetail>(accessoriesDetailStorage,
                    AccessoriesDetail.class, accessoriesSupCooldown, 3));
        }
        this.dealers = new ArrayList<>();
        for(int i = 0; i < n_dealers; ++i) {
            this.dealers.add(new Dealer<Car>(carStorage, Car.class, dealerCooldown, 4));
        }

        this.workers = (ThreadPoolExecutor) Executors.newFixedThreadPool(n_workers);
    }

    public void start()
    {
        StorageController controller = new StorageController(this.workers, this.engineDetailStorage, this.carcassDetailStorage,
                this.accessoriesDetailStorage, this.n_accessoriesSuppliers, this.carStorage);
        this.carStorage.addObserver(controller);

        engineDetailSupplier.start();
        carcassDetailSupplier.start();
        for(int i = 0; i < n_accessoriesSuppliers; ++i) {
            this.accessoriesDetailSuppliers.get(i).start();
        }

        for(int i = 0; i < n_dealers; ++i) {
            this.dealers.get(i).start();
        }

        Task task = new Task(this.engineDetailStorage, this.carcassDetailStorage,
                this.accessoriesDetailStorage, this.n_accessoriesSuppliers, this.carStorage);
        this.workers.execute(task);
    }
}
