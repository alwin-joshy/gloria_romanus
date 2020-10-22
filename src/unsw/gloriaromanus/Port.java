package unsw.gloriaromanus;

public class Port extends WealthGenerationBuilding {
    private Faction f;
    private int bonus;
    
    public Port(Faction f) {
        this.f = f;
        bonus = 10;
        setBaseContructionTime(2);
        setBaseCost(50);
    }

    public void levelUp() {
        updateCosts();
        bonus *= getLevel();
    }

    public int getBonus() {
        return bonus;
    }


}
