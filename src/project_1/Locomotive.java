package project_1;

import java.util.concurrent.atomic.AtomicInteger;

public class Locomotive {
    private int id;
    private static int uid = 0;
    private final String name;
    private final RailwayStation homeStation;
    private final RailwayStation sourceStation;
    private final RailwayStation destinationStation;
    private final int maxWeight;
    private final int maxElectricCars;
    private final AtomicInteger currentSpeed;
    private volatile boolean stopped;
    public Locomotive(String name, RailwayStation homeStation, RailwayStation sourceStation,
                      RailwayStation destinationStation, int maxWeight, int maxElectricCars) {
        this.id = uid++;
        this.name = name;
        this.homeStation = homeStation;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.maxWeight = maxWeight;
        this.maxElectricCars = maxElectricCars;
        this.currentSpeed = new AtomicInteger(0);
        this.stopped = false;
        Thread speedUpdateThread = new Thread(new SpeedUpdater());
        speedUpdateThread.start();
    }
    public String getName() {
        return name;
    }
    public int getId() {return id;}
    public RailwayStation getHomeStation() {
        return homeStation;
    }
    public RailwayStation getSourceStation() {
        return sourceStation;
    }
    public RailwayStation getDestinationStation() {
        return destinationStation;
    }
    public int getMaxWeight() {
        return maxWeight;
    }
    public int getMaxElectricCars() {
        return maxElectricCars;
    }
    public int getCurrentSpeed() {
        return currentSpeed.get();
    }
    public void stop() {
        stopped = true;
    }
    public boolean isStopped() {
        return stopped;
    }
    public void setCurrentSpeed(int speed) {
        if (speed < 0) {
            currentSpeed.set(0);
        }
        else {
            currentSpeed.set(speed);
        }
    }
    private class SpeedUpdater implements Runnable {
        static class RailroadHazard extends RuntimeException {
            public RailroadHazard(String message) {
                super(message);
            }
        }
        @Override
        public void run() {
            try {
            while (!stopped) {
                double factor = Math.random() * 0.06 - 0.03;
                int speed = currentSpeed.get();
                setCurrentSpeed((int) (speed + speed * factor));
                if (speed > 200) {
                    throw new RailroadHazard("Speed too high!");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            } catch (RailroadHazard e) {
                System.err.println("Locomotive " + name + " has encountered a railroad hazard: " + e.getMessage());
                stop();
            }
        }
    }

    @Override
    public String toString() {
        return "Locomotive{" +
                "name='" + name + '\'' +
                ", homeStation=" + homeStation +
                ", sourceStation=" + sourceStation +
                ", destinationStation=" + destinationStation +
                ", maxWeight=" + maxWeight +
                ", maxElectricCars=" + maxElectricCars +
                ", currentSpeed=" + currentSpeed +
                ", stopped=" + stopped +
                '}';
    }
}


