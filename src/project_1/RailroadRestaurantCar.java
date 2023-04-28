package project_1;

public class RailroadRestaurantCar extends RailroadCar implements PowerGridConnection {
    private int numTables;
    public RailroadRestaurantCar(int numTables) {
        super();
        connectToPowerGrid();
        this.numTables = numTables;
    }
    public int getNumTables() {
        return numTables;
    }
    @Override
    public void connectToPowerGrid() {
        setConnectedToPowerGrid(true);
    }
    @Override
    public void disconnectFromPowerGrid() {
        setConnectedToPowerGrid(false);
    }
}
