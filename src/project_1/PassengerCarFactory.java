package project_1;

import java.util.Random;

public class PassengerCarFactory implements RailroadCarFactory {
    @Override
    public RailroadCar createRandomRailroadCar() {
        Random random = new Random();
        int numSeats = random.nextInt(100) + 1;
        return new PassengerRailroadCar(numSeats);
    }
}
