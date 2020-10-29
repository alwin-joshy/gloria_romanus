package unsw.gloriaromanus;

import java.util.ArrayList;

public class Subgoal implements Goal {
    private ArrayList<Goal> conditions;
    private boolean isAnd;

    public Subgoal(boolean isAnd) {
        conditions = new ArrayList<Goal>();
        this.isAnd = isAnd;
    }

    public void add(Goal g) {
        conditions.add(g);
    }

    public boolean checkVictory(Faction f) {
        for (Goal g : conditions) {
            if (g.checkVictory(f) && !isAnd)
                return true;
            else if (!g.checkVictory(f) && isAnd)
                return false;
        }
        if (isAnd)
            return true;
        else
            return false;
    }

    public void showGoal() {
        if (isAnd)
            System.out.print("AND");
        else
            System.out.print("OR");
        System.out.print(" ( ");
        for (Goal g : conditions) {
            g.showGoal();
        }
        System.out.print(") ");
    }
}
