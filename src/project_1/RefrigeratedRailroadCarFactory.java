package project_1;

import java.util.Random;

public class RefrigeratedRailroadCarFactory implements RailroadCarFactory {
    @Override
    public RailroadCar createRandomRailroadCar() {
        Random random = new Random();
        int weightPerGood = random.nextInt(300) + 1;
        int maxCapacity = random.nextInt(1000) + 1;
        return new RefrigeratedRailroadCar(weightPerGood, maxCapacity);
    }
}
