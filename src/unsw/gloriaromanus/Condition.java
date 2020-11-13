package unsw.gloriaromanus;

public class Condition implements Goal {
    private VictoryCondition condition;

    public Condition(VictoryCondition condition) {
        this.condition = condition;
    }
    
    public void add(Goal g) {
        System.out.println("Cannot add goals to a condition.");
    }

    public boolean checkVictory(Faction f) {
        return condition.checkCondition(f);
    }

    public String showGoal() {
        return condition.toString() + " ";
    }

    public VictoryCondition getVictoryCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false; 
        Condition c = (Condition) obj;
        return (c.getVictoryCondition().getClass() == condition.getClass());
    }
}
