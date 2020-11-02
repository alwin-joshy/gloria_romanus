package unsw.gloriaromanus;

import java.util.ArrayList;

public interface BattleResolver {
    public boolean battle(Province attacking, ArrayList<Unit> attackingArmy, Province defending, ArrayList<Unit> defendingArmy);
    private ArrayList<BattleObserver> getBattleObservers();
    private void notifyBattleObservers(Faction f);
    private BuildingObserver getBuildingObserver();
    private void setSeed(int seed); 
}
