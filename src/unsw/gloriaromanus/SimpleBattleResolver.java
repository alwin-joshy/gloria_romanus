package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class SimpleBattleResolver implements BattleResolver, Serializable {
    int attackingSum;
    int defendingSum;
    int totalInvadingTroops;
    int totalDefendingTroops;

    public boolean battle(Province invader, ArrayList<Unit> invArmy, Province invaded, ArrayList<Unit> defArmy) {
        totalInvadingTroops = 0;
        totalDefendingTroops = 0;
        attackingSum = calculateStrength(invArmy, true);
        defendingSum = calculateStrength(defArmy, false);
        double invadeSuccess = attackingSum/(attackingSum + defendingSum);
        double invadeFail = defendingSum/(attackingSum + defendingSum);
        Random r = new Random();
        double rNum = r.nextDouble();
        if (rNum <= invadeSuccess) {
            invaded.destroyAllUnits();
            invader.destroyUnits(invArmy, (int) ((r.nextDouble() % invadeFail) * totalInvadingTroops));
            transferProvinceOwnership(invader.getFaction(), invaded.getFaction(), invaded);
            // All of enemy army is destroyed
            // Proportion of ally army is destroyed
            // Remaining ally army is moved into invaded province
            // Invaded province joins invading faction
            return true;
        } else {
            invaded.destroyUnits(defArmy, (int) ((r.nextDouble() % invadeSuccess) * totalDefendingTroops));
            invaded.destroyUnits(invArmy, (int) ((r.nextDouble() % invadeFail) * totalInvadingTroops));
            // Proportion of enemy army is destroyed
            // Proportion of ally army is destroyed 
            // No units are moved
            return false;
        }
        
    }
    
    public void setEngagementObserver(EngagementObserver engagementObserver) {
        return;
    }

    public void moveInvadingArmy(ArrayList<Unit> army, Province from, Province to) {
        for (Unit u : army) {
            from.removeUnit(u);
            to.addUnit(u);
        }
    }

    public void transferProvinceOwnership(Faction from, Faction to, Province p) {
        from.removeProvince(p);
        to.addProvince(p);
        p.setFaction(to);
    }

    private int calculateStrength(ArrayList<Unit> army, Boolean invading) {
        int sum = 0;
        for (Unit u : army) {
            sum += u.getNumTroops() * u.getAttack() * u.getMeleeDefence();
            if (invading) {
                totalInvadingTroops += u.getNumTroops();
            } else {
                totalDefendingTroops += u.getNumTroops();
            }
        }
        return sum;
    }

    public BuildingObserver getBuildingObserver() { return null; }

    public ArrayList<BattleObserver> getBattleObservers() { return null; }

    public void notifyBattleObservers(Faction f) {}

    public void setSeed(int seed) {}

    public void addDefeatObserver(DefeatObserver defObserver) {}

    public void notifyDefeat(Faction f) {}
}
