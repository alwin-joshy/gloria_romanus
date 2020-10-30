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

    public Faction getFaction() {
        return f;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Port p = (Port) obj;
        return super.equals(obj) && f.getName().equals(p.getFaction().getName()) && bonus == p.getBonus();
    }

}
