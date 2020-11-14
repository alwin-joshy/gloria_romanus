package unsw.gloriaromanus;

public class Walls extends Infrastructure {
    WallLevel currentLevel;

    // Walls double the melee defence of all troops defending a settlement (except when fighting artillery), 
    // reduce the missile attack damage of 
    // attacking archers by 50% (to a minimum of 1), and reduce the missile attack damage of attacking horse archers to 1.
    
    public Walls(){
        setBaseContructionTime(2);
        setBaseCost(120);
        setName("Walls");
        setDescription("Each wall upgrade will provide a buffer to depending troop and can be upgraded into; \nLevel 1 : Walls  \nLevel 2: TwoArcherTowers \nLevel 3: OneBallistaTower + TwoArcherTowers \nLevel 4: ThreeBallistaTowers.");
        currentLevel = null;
    }


    public void setState(WallLevel level) {
        currentLevel = level;
    }

    public void levelUpTowers(Province p) {
        currentLevel.addTowers(p);
    }

    public void levelUp() {
        updateCosts();
        if (currentLevel == null) {
            setState(new WallLevelOne());
        } else {
            currentLevel.goNext(this);
        }

        // lvl2 adds 2 x archer tower
        // lvl3 adds 1 x ballista towers to province list of units
        // lvl4 replaces remaining archer towers with ballista towers

        // create json for these
        // should these be cumulative?
    }
}
