package unsw.gloriaromanus;

public class Farm extends WealthGenerationBuilding {
    private int farmBonus;

    public Farm() {
        farmBonus = 1;
        setBaseCost(40);
        setBaseContructionTime(2);
    }

    public void levelUp() {
        updateCosts();
        farmBonus++;
    }

    public int getBonus() {
        return farmBonus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Farm f = (Farm) obj;
        return super.equals(obj) && farmBonus == f.getBonus();
    }
    
}
