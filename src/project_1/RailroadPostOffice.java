package project_1;
public class RailroadPostOffice extends RailroadCar implements PowerGridConnection {
    private boolean secure;
    public RailroadPostOffice(boolean secure) {
        super();
        connectToPowerGrid();
        this.secure = secure;
        }
    public boolean isSecure() {
            return secure;
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

