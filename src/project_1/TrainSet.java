package project_1;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class TrainSet {
    private Locomotive locomotive;
    private ArrayList<RailroadCar> railCars;
    private RailwayStation currentStation;
    private RailwayGraph railwayGraph;
    private boolean isGoingForward;
    private static ConcurrentHashMap<String, Semaphore> activeRoutes = new ConcurrentHashMap<>();
    private final Object printLock;
    private volatile boolean stopped;

    public TrainSet(Locomotive locomotive, RailwayStation startingStation, RailwayGraph railwayGraph, Object printLock) {
        this.locomotive = locomotive;
        this.railCars = new ArrayList<>();
        this.currentStation = startingStation;
        this.railwayGraph = railwayGraph;
        this.isGoingForward = true;
        this.printLock = printLock;
    }
    public void generateReport(String trainsetIdentifier) {
        System.out.println("---- Report for " + trainsetIdentifier + " ----");

        System.out.println("Basic information:");
        System.out.println(this);

        RailwayStation startingStation = locomotive.getSourceStation();
        RailwayStation destinationStation = locomotive.getDestinationStation();
        List<RailwayStation> route = railwayGraph.generateRoute(startingStation, destinationStation);
        List<RailwayStation> completedRoute = railwayGraph.generateRoute(startingStation, currentStation);
        double totalDistance = railwayGraph.calculateTotalDistance(route);
        double completedDistance = railwayGraph.calculateTotalDistance(completedRoute);
        double percentageCompleted = (completedDistance / totalDistance) * 100;
        System.out.println("% of distance completed between starting and destination stations: " + percentageCompleted + "%");

        System.out.println("Railroad cars summary:");
        int totalPassengers = 0;
        for (RailroadCar railCar : railCars) {
            System.out.println(railCar.toString());
            if (railCar instanceof PassengerRailroadCar) {
                PassengerRailroadCar passengerCar = (PassengerRailroadCar) railCar;
                totalPassengers += passengerCar.getLoadedGoods() * passengerCar.getWeightPerGood();
            }
        }
        System.out.println("Total number of people based on the goods transported: " + totalPassengers);

        RailwayStation previousStation = null;
        RailwayStation nextStation = null;
        double segmentCompletedPercentage = 0;
        if (isGoingForward) {
            for (int i = 1; i < route.size(); i++) {
                if (route.get(i).equals(currentStation)) {
                    previousStation = route.get(i - 1);
                    if (i + 1 < route.size()) {
                        nextStation = route.get(i + 1);
                    }
                    break;
                }
            }
        } else {
            Collections.reverse(route);
            for (int i = 1; i < route.size(); i++) {
                if (route.get(i).equals(currentStation)) {
                    previousStation = route.get(i - 1);
                    if (i + 1 < route.size()) {
                        nextStation = route.get(i + 1);
                    }
                    break;
                }
            }
        }
        if (previousStation != null && nextStation != null) {
            double segmentDistance = railwayGraph.calculateDistanceBetweenStations(previousStation, nextStation);
            double completedSegmentDistance = railwayGraph.calculateDistanceBetweenStations(previousStation, currentStation);
            segmentCompletedPercentage = (completedSegmentDistance / segmentDistance) * 100;
        }
        System.out.println("% of distance completed between the nearest railway stations on the route: " + segmentCompletedPercentage + "%");

        System.out.println("-------------------------------------------");
    }
    public void run() throws InterruptedException {
    while (!stopped) {
        if (isGoingForward) {
            List<RailwayStation> route = findRoute(currentStation, locomotive.getDestinationStation());
            System.out.println(locomotive.getName() + " route to destination: " + route);
            for (int i = 1; i < route.size(); i++) {
                RailwayStation nextStation = route.get(i);
                System.out.println(locomotive.getName() + " is about to stop at " + nextStation.getName());
                stopAtStation(nextStation);
                System.out.println(locomotive.getName() + " has left " + nextStation.getName());
                currentStation = nextStation;
                Thread.sleep(2000);
            }
            synchronized (printLock) {
                System.out.println(locomotive.getName() + " waiting at destination station for 30 seconds.");
            }

            Thread.sleep(30000);
            isGoingForward = false;
            route = findRoute(currentStation, locomotive.getHomeStation());
            Collections.reverse(route);
        } else {
            List<RailwayStation> route = findRoute(currentStation, locomotive.getHomeStation());
            System.out.println(locomotive.getName() + " route to home: " + route);
            for (int i = 1; i < route.size(); i++) {
                RailwayStation nextStation = route.get(i);
                System.out.println(locomotive.getName() + " is about to stop at " + nextStation.getName());
                stopAtStation(nextStation);
                System.out.println(locomotive.getName() + " has left " + nextStation.getName());
                currentStation = nextStation;
                Thread.sleep(2000);
            }
            synchronized (printLock) {
                System.out.println(locomotive.getName() + " waiting at starting station for 30 seconds.");
            }
            Thread.sleep(30000);
            isGoingForward = true;
            route = findRoute(currentStation, locomotive.getDestinationStation());
            Collections.reverse(route);
        }
        if (locomotive.isStopped()) {
            stop();
            break;
            }
        }
    }
    public void stopAtStation(RailwayStation station) throws InterruptedException {
        if (!station.equals(currentStation)) {
            String routeKey = currentStation.getName() + "-" + station.getName();
            Semaphore semaphore = activeRoutes.computeIfAbsent(routeKey, k -> new Semaphore(1));
//            System.out.println(locomotive.getName() + " is acquiring semaphore for " + routeKey);
            semaphore.acquire();
//            System.out.println(locomotive.getName() + " has acquired semaphore for " + routeKey);
        }
        System.out.println(locomotive.getName() + " stopping at " + station.getName() + " for 2 seconds.");
        Thread.sleep(2000);
        for (RailroadCar railCar : railCars) {
            if (railCar instanceof Loadable cargo) {
                cargo.unload();
                railCar.setLoaded(false);
                int maxCapacity = cargo.getMaxCapacity();
                int randomLoad = new Random().nextInt(maxCapacity + 1);
                for (int i = 0; i < randomLoad; i++) {
                    cargo.load();
                    railCar.setLoaded(true);
                }
            }
        }
        if (!station.equals(currentStation)) {
            String routeKey = currentStation.getName() + "-" + station.getName();
            Semaphore semaphore = activeRoutes.get(routeKey);
//            System.out.println(locomotive.getName() + " is releasing semaphore for " + routeKey);
            semaphore.release();
//            System.out.println(locomotive.getName() + " has released semaphore for " + routeKey);
            currentStation = station;
        }
    }
    private List<RailwayStation> findRoute(RailwayStation startStation, RailwayStation endStation) {
        return railwayGraph.generateRoute(startStation, endStation);
    }
    public int getNumElectricCars() {
        int numElectricCars = 0;
        for (RailroadCar railCar : railCars) {
            if (railCar instanceof PowerGridConnection) {
                numElectricCars++;
            }
        }
        return numElectricCars;
    }
    public void addRailCar(RailroadCar railCar) {
        if (getTotalWeight() + railCar.getGrossWeight() > locomotive.getMaxWeight()) {
            throw new IllegalArgumentException("For " + locomotive.getName() + " cannot add the specified railroad car, maximum weight limit exceeded.");
        }
        if (railCar instanceof PowerGridConnection && getNumElectricCars() >= locomotive.getMaxElectricCars()) {
            throw new IllegalArgumentException("For " + locomotive.getName() + " cannot add more electric railroad cars, maximum electric cars limit exceeded.");
        }
        railCars.add(railCar);
    }
    public void stop() {
        stopped = true;
        locomotive.stop();
    }
    public void removeRailCar(int index) {
        this.railCars.remove(index);
    }
    public int getNumRailCars() {
        return this.railCars.size();
    }
    public Locomotive getLocomotive() {
        return this.locomotive;
    }
    public ArrayList<RailroadCar> getRailCars() {
        return this.railCars;
    }
    public int getTotalWeight() {
        int totalWeight = 0;
        for (RailroadCar railCar : this.railCars) {
            totalWeight += railCar.getGrossWeight();
        }
        return totalWeight;
    }
    public int getTotalPassengerCapacity() {
        int totalPassengerCapacity = 0;
        for (RailroadCar railCar : this.railCars) {
            if (railCar instanceof PassengerRailroadCar) {
                totalPassengerCapacity -= ((PassengerRailroadCar) railCar).getNumSeats();
            }
        }
        return totalPassengerCapacity;
    }
    public List<RailwayStation> getRoute() {
        List<RailwayStation> route;
        if (isGoingForward) {
            route = railwayGraph.generateRoute(locomotive.getSourceStation(), locomotive.getDestinationStation());
        } else {
            route = railwayGraph.generateRoute(locomotive.getDestinationStation(), locomotive.getSourceStation());
        }
        return route;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TrainSet ID: ").append(locomotive.getId()).append("\n");
        sb.append("Locomotive Name: ").append(locomotive.getName()).append("\n");
        sb.append("Start: ").append(locomotive.getHomeStation()).append("\n");
        sb.append("End: ").append(locomotive.getDestinationStation()).append("\n");

        try {
            int totalDistance = railwayGraph.calculateDistanceBetweenStations(locomotive.getHomeStation(), locomotive.getDestinationStation());
            sb.append("Total Distance: ").append(totalDistance).append("\n");
        } catch (IllegalArgumentException e) {
            sb.append("Stations are not directly connected.\n");
        }

        sb.append("Railroad Cars:\n");
        for (RailroadCar car : railCars) {
            sb.append("\tID: ").append(car.getId()).append(", Weight: ").append(car.getGrossWeight()).append("\n");
        }
        return sb.toString();
    }
}




