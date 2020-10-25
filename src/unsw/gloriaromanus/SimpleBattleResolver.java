package unsww.gloriaromanus;

import java.util.Random;

import org.graalvm.compiler.nodes.java.ArrayLengthNode;

public class SimpleBattleResolver implements BattleResolver {
    int attackingSum;
    int defendingSum;
    int totalInvadingTroops;
    int totalDefendingTroops;

    public SimpleBattleResolver(Game g) {
        this.game = game;
    }

    public boolean invade(Province invader, Province invaded, ArrayList<Unit> invArmy, ArrayList<Unit> defArmy) {
        totalInvadingTroops = 0;
        totalDefendingTroops = 0;
        attackingSum = calculateStrength(invArmy);
        defendingSum = calculateStrength(defArmy);
        double invadeSuccess = attackingSum/(attackingSum + defendingSum);
        double invadeFail = defendingSum/(attackingSum + defendingSum);
        Random r = new Random();
        double rNum = r.nextDouble();
        if (rNum <= invadeSuccess) {
            invaded.destroyAllUnits(invaded);
            invader.destroyProportionUnits(invArmy, (int) ((r.nextDouble % invadeFail) * totalInvadingTroops));
            moveInvadingArmy(invArmy, invader, invaded);
            transferProvinceOwnership(invader.getFaction(), invaded.getFaction(), invaded);
            // All of enemy army is destroyed
            // Proportion of ally army is destroyed
            // Remaining ally army is moved into invaded province
            // Invaded province joins invading faction
        } else {
            invaded.destroyProportionUnits(defArmy, (int) ((r.nextDouble() % invadeSuccess) * totalDefendingTroops));
            invaded.destroyProportionUnits(invArmy, (int) ((r.nextDouble() % invadeFail) * totalInvadingTroops));
            // Proportion of enemy army is destroyed
            // Proportion of ally army is destroyed 
            //No units are moved
        }
        
    }

    public void moveInvadingArmy(ArrayList<unit> army, Province from, Province to) {
        for (Unit u : army) {
            from.removeUnit(u);
            to.addUnit(u);
        }
    }

    public void trasferProvinceOwnership(Faction from, Faction to, Province p) {
        from.removeProvince(p);
        to.addProvince(p);
        p.changeFaction(to)
    }


    private int calculateStrength(ArrayList<Unit> army, Boolean invading) {
        sum = 0;
        for (Unit u : army) {
            sum += u.getNumTroops() * u.getAttack() * u.getDefense();
            if (invading) {
                totalInvadingTroops += u.getNumTroops();
            } else {
                totalDefendingTroops += u.getNumTroops();
            }
        }

    }
}
