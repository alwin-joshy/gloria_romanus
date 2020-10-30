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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Farm f = (Farm) obj;
        return super.equals(obj) && p.getName().equals(f.getFaction().getName()) && farmBonus == f.getBonus();
    }
    
}
