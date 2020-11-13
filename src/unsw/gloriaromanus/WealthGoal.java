package unsw.gloriaromanus;

public class WealthGoal implements VictoryCondition {
    public boolean checkCondition(Faction f) {
        if (f.getWealth() >= 400000) return true;
        return false;
    }
    
    @Override
    public String toString() {
        return "WEALTH";
    }
}
