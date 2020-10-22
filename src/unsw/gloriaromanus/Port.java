package unsw.gloriaromanus;

import unsw.gloriaromanus.BuffStrategy;

public class Port extends WealthGenerationBuilding implements BuffStrategy {
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
