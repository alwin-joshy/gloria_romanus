package unsw.gloriaromanus;

public class Port extends WealthGenerationBuilding {
    private int bonus;
    
    public Port() {
        bonus = 10;
        setBaseContructionTime(2);
        setBaseCost(50);
        setName("Port");
        setDescription("Each Port upgraded increases the before-tax rate of the town-wealth for any sea boarding region 10/20/60/240 depending on level.");
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
