package project_1;

public class RefrigeratedRailroadCar extends BasicRailroadFreightCar implements PowerGridConnection, Loadable {
    private int temperature;
    private int maxCapacity;
    private int loadedGoods;
    private int weightPerGood;
    public RefrigeratedRailroadCar(int weightPerGood, int maxCapacity) {
        super();
        connectToPowerGrid();
        this.temperature = -20;
        this.maxCapacity = maxCapacity;
        this.weightPerGood = weightPerGood;
        this.loadedGoods = 0;
        updateGrossWeight();
    }
    public int getTemperature() {
        return temperature;
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
    private void updateGrossWeight() {
        setGrossWeight(getNetWeight() + (loadedGoods * weightPerGood));
    }
    protected void setGrossWeight(int grossWeight) {
        super.setGrossWeight(grossWeight);
    }
    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public int getLoadedGoods() {
        return loadedGoods;
    }

    @Override
    public int getWeightPerGood() {
        return weightPerGood;
    }
}
