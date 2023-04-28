package project_1;
public abstract class RailroadCar {
    private int id;
    private static int uid = 0;
    private int netWeight;
    private int grossWeight;
    private boolean connectedToPowerGrid;
    private boolean loaded;

    public RailroadCar() {
        this.id = uid++;
        this.netWeight = 30000;
        this.grossWeight = netWeight;
        this.connectedToPowerGrid = false;
        this.loaded = false;
    }
    public int getId() {
        return id;
    }
    public int getNetWeight() {
        return netWeight;
    }
    public int getGrossWeight() {
        return grossWeight;
    }
    public boolean isConnectedToPowerGrid() {
        return connectedToPowerGrid;
    }
    public void setConnectedToPowerGrid(boolean connectedToPowerGrid) {
        this.connectedToPowerGrid = connectedToPowerGrid;
    }
    public boolean isLoaded() {
        return loaded;
    }
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    protected void setGrossWeight(int grossWeight) {
        this.grossWeight = grossWeight;
    }
    @Override
    public String toString() {
        return "RailroadCar{" +
                "id=" + id +
                ", netWeight=" + netWeight +
                ", grossWeight=" + grossWeight +
                ", connectedToPowerGrid=" + connectedToPowerGrid +
                ", loaded=" + loaded +
                '}';
    }
}

