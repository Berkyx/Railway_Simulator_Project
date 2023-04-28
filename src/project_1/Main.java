package project_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main {
    private String Prompt;
    private Scanner input;
    RailwayGraph railwayGraph;
    RailwayStation station;
    private List<TrainSet> trainSets;
    Object printLock;
    public Main() {
        input = new Scanner(System.in);
        railwayGraph = new RailwayGraph();
        trainSets = new ArrayList<>();
        printLock = new Object();
        loadStationsAndRoutesFromFile("stations.txt");
    }
    public void Start () {
        Display();
        Prompt = "Simulator: ";
        label:
        while(true) {
            System.out.println(Prompt);
            String command = input.nextLine();
            switch (command) {
                case "1":
                    generateRandomTrainSets(25);
                    break;
                case "2": {
                    String[] railCars = {"Passenger Car",
                            "Restaurant Car",
                            "Post Office Car",
                            "Baggage and Mail Car",
                            "Refrigerator Car",
                            "Car for Explosives",
                            "Car for Toxic Materials",
                            "Car for Gaseous Materials",
                            "Car for Liquid Materials",
                            "Car for Liquid Toxic Materials"};
                    boolean flag = true;
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Creating a new train set...");
                    System.out.println("Please select a home station: ");
                    List<RailwayStation> allStations = new ArrayList<>(railwayGraph.getStations());
                    for (int i = 0; i < allStations.size(); i++) {
                        System.out.println((i + 1) + ". " + allStations.get(i).getName());
                    }
                    int homeStationIndex = input.nextInt();
                    RailwayStation homeStation = allStations.get(homeStationIndex - 1);
                    System.out.println("Please select a source station: ");
                    for (int i = 0; i < allStations.size(); i++) {
                        System.out.println((i + 1) + ". " + allStations.get(i).getName());
                    }
                    int sourceStationIndex = input.nextInt();
                    RailwayStation sourceStation = allStations.get(sourceStationIndex - 1);
                    System.out.println("Please select a destination station: ");
                    for (int i = 0; i < allStations.size(); i++) {
                        System.out.println((i + 1) + ". " + allStations.get(i).getName());
                    }
                    int destinationStationIndex = input.nextInt();
                    RailwayStation destinationStation = allStations.get(destinationStationIndex - 1);
                    System.out.println("Please write a name for locomotive: ");
                    input.nextLine();
                    String locomotiveName = input.nextLine();
                    System.out.println("Please enter a number for maximum weight it can pull: ");
                    int maxWeight = input.nextInt();
                    System.out.println("Please enter a number for maximum electric cars it can pull: ");
                    int maxElectricCars = input.nextInt();
                    input.nextLine();
                    Locomotive locomotive = new Locomotive(locomotiveName, homeStation, sourceStation, destinationStation, maxWeight, maxElectricCars);
                    TrainSet trainSet = new TrainSet(locomotive, sourceStation, railwayGraph, printLock);
                    trainSets.add(trainSet);
                    System.out.println("We will add cars to your train set now. Please enter 0 when you are done.");
                    while (flag) {
                        for (int i = 0; i < railCars.length; i++) {
                            System.out.println((i + 1) + ". " + railCars[i]);
                        }
                        int carSelection = input.nextInt();
                        if (carSelection == 0) {
                            flag = false;
                        } else if (carSelection >= 1 && carSelection <= railCars.length) {
                            RailcarGenerator railcarGenerator = new RailcarGenerator();
                            RailroadCar randomCar = railcarGenerator.getFactories().get(carSelection - 1).createRandomRailroadCar();
                            trainSet.addRailCar(randomCar);
                            System.out.println(railCars[carSelection - 1] + " is created successfully.");
                            System.out.println("Please enter 0 if you are done adding cars.");
                        } else {
                            System.out.println("Invalid command.");
                        }
                    }
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Your train set is created successfully.");
                    Display();
                    break;
                }
                case "3":
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Creating a new station...");
                    System.out.println("Please enter the name of the new station:");
                    String stationName = input.nextLine();
                    createStation(stationName);
                    System.out.println("New station " + stationName + " created successfully.");
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Your station is created successfully.");
                    Display();
                    break;
                case "4": {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Creating a new route...");
                    System.out.println("Please select 2 station from the list below: ");
                    List<RailwayStation> allStations = new ArrayList<>(railwayGraph.getStations());
                    for (int i = 0; i < allStations.size(); i++) {
                        System.out.println((i + 1) + ". " + allStations.get(i).getName());
                    }
                    int station_1Index = input.nextInt();
                    RailwayStation station_1 = allStations.get(station_1Index - 1);
                    int station_2Index = input.nextInt();
                    RailwayStation station_2 = allStations.get(station_2Index - 1);
                    System.out.println("Please enter the distance between " + station_1.getName() + " and " + station_2.getName() + ": ");
                    int distance = input.nextInt();
                    railwayGraph.addEdge(station_1, station_2, distance);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Your route is created successfully.");
                    Display();
                    break;
                }
                case "5":
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Running the simulation...");
                    List<Thread> trainSetThreads = new ArrayList<>();
                    List<Thread> reportThreads = new ArrayList<>();
                    for (TrainSet trainSet : trainSets) {
                        Thread trainSetThread = new Thread(() -> {
                            try {
                                trainSet.run();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        trainSetThreads.add(trainSetThread);
                        trainSetThread.start();
                        Thread reportThread = new Thread(() -> {
                            while (!trainSetThread.isInterrupted()) {
                                try {
                                    trainSet.generateReport("Trainset - " + trainSet.getLocomotive().getName());
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    break;
                                }
                            }
                        });
                        reportThreads.add(reportThread);
                        reportThread.start();
                    }
                    for (Thread trainSetThread : trainSetThreads) {
                        try {
                            trainSetThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (Thread reportThread : reportThreads) {
                        reportThread.interrupt();
                        try {
                            reportThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "6":
                    System.out.println("Listing...");
                    System.out.println("Train Sets: ");
                    for (TrainSet trainSet : trainSets) {
                        System.out.println(trainSet);
                    }
                    System.out.println("Stations: ");
                    for (RailwayStation station : railwayGraph.getStations()) {
                        System.out.println(station);
                    }
                    break;
                case "7":
                    System.out.println("Exiting...");
                    break label;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }
    }
    public void Display () {
        System.out.println("Welcome to the Train Simulator!");
        System.out.println("Please select an option:");
        System.out.println("1. Generate 25 random train sets");
        System.out.println("2. Create a new train set");
        System.out.println("3. Create a new station");
        System.out.println("4. Create a new route");
        System.out.println("5. Run a train");
        System.out.println("6. List Everything");
        System.out.println("7. Exit");
    }
    private void createStation(String stationName) {
        RailwayStation newStation = new RailwayStation(stationName);
        railwayGraph.addStation(newStation);
        System.out.println("Do you want to connect the new station to existing stations? (y/n)");
        String choice = input.nextLine();
        List<String> connections = new ArrayList<>();
        while (choice.equalsIgnoreCase("y")) {
            System.out.println("Enter the name of the station to connect to:");
            String existingStationName = input.nextLine();
            RailwayStation existingStation = RailwayStation.getStationByName(existingStationName);
            if (existingStation != null) {
                System.out.println("Enter the distance between the two stations:");
                int distance = input.nextInt();
                input.nextLine();
                railwayGraph.addEdge(newStation, existingStation, distance);
                connections.add(newStation.getName() + " " + existingStation.getName() + " " + distance);
                System.out.println("Connection added successfully.");
            } else {
                System.out.println("Station not found.");
            }
            System.out.println("Do you want to connect the new station to another existing station? (y/n)");
            choice = input.nextLine();
        }
        try {
            for (String connection : connections) {
                Files.write(Paths.get("stations.txt"), (connection + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("Error while updating stations.txt.");
            e.printStackTrace();
        }
    }
    private void loadStationsAndRoutesFromFile(String filename) {
        Map<String, RailwayStation> stationMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String stationAName = parts[0];
                String stationBName = parts[1];
                int distance = Integer.parseInt(parts[2]);
                RailwayStation stationA = stationMap.computeIfAbsent(stationAName, name -> new RailwayStation(name));
                RailwayStation stationB = stationMap.computeIfAbsent(stationBName, name -> new RailwayStation(name));
                railwayGraph.addEdge(stationA, stationB, distance);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void generateRandomTrainSets(int numberOfTrainSets) {
        Random random = new Random();
        List<RailwayStation> allStations = new ArrayList<>(railwayGraph.getStations());
        RailcarGenerator railcarGenerator = new RailcarGenerator();
        for (int i = 0; i < numberOfTrainSets; i++) {
            RailwayStation homeStation = allStations.get(random.nextInt(allStations.size()));
            RailwayStation sourceStation = allStations.get(random.nextInt(allStations.size()));
            RailwayStation destinationStation = allStations.get(random.nextInt(allStations.size()));
            String locomotiveName = "Locomotive-" + (i + 1);
            int maxWeight = random.nextInt(1000000) + 100000;
            int maxElectricCars = random.nextInt(25) + 5;
            Locomotive locomotive = new Locomotive(locomotiveName, homeStation, sourceStation, destinationStation, maxWeight, maxElectricCars);
            TrainSet trainSet = new TrainSet(locomotive, sourceStation, railwayGraph, printLock);
            trainSets.add(trainSet);
            int numberOfCars = random.nextInt(10) + 5;
            for (int j = 0; j < numberOfCars; j++) {
                boolean added = false;
                int attempts = 0;
                final int maxAttempts = 10;
                while (!added && attempts < maxAttempts) {
                    int carSelection = random.nextInt(railcarGenerator.getFactories().size());
                    RailroadCar randomCar = railcarGenerator.getFactories().get(carSelection).createRandomRailroadCar();
                    try {
                        trainSet.addRailCar(randomCar);
                        added = true;
                    } catch (IllegalArgumentException e) {
                        attempts++;
                        System.out.println("Attempt " + attempts + ": Failed to add railcar. Reason: " + e.getMessage());
                    }
                }
                if (attempts >= maxAttempts) {
                    System.out.println("Failed to add a railcar after " + maxAttempts + " attempts.");
                }
            }
        }
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.Start();
    }
}