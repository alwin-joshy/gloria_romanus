package unsw.gloriaromanus;

public class TreasuryGoal implements VictoryCondition {
    public boolean checkCondition(Faction f) {
        if (f.getTreasury() >= 100000) return true;
        return false; 
    }
}
