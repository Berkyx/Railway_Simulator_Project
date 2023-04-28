package project_1;

import java.util.Random;

public class RailroadCarForLiquidToxicMaterialFactory implements RailroadCarFactory {
    @Override
    public RailroadCar createRandomRailroadCar() {
        Random random = new Random();
        int maxVolume = random.nextInt(1000) + 1;
        int maxConcentration = random.nextInt(1000) + 1;
        return new RailroadCarForLiquidToxicMaterial(maxVolume, maxConcentration);
    }
}
