package unsw.gloriaromanus;

public class Mine extends WealthGenerationBuilding {
    private Faction f;
    private double multiplier;

    public Mine(Faction f) {
        super(f.getMineTurnReduction());
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

    public Faction getFaction() {
        return f;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Mine m = (Mine) obj;
        return super.equals(obj) && f.getName().equals(m.getFaction().getName()) && multiplier == m.getMultiplier();
    }
}
