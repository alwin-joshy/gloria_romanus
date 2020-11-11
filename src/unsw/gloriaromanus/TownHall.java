package unsw.gloriaromanus;

public class TownHall extends Infrastructure {
    private double publicOrderBonus;

    public TownHall() {
        setBaseCost(45);
        setBaseContructionTime(2);

    }

    public void levelUp() {
        if (getLevel() < 4) {
            publicOrderBonus += 0.03;
        }
        updateCosts();
    }

    public double getBuff() {
        return publicOrderBonus;
    }
    
}
