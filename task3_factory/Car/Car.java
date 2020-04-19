package Car;

import Details.AccessoriesDetail;
import Details.CarcassDetail;
import Details.EngineDetail;

import java.util.LinkedList;

public class Car
{
    private EngineDetail engine;
    private CarcassDetail carcass;
    private LinkedList<AccessoriesDetail> accessories = new LinkedList<>();

    public Car() {}

    public void setEngine(EngineDetail engine) {
        this.engine = engine;
    }

    public void setCarcass(CarcassDetail carcass) {
        this.carcass = carcass;
    }

    public void setAccessories(AccessoriesDetail accessory) {
        this.accessories.push(accessory);
    }
}
