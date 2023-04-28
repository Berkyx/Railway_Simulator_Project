package project_1;

public class RailroadCarForLiquidToxicMaterial extends RailroadCarForLiquidMaterials implements Loadable {
    private int maxConcentration;
    private int loadedGoods;
    private int weightPerGood;
    public RailroadCarForLiquidToxicMaterial(int maxVolume, int maxConcentration) {
        super(maxVolume);
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
}
