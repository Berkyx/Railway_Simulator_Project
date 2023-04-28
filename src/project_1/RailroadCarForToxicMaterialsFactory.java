package project_1;

import java.util.Random;

public class RailroadCarForToxicMaterialsFactory implements RailroadCarFactory {
    @Override
    public RailroadCar createRandomRailroadCar() {
        Random random = new Random();
        int maxConcentration = random.nextInt(1000) + 1;
        int maxLoad = random.nextInt(1000) + 1;
        return new RailroadCarForToxicMaterials(maxLoad, maxConcentration);
    }
}
