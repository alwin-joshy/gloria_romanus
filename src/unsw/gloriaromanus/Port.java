package unsw.gloriaromanus;

public class Port extends WealthGenerationBuilding {
    private int bonus;
    
    public Port() {
        bonus = 10;
        setBaseContructionTime(2);
        setBaseCost(50);
        setName("Port");
    }

    public void levelUp() {
        updateCosts();
        bonus *= getLevel();
    }

    public int getBonus() {
        return bonus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Port p = (Port) obj;
        return super.equals(obj) && bonus == p.getBonus();
    }

}
