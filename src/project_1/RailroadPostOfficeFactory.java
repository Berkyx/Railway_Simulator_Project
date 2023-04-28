package project_1;

import java.util.Random;

public class RailroadPostOfficeFactory implements RailroadCarFactory {
    @Override
    public RailroadCar createRandomRailroadCar() {
        Random random = new Random();
        boolean secure = random.nextBoolean();
        return new RailroadPostOffice(secure);
    }
}
