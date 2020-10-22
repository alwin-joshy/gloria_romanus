package unsw.gloriaromanus;

public class Farm extends WealthGenerationBuilding {
    private Province p;
    private int farmBonus;

    public Farm(Province p) {
        this.p = p;
        farmBonus = 1;
    }

    public void levelUp() {
        updateCosts();
        farmBonus++;
    }

    public int getBonus() {
        return farmBonus;
    }
    
}
