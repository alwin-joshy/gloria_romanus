package unsw.gloriaromanus;

public class ConquestGoal implements VictoryCondition {
    public boolean checkCondition(Faction f) {
        if (f.getNumProvinces() == 52) return true;
        return false; 
    }
}
