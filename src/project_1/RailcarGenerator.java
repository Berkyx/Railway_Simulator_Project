package project_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RailcarGenerator {
    private List<RailroadCarFactory> factories;
    public RailcarGenerator() {
        factories = new ArrayList<>();
        factories.add(new PassengerCarFactory());
        factories.add(new RailroadBaggageAndMailCarFactory());
        factories.add(new RailroadCarForExplosivesFactory());
        factories.add(new RailroadCarForGaseousMaterialsFactory());
        factories.add(new RailroadCarForLiquidMaterialsFactory());
        factories.add(new RailroadCarForLiquidToxicMaterialFactory());
        factories.add(new RailroadCarForToxicMaterialsFactory());
        factories.add(new RailroadPostOfficeFactory());
        factories.add(new RailroadRestaurantCarFactory());
        factories.add(new RefrigeratedRailroadCarFactory());
    }
    public List<RailroadCar> generateRandomRailCars(int numRailCars, int maxWeight) {
        List<RailroadCar> railCars = new ArrayList<>();
        Random random = new Random();
        int currentTotalWeight = 0;
        for (int i = 0; i < numRailCars; i++) {
            int factoryIndex = random.nextInt(factories.size());
            RailroadCarFactory factory = factories.get(factoryIndex);
            RailroadCar railCar = factory.createRandomRailroadCar();

            if (currentTotalWeight + railCar.getGrossWeight() <= maxWeight) {
                railCars.add(railCar);
                currentTotalWeight += railCar.getGrossWeight();
            } else {
                break;
            }
        }
        return railCars;
    }
    public List<RailroadCarFactory> getFactories() {
        return factories;
    }
}