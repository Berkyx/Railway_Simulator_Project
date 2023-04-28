package project_1;

import java.util.ArrayList;
import java.util.List;

public class RailwayStation {
    private String name;
    private List<RailroadCar> cars;
    private List<Locomotive> locomotives;
    private static List<RailwayStation> allStations = new ArrayList<>();
    public RailwayStation(String name) {
        this.name = name;
        this.cars = new ArrayList<>();
        this.locomotives = new ArrayList<>();
        allStations.add(this);
    }
    public static List<RailwayStation> getAllStations() {
        return allStations;
    }

    public static RailwayStation getStationByName(String existingStationName) {
        for (RailwayStation station : allStations) {
            if (station.getName().equals(existingStationName)) {
                return station;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
    public List<RailroadCar> getCars() {
        return cars;
    }
    public List<Locomotive> getLocomotives() {
        return locomotives;
    }
    public void addCar(RailroadCar car) {
        cars.add(car);
    }
    public void removeCar(RailroadCar car) {
        cars.remove(car);
    }
    public void addLocomotive(Locomotive locomotive) {
        locomotives.add(locomotive);
    }
    public void removeLocomotive(Locomotive locomotive) {
        locomotives.remove(locomotive);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RailwayStation that = (RailwayStation) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
    @Override
    public String toString() {
        return "RailwayStation{" +
                "name='" + name + '\'' +
                '}';
    }
}
