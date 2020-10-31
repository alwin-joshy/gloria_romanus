package unsw.gloriaromanus;

public class ConquestGoal implements VictoryCondition {
    private int totalProvinces;

    public ConquestGoal(int totalProvinces) {
        this.totalProvinces = totalProvinces;
    }
    
    public boolean checkCondition(Faction f) {
        if (f.getNumProvinces() == totalProvinces) return true;
        return false; 
    }
}
