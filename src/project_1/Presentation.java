package project_1;

import java.util.ArrayList;
import java.util.List;

public class Presentation {
    public static void main(String[] args) {
        RailwayStation station1 = new RailwayStation("Station-1");
        RailwayStation station2 = new RailwayStation("Station-2");
        RailwayStation station3 = new RailwayStation("Station-3");
        RailwayStation station4 = new RailwayStation("Station-4");
        RailwayGraph graph = new RailwayGraph();
        List<TrainSet> trainSets = new ArrayList<>();
        Object printLock = new Object();
        graph.addEdge(station1, station2, 10);
        graph.addEdge(station1, station3, 15);
        graph.addEdge(station2, station3, 10);
        graph.addEdge(station2, station4, 12);
        graph.addEdge(station3, station4, 10);
        System.out.println("Stations: ");
        for (RailwayStation station : graph.getStations()) {
            System.out.println(station);
        }
        Locomotive locomotive = new Locomotive("Thomas-the-Engine", station1, station2, station4, 100000, 10);
        Locomotive locomotive2 = new Locomotive("Percy-the-Engine", station1, station2, station4, 100000, 10);
        TrainSet trainSett = new TrainSet(locomotive, station1, graph, printLock);
        TrainSet trainSett2 = new TrainSet(locomotive2, station1, graph, printLock);
        trainSets.add(trainSett);
        trainSets.add(trainSett2);
        PassengerRailroadCar passengerRailroadCar = new PassengerRailroadCar(20);
        PassengerRailroadCar passengerRailroadCar2 = new PassengerRailroadCar(20);
        passengerRailroadCar.load();
        passengerRailroadCar.load();
        passengerRailroadCar.load();
        passengerRailroadCar.load();
        passengerRailroadCar.unload();
        passengerRailroadCar2.load();
        passengerRailroadCar2.load();
        trainSett.addRailCar(passengerRailroadCar);
        trainSett.addRailCar(passengerRailroadCar2);
        System.out.println("Train Sets: ");
        for (TrainSet trainSet : trainSets) {
            System.out.println(trainSet);
        }
        List<Thread> trainSetThreads = new ArrayList<>();
        List<Thread> reportThreads = new ArrayList<>();
        for (TrainSet trainSet : trainSets) {
            Thread trainSetThread = new Thread(() -> {
                try {
                    trainSet.getLocomotive().setCurrentSpeed(202);
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
    }
}
