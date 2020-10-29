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

    public void showGoal() {
        System.out.print(condition + " ");
    }
}
