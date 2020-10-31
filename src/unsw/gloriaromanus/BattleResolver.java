package unsw.gloriaromanus;

import java.util.ArrayList;

public interface BattleResolver {
    public boolean battle(Province attacking, ArrayList<Unit> attackingArmy, Province defending, ArrayList<Unit> defendingArmy);
    public ArrayList<BattleObserver> getObservers();
    public void notify(Faction f);
}
