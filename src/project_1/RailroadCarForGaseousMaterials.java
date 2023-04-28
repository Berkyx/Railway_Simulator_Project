package project_1;

public class RailroadCarForGaseousMaterials extends BasicRailroadFreightCar implements Loadable{
    private int maxPressure;
    private int loadedGoods;
    private int weightPerGood;
    public RailroadCarForGaseousMaterials(int maxPressure) {
        super();
        this.maxPressure = maxPressure;
        this.weightPerGood = 1;
        this.loadedGoods = 0;
        updateGrossWeight();
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
        return maxPressure;
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
