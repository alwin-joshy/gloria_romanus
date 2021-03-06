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
            System.out.println("AND:");
        else
            System.out.println("OR:");
        System.out.println(" ( ");
        for (Goal g : conditions) {
            g.showGoal();
        }
        System.out.println("\n) ");
    }

    public Goal getNthGoal(int n) {
        return conditions.get(n);    
    }

    public boolean isAnd() {
        return isAnd;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false; 
        Subgoal s = (Subgoal) obj;
        for (int i = 0 ; i < conditions.size() ; i++) {
            if (! conditions.get(i).equals(s.getNthGoal(i))) return false; 
        }
        return isAnd == s.isAnd();
    }
}
