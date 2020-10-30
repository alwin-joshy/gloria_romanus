package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Random;

public class StandardBattleResolver implements BattleResolver {
    private boolean rangedEngagement;
    private ArrayList<Unit> routedAttackers;
    private int engagementCounter;
    private int attackingDruids;
    private int defendingDruids;

    public StandardBattleResolver() {
        routedAttackers = new ArrayList<Unit>();
        engagementCounter = 0;
    }

    public void setTaxDebuff(Province p, ArrayList<Unit> army) {
        int isVeryHighTax = p.isVeryHighTax();
        for (Unit u : army) {
            u.setTaxDebuff(isVeryHighTax);
        }
    }

    public int countDruids(Province p, ArrayList<Unit> army) {
        int totalDruids = 0;
        if (!p.getFaction().getName().equals("Spanish")) return totalDruids;
        for (Unit u : army) {
            if (u.getName().equals("druid")) totalDruids++;
        }
        return totalDruids;
    }

    public double getDruidMultiplier(boolean isAttacker) {
        double multiplier = 1.0;
        if (isAttacker) {
            multiplier += attackingDruids * 0.1;
            multiplier -= defendingDruids * 0.05;
        } else {
            multiplier += defendingDruids * 0.1;
            multiplier -= attackingDruids * 0.05;
        }
        if (multiplier > 1.5) multiplier = 1.5;
        if (multiplier < 0.75) multiplier = 0.75;
        return multiplier;
    }

    public boolean battle(Province attacking, ArrayList<Unit> attackingArmy, Province defending, ArrayList<Unit> defendingArmy) {
        attackingDruids = countDruids(attacking, attackingArmy);
        defendingDruids = countDruids(defending, defendingArmy);
        engagementCounter = 0;
        setTaxDebuff(attacking, attackingArmy);
        setTaxDebuff(defending, defendingArmy);

        while (attackingArmy.size() > 0 && defendingArmy.size() > 0 && engagementCounter <= 200) {
            Random random = new Random();
            // randomly choose a unit from each
            Unit attackingUnit = attackingArmy.get(random.nextInt(attackingArmy.size()));
            Unit defendingUnit = defendingArmy.get(random.nextInt(defendingArmy.size()));
            // removes the loser from its army
            int result = skirmish(defending, attackingUnit, defendingUnit);
            if (result == 1 || result == 0) {
                defendingArmy.remove(defendingUnit);
                defending.removeUnit(defendingUnit);
            }
            if (result == -1 || result == 0) {
                attackingArmy.remove(attackingUnit);
                attacking.removeUnit(attackingUnit);
            }
        }

        if (defendingArmy.size() == 0) {
            transferProvinceOwnership(defending.getFaction(), attacking.getFaction(), defending);
            for (Unit u : routedAttackers) {
                defending.addUnit(u);
            }
            return true;
        } else {
            return false;
        }
    }

    public int skirmish(Province defending, Unit attackingUnit, Unit defendingUnit) {
        int result = 0;
        while (result == 0) {
            result = engage(defending, attackingUnit, defendingUnit);
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

    public int engage(Province defending, Unit attackingUnit, Unit defendingUnit) {
        boolean isRangedEngagement = decideEngagementType(defending, attackingUnit, defendingUnit);
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

        defendingUnit.checkIfBroken(defenderDamage, defenderSize, attackerDamage, attackerSize, getDruidMultiplier(false));
        attackingUnit.checkIfBroken(attackerDamage, attackerSize, defenderDamage, defenderSize, getDruidMultiplier(true));

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

    public boolean decideEngagementType(Province defending, Unit attackingUnit, Unit defendingUnit) {
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
            double rangedThreshold = defending.hasWalls() ? 0.9 : 0.5;
            rangedThreshold -= 0.1 * (melee.getSpeed() - ranged.getSpeed());
            if (rangedThreshold > 0.95) rangedThreshold = 0.95;
            if (rangedThreshold < 0.05) rangedThreshold = 0.05;
            rangedEngagement = r.nextDouble() <= rangedThreshold ? true : false;
        }
        return rangedEngagement;
    }

    public void transferProvinceOwnership(Faction from, Faction to, Province p) {
        from.removeProvince(p);
        to.addProvince(p);
        p.setFaction(to);
    }
}
