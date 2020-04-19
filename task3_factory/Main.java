import Factory.Factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main
{
    private static void getFactoryInitValues(Map<String, Integer> initValues)
    {
        InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties");

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

    public static void main(String[] args)
    {
        Map<String, Integer> initValues = new HashMap<>();
        getFactoryInitValues(initValues);

        Factory factory = new Factory();
        factory.initialize(
                initValues.get("engineStorageCapacity"),
                initValues.get("carcassStorageCapacity"),
                initValues.get("accessoriesStorageCapacity"),
                initValues.get("carStorageCapacity"),
                initValues.get("engineSupCooldown"),
                initValues.get("carcassSupCooldown"),
                initValues.get("accessoriesSupCooldown"),
                initValues.get("dealerCooldown"),
                initValues.get("n_accessoriesSuppliers"),
                initValues.get("n_workers"),
                initValues.get("n_dealers")
        );
        factory.start();
    }
}
