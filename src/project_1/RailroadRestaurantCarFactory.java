package project_1;

import java.util.Random;

public class RailroadRestaurantCarFactory implements RailroadCarFactory {
    @Override
    public RailroadCar createRandomRailroadCar() {
        Random random = new Random();
        int numTables = random.nextInt(10) + 1;
        return new RailroadRestaurantCar(numTables);
    }
}
