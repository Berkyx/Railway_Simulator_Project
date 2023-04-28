package project_1;

public class RailroadCarForToxicMaterials extends HeavyRailroadFreightCar implements Loadable{
    private int maxConcentration;
    private int loadedGoods;
    private int weightPerGood;
    public RailroadCarForToxicMaterials(int maxLoad, int maxConcentration) {
        super(maxLoad);
        this.maxConcentration = maxConcentration;
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
        return maxConcentration;
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
