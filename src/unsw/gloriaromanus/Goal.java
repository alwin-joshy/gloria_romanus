package unsw.gloriaromanus;

import java.io.Serializable;

public interface Goal extends Serializable {
    public boolean checkVictory(Faction f);
    public void add(Goal g);
    public void showGoal();
}
