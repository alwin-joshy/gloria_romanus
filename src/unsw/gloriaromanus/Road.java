package unsw.gloriaromanus;

public class Road extends Infrastructure {
    private Province p;

    public Road(Province p) {
        this.p = p;
        setBaseCost(85);
        setBaseContructionTime(3);
    }

    public void levelUp() {
        updateCosts();
        Game.updateAdjacentProvinces(p);
    }
}
