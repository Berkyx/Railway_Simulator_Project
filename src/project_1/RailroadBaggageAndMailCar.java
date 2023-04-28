package project_1;

public class RailroadBaggageAndMailCar extends RailroadCar implements Loadable {
    private int maxCapacity;
    private int loadedGoods;
    private int weightPerGood;
    public RailroadBaggageAndMailCar(int maxCapacity) {
        super();
        this.maxCapacity = maxCapacity;
        this.weightPerGood = 10;
        this.loadedGoods = 0;
        updateGrossWeight();
    }

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
}
