package project_1;

import java.util.Random;

public class RailroadBaggageAndMailCarFactory implements RailroadCarFactory {
    @Override
    public RailroadCar createRandomRailroadCar() {
        Random random = new Random();
        int maxCapacity = random.nextInt(100) + 1;
        return new RailroadBaggageAndMailCar(maxCapacity);
    }
}

