public class InfrastructureGoal implements VictoryCondition {
    public boolean checkCondition(Faction f) {
        if (f.checkMaxedInfrastructure()) return true; 
        return false;
    }
}
