package unsw.gloriaromanus;

public abstract class WealthGenerationBuilding extends Infrastructure {
    public WealthGenerationBuilding(int factionBonus) {
        super(factionBonus);
    }

    int wealthIncrease;
    int wealthgrowthIncrease;
}