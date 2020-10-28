package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Random;

public class StandardBattleResolver implements BattleResolver {
    private boolean rangedEngagement;
    private ArrayList<Unit> routedAttackers;
    private int engagementCounter;

    public StandardBattleResolver() {
        routedAttackers = new ArrayList<Unit>();
        engagementCounter = 0;
    }

    public boolean battle(Province invader, ArrayList<Unit> attackingArmy, Province invaded, ArrayList<Unit> defendingArmy) {
        engagementCounter = 0;
        while (attackingArmy.size() > 0 && defendingArmy.size() > 0 && engagementCounter <= 200) {
            Random random = new Random();
            // randomly choose a unit from each
            Unit attackingUnit = attackingArmy.get(random.nextInt(attackingArmy.size()));
            Unit defendingUnit = defendingArmy.get(random.nextInt(defendingArmy.size()));
            // removes the loser from its army
            int result = skirmish(invaded, attackingUnit, defendingUnit);
            if (result == 1 || result == 0) {
                defendingArmy.remove(defendingUnit);
                invaded.removeUnit(defendingUnit);
            }
            if (result == -1 || result == 0) {
                attackingArmy.remove(attackingUnit);
                invader.removeUnit(attackingUnit);
            }
        }

        if (attackingArmy.size() == 0) return true;
        else return false;
    }

    public int skirmish(Province invaded, Unit attackingUnit, Unit defendingUnit) {
        int result = 0;
        while (result == 0) {
            result = engage(invaded, attackingUnit, defendingUnit);
            if (attackingUnit.isBroken() && defendingUnit.isBroken()) {
                if (!routedAttackers.contains(attackingUnit)) {
                    routedAttackers.add(attackingUnit);
                }
                return 0;
            }
            engagementCounter++;
        }
        return result;
    }

    public int engage(Province invaded, Unit attackingUnit, Unit defendingUnit) {
        boolean isRangedEngagement = decideEngagementType(invaded, attackingUnit, defendingUnit);
        int attackerSize = attackingUnit.getNumTroops();
        int defenderSize = defendingUnit.getNumTroops();

        int attackerDamage = 0;
        int defenderDamage = 0;

        if (!attackingUnit.isBroken()) {
            attackerDamage = attackingUnit.calculateDamage(defendingUnit, isRangedEngagement);
            defendingUnit.takeDamage(attackerDamage);
        }

        int result = 0;
        if (defendingUnit.isDefeated()) result = 1;

        if (!defendingUnit.isBroken()) {
            defenderDamage = defendingUnit.calculateDamage(attackingUnit, isRangedEngagement);
            attackingUnit.takeDamage(defenderDamage);
        }

        if (attackingUnit.isDefeated()) result = -1;

        defendingUnit.checkIfBroken(defenderDamage, defenderSize, attackerDamage, attackerSize);
        attackingUnit.checkIfBroken(attackerDamage, attackerSize, defenderDamage, defenderSize);

        Random random = new Random();
        double r = random.nextDouble();
        double routeChance;

        if (attackingUnit.isBroken()) {
            routeChance = 0.5 + 0.1 * (attackingUnit.getSpeed() - defendingUnit.getSpeed());
            if (r < routeChance) {
                routedAttackers.add(attackingUnit);
                result = -1;
            }
        } else if (defendingUnit.isBroken()) {
            routeChance = 0.5 + 0.1 * (defendingUnit.getSpeed() - attackingUnit.getSpeed());
            if (r < routeChance) result = 1;
        }
        return result;

    }

    public boolean decideEngagementType(Province invaded, Unit attackingUnit, Unit defendingUnit) {
        if (defendingUnit.checkType("tower")) {
            rangedEngagement = true;
        }
        if (attackingUnit.isRanged() && defendingUnit.isRanged()) {
            rangedEngagement = true;
        } else if (!attackingUnit.isRanged() && !defendingUnit.isRanged()) {
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
        return rangedEngagement;
    }
}
