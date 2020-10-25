package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Random;

public class StandardBattleResolver implements BattleResolver {
    public boolean rangedEngagement;

    public static void battle(Province invader, Province invaded, ArrayList<Unit> attackingArmy, ArrayList<Unit> defendingArmy) {
        while (attackingArmy.size() > 0 && defendingArmy.size() > 0) {
            Random random = new Random();
            // randomly choose a unit from each
            Unit attackingUnit = attackingArmy.get(random.nextInt(attackingArmy.size()));
            Unit defendingUnit = defendingArmy.get(random.nextInt(defendingArmy.size()));
            // removes the loser from its army
            int result = skirmish(attackingUnit, defendingUnit);
            if (result == 1) defendingArmy.remove(defendingUnit);
            if (result == -1) attackingArmy.remove(attackingUnit);
        }
    }

    public static int skirmish(Province invaded Unit attackingUnit, Unit defendingUnit) {
        int engagementCounter = 0;
        while (!attackingUnit.DefeatedOrRouted() && !defendingUnit.DefeatedOrRouted()) {
            engage(invaded, attackingUnit, defendingUnit);
            engagementCounter++;
            // draw
            if (engagementCounter > 200) return 0;
        }

        if (defendingUnit.DefeatedOrRouted()) return 1;
        if (attackingUnit.DefeatedOrRouted()) return -1;
    }

    public static void engage(Unit attackingUnit, Unit defendingUnit) {
        decideEngagementType(Unit attackingUnit, Unit defendingUnit);
    }

    public static void decideEngagementType(Province invaded, Unit attackingUnit, Unit defendingUnit) {
        if (defendingUnit.checkType("tower")) {
            rangedEngagement = true;
            return;
        }
        if (attackingUnit.isRanged() and defendingUnit.isRanged()) {
            rangedEngagement = true;
        } else if (! attackingUnit.isRanged() and ! defendingUnit.isRanged()) {
            rangedEngagement = false;
        } else {
            Unit melee = attackingUnit.isRanged() ? defendingUnit : attackingUnit;
            Unit ranged = melee == attackingUnit ? defendingUnit : attackingUnit;
            Random r = new Random();
            double rangedThreshold = invaded.hasWalls() ? 0.9 : 0.5;
            rangedThreshold -= 0.1 * (melee.getSpeed() - ranged.getSpeed());
            if (rangedThreshold > 0.95) rangedThreshold = 0.95;
            if (rangedThreshold < 0.05) rangedThreshold = 0.05;
            rangedEngagement = r.nextDouble() <= rangedThreshold ? true : false;
        }
    }
}
