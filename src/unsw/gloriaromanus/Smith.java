package unsw.gloriaromanus;

public class Smith extends Infrastructure{
    public SmithLevel smithLevel;
    
    public Smith() {
        setBaseContructionTime(2);
        setBaseCost(60);
        setName("Smith");
        smithLevel = new SmithLevelZero();
        setDescription("Each Smith upgrade will give in-battle buff to troops depending on level;\n \nLevel 1: Upgraded Helmets \nLevel 2: Upgraded Armour Suits \nLevel 3: Upgraded Weapons \nLevel 4: Fire Arrows for Archers ");
    }

    public void levelUp() {
        updateCosts();
        smithLevel.nextLevel(this);
    }

    public void setSmithLevel(SmithLevel s) {
        this.smithLevel = s;
        System.out.println(smithLevel.getLevel());
    }

    public SmithLevel getSmithLevel() {
        return smithLevel;
    }
}
