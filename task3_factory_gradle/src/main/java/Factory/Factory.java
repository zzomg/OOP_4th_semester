package Factory;

import Car.Car;
import Dealer.Dealer;
import Details.AccessoriesDetail;
import Details.CarcassDetail;
import Details.EngineDetail;
import Exception.FactoryException;
import Storage.Storage;
import Storage.StorageController;
import Supplier.Supplier;
import Task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Factory
{
    private static final Logger logger = LogManager.getLogger(Factory.class.getName());
    private Map<String, Integer> initValues = new HashMap<>();

    private int n_accessoriesSuppliers;
    private int n_dealers;

    private ThreadPoolExecutor workers;

    private StorageController controller;

    public Storage<EngineDetail> engineDetailStorage;
    public Storage<CarcassDetail> carcassDetailStorage;
    public Storage<AccessoriesDetail> accessoriesDetailStorage;
    public Storage<Car> carStorage;

    public Supplier<EngineDetail> engineDetailSupplier;
    public Supplier<CarcassDetail> carcassDetailSupplier;
    public List< Supplier<AccessoriesDetail> > accessoriesDetailSuppliers;

    public List< Dealer<Car> > dealers;

    private static void getFactoryInitValues(Map<String, Integer> initValues)
    {
        InputStream input = Factory.class.getClassLoader().getResourceAsStream("config.properties");

        try {
            if( input == null ) {
                throw new Exception("Error : config file not found");
            }
            Properties properties = new Properties();
            properties.load(input);
            for (Object prop : properties.keySet()) {
                initValues.put(prop.toString(), Integer.parseInt(properties.getProperty(prop.toString())));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initialize()
    {
        getFactoryInitValues(initValues);

        try {
            this.engineDetailStorage = new Storage<EngineDetail>(initValues.get("engineStorageCapacity"));
            this.carcassDetailStorage = new Storage<CarcassDetail>(initValues.get("carcassStorageCapacity"));
            this.accessoriesDetailStorage = new Storage<AccessoriesDetail>(initValues.get("accessoriesStorageCapacity"));
            this.carStorage = new Storage<Car>(initValues.get("carStorageCapacity"));

            this.n_accessoriesSuppliers = initValues.get("n_accessoriesSuppliers");
            this.n_dealers = initValues.get("n_dealers");

            this.engineDetailSupplier = new Supplier<EngineDetail>(engineDetailStorage,
                    EngineDetail.class, initValues.get("engineSupCooldown"));
            this.carcassDetailSupplier = new Supplier<CarcassDetail>(carcassDetailStorage,
                    CarcassDetail.class, initValues.get("carcassSupCooldown"));

            this.accessoriesDetailSuppliers = new ArrayList<>();
            for (int i = 0; i < n_accessoriesSuppliers; ++i) {
                this.accessoriesDetailSuppliers.add(new Supplier<AccessoriesDetail>(accessoriesDetailStorage,
                        AccessoriesDetail.class, initValues.get("accessoriesSupCooldown")));
            }
            this.dealers = new ArrayList<>();
            for (int i = 0; i < n_dealers; ++i) {
                this.dealers.add(new Dealer<Car>(carStorage, Car.class, initValues.get("dealerCooldown")));
            }

            this.workers = (ThreadPoolExecutor) Executors.newFixedThreadPool(initValues.get("n_workers"));
        } catch (NullPointerException ex) {
            logger.error("Improper initialization value (not found in config file).");
            throw new FactoryException("Improper initialization value (not found in config file).");
        }  catch (Exception ex) {
            logger.error("Unexpected error during factory initialization.");
            throw new FactoryException("Unexpected error during factory initialization.");
        }

        this.controller = new StorageController(this.workers, this.engineDetailStorage,
                this.carcassDetailStorage, this.accessoriesDetailStorage, this.n_accessoriesSuppliers, this.carStorage);
    }

    public void start() {
        logger.info("Starting factory...");
        this.carStorage.addObserver(controller);

        logger.info("Starting suppliers...");
        this.engineDetailSupplier.start();
        this.carcassDetailSupplier.start();
        for(int i = 0; i < n_accessoriesSuppliers; ++i) {
            this.accessoriesDetailSuppliers.get(i).start();
        }

        logger.info("Starting dealers...");
        for(int i = 0; i < n_dealers; ++i) {
            this.dealers.get(i).start();
        }

        logger.info("Starting workflow...");
        Task task = new Task(this.engineDetailStorage, this.carcassDetailStorage,
                this.accessoriesDetailStorage, this.n_accessoriesSuppliers, this.carStorage);
        this.workers.execute(task);
    }

    public void stop() throws InterruptedException {
        logger.info("Stopping factory...");
        this.carStorage.removeObserver(controller);

        logger.info("Stopping workflow...");
        this.workers.shutdownNow();

        logger.info("Stopping suppliers...");
        this.engineDetailSupplier.interrupt();
        this.carcassDetailSupplier.interrupt();

        for(int i = 0; i < n_accessoriesSuppliers; ++i) {
            this.accessoriesDetailSuppliers.get(i).interrupt();
        }

        logger.info("Stopping dealers...");
        for(int i = 0; i < n_dealers; ++i) {
            this.dealers.get(i).interrupt();
        }

        this.workers.awaitTermination(5L, TimeUnit.SECONDS);

        this.engineDetailSupplier.join();
        this.carcassDetailSupplier.join();
        for(int i = 0; i < n_accessoriesSuppliers; ++i) {
            this.accessoriesDetailSuppliers.get(i).join();
        }
        for(int i = 0; i < n_dealers; ++i) {
            this.dealers.get(i).join();
        }
    }
}
