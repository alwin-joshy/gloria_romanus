package unsw.gloriaromanus;

public class Walls extends Infrastructure {

    // Walls double the melee defence of all troops defending a settlement (except when fighting artillery), 
    // reduce the missile attack damage of 
    // attacking archers by 50% (to a minimum of 1), and reduce the missile attack damage of attacking horse archers to 1.
    
    public Walls(){
    }


    public void levelUp() {
        updateCosts();
        // lvl2 adds archer tower
        // lvl3 adds ballista towers to province list of units
        // create json for these
        // should these be cumulative?
    }
}
