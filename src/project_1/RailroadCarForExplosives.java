package project_1;

public class RailroadCarForExplosives extends HeavyRailroadFreightCar implements Loadable{
    private boolean shockproof;
    private int loadedGoods;
    private int weightPerGood;
    public RailroadCarForExplosives(int maxLoad, boolean shockproof) {
        super(maxLoad);
        this.shockproof = shockproof;
        this.weightPerGood = 100;
        this.loadedGoods = 0;
        updateGrossWeight();
    }
    public boolean isShockproof() {
        return shockproof;
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
        return getMaxLoad();
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
    public String toString() {
        return "RailroadCarForExplosives{" +
                "shockproof=" + shockproof +
                '}';
    }
}
