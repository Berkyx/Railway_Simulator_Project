package project_1;

public interface Loadable {
    void load();
    void unload();
    int getMaxCapacity();
    int getLoadedGoods();
    int getWeightPerGood();
}
