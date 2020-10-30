package unsw.gloriaromanus;

public class Market extends WealthGenerationBuilding {
    private Faction f;
    private double multiplier;

    public Market (Faction f) {
        this.f = f;
        multiplier = 0.99;
        setBaseCost(40);
        setBaseContructionTime(2);
    }

    public void levelUp() {
        updateCosts();
        multiplier -= 0.01;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Market m = (Market) obj;
        return super.equals(obj) && f.getName().equals(m.getFaction().getName()) && multiplier == m.getMultiplier();
    }

}
