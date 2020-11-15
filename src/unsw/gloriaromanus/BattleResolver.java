package unsw.gloriaromanus;

import java.util.ArrayList;

public interface BattleResolver {
    public boolean battle(Province attacking, ArrayList<Unit> attackingArmy, Province defending, ArrayList<Unit> defendingArmy);
    public ArrayList<BattleObserver> getBattleObservers();
    public void notifyBattleObservers(Faction f);
    public BuildingObserver getBuildingObserver();
    public void setSeed(int seed); 
    public void setEngagementObserver(EngagementObserver engagementObserver);
    public void addDefeatObserver(DefeatObserver defObserver);
}
