package unsw.gloriaromanus;

public class TownHall extends Infrastructure {
    private double publicOrderBonus;

    public TownHall() {
        setBaseCost(45);
        setBaseContructionTime(2);
        setName("Town Hall");
        setDescription("Each Townhall upgrade increases the province public order by 3/6/9/12% depending on level.");
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
