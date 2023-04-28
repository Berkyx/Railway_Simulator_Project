package project_1;

import java.util.Random;

public class PassengerRailroadCar extends RailroadCar implements PowerGridConnection, Loadable {
    private int numSeats;
    private int loadedGoods;

    public int getLoadedGoods() {
        return loadedGoods;
    }

    public int getWeightPerGood() {
        return weightPerGood;
    }

    private int weightPerGood;
    public PassengerRailroadCar(int numSeats) {
        super();
        connectToPowerGrid();
        this.numSeats = numSeats;
        this.weightPerGood = 70;
        this.loadedGoods = 0;
        updateGrossWeight();
    }
    public int getNumSeats() {
        return numSeats;
    }
    @Override
    public void connectToPowerGrid() {
        setConnectedToPowerGrid(true);
    }
    @Override
    public void disconnectFromPowerGrid() {
        setConnectedToPowerGrid(false);
    }
    @Override
    public void load() {
        if (loadedGoods < getMaxCapacity()) {
            loadedGoods++;
            updateGrossWeight();
        }
    }
    @Override
    public void unload() {
        if (loadedGoods > 0) {
            loadedGoods--;
            updateGrossWeight();
        }
    }
    @Override
    public int getMaxCapacity() {
        return numSeats;
    }
    private void updateGrossWeight() {
        setGrossWeight(getNetWeight() + (loadedGoods * weightPerGood));
    }
    protected void setGrossWeight(int grossWeight) {
        super.setGrossWeight(grossWeight);
    }
    @Override
    public String toString() {
        return "PassengerRailroadCar{" +
                "numSeats=" + numSeats +
                '}';
    }
}