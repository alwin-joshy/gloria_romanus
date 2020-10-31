package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class StandardBattleResolver implements BattleResolver, Serializable {
    private boolean rangedEngagement;
    private Province attacking;
    private Province defending;
    private ArrayList<Unit> routedAttackers;
    private ArrayList<Unit> attackingArmy;
    private ArrayList<Unit> defendingArmy;
    private int engagementCounter;
    private int attackingDruids;
    private int defendingDruids;
    private int attackingLegionaryCount;
    private int defendingLegionaryCount;
    private boolean attackingHeroicCharge;
    private boolean defendingHeroicCharge;
    private double attackingDruidMultiplier;
    private double defendingDruidMultiplier;
    private ArrayList<BattleObserver> observers;

    public StandardBattleResolver() {
        routedAttackers = new ArrayList<Unit>();
        engagementCounter = 0;
        observers = new ArrayList<BattleObserver>(Arrays.asList(new VictoryObserver(), new DefeatObserver()));
    }

    public void notify(Faction f) {
        for (BattleObserver bo : observers) {
            bo.update(f);
        }
    }

    public void setTaxDebuff(Province p, ArrayList<Unit> army) {
        int isVeryHighTax = p.isVeryHighTax();
        for (Unit u : army) {
            u.setTaxDebuff(isVeryHighTax);
        }
    }

    public void resetEngagementCount() {
        for (Unit u : attackingArmy) {
            u.resetEngagementCount();
        }
        for (Unit u : defendingArmy) {
            u.resetEngagementCount();
        }
    }

    public int countUnit(Province p, ArrayList<Unit> army, String name, String faction) {
        int count = 0;
        if (!p.getFaction().getName().equals(faction)) return count;
        for (Unit u : army) {
            if (u.getName().equals(name)) count++;
        }
        return count;
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
        this.attacking = attacking;
        this.attackingArmy = attackingArmy;
        this.defending = defending;
        this.defendingArmy = defendingArmy;

        attackingDruids = countUnit(attacking, attackingArmy, "druid", "Spanish");
        defendingDruids = 0;
        if (attackingDruids == 0)
            defendingDruids = countUnit(defending, defendingArmy, "druid", "Spanish");

        attackingLegionaryCount = countUnit(attacking, attackingArmy, "legionary", "Roman");
        defendingLegionaryCount = 0;
        if (attackingLegionaryCount == 0)
            defendingLegionaryCount = countUnit(defending, defendingArmy, "legionary", "Roman");

        attackingDruidMultiplier = getDruidMultiplier(true);
        defendingDruidMultiplier = getDruidMultiplier(false);

        engagementCounter = 0;

        setTaxDebuff(attacking, attackingArmy);
        setTaxDebuff(defending, defendingArmy);

        while (attackingArmy.size() > 0 && defendingArmy.size() > 0 && engagementCounter <= 200) {
            Random random = new Random();
            // randomly choose a unit from each
            Unit attackingUnit = attackingArmy.get(random.nextInt(attackingArmy.size()));
            Unit defendingUnit = defendingArmy.get(random.nextInt(defendingArmy.size()));
            // removes the loser from its army
            int result = skirmish(attackingUnit, defendingUnit);
            if (result == 1 || result == 0) {
                if (defendingUnit.getName().equals("druid")) {
                    defendingDruids--;
                    attackingDruidMultiplier = getDruidMultiplier(true);
                    defendingDruidMultiplier = getDruidMultiplier(false);
                } else if (attackingUnit.getName().equals("legionary")) {
                    defendingLegionaryCount--;
                    defending.incrementNumLegionaryDeaths();
                }
                defendingArmy.remove(defendingUnit);
                defending.removeUnit(defendingUnit);
            }
            if (result == -1 || result == 0) {
                if (attackingUnit.getName().equals("druid")) {
                    attackingDruids--;
                    attackingDruidMultiplier = getDruidMultiplier(true);
                    defendingDruidMultiplier = getDruidMultiplier(false);
                } else if (attackingUnit.getName().equals("legionary")) {
                    attackingLegionaryCount--;
                }
                attackingArmy.remove(attackingUnit);
                attacking.removeUnit(attackingUnit);
            }
        }

        if (defendingArmy.size() == 0) {
            transferProvinceOwnership(defending.getFaction(), attacking.getFaction(), defending);
            for (Unit u : routedAttackers) {
                defending.addUnit(u);
            }
            if (attacking.getFaction().getName().equals("Roman")) {
                defending.resetLegionaryDeaths();
            }
            notify(attacking.getFaction());
            return true;
        } else {
            defending.resetLegionaryDeaths();
            return false;
        }
    }

    public int skirmish(Unit attackingUnit, Unit defendingUnit) {
        int result = 0;
        if (attackingArmy.size() < defendingArmy.size() / 2) {
            attackingHeroicCharge = true;
            defendingHeroicCharge = false;
        } else if (attackingArmy.size() > defendingArmy.size() * 2) {
            attackingHeroicCharge = false;
            defendingHeroicCharge = true;
        } else {
            attackingHeroicCharge = false;
            defendingHeroicCharge = false;
        }
        while (result == 0) {
            result = engage(attackingUnit, defendingUnit);
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

    public int engage(Unit attackingUnit, Unit defendingUnit) {
        boolean isRangedEngagement = decideEngagementType(defending, attackingUnit, defendingUnit);
        int attackerSize = attackingUnit.getNumTroops();
        int defenderSize = defendingUnit.getNumTroops();

        attackingUnit.incrementEngagementCount();
        defendingUnit.incrementEngagementCount();

        int attackerDamage = 0;
        int defenderDamage = 0;

        if (!attackingUnit.isBroken()) {
            Unit temp = null;
            if (attackingUnit.getName().equals("elephant")) {
                Random random = new Random();
                int x = random.nextInt(10);
                if (x == 0) {
                    temp = defendingUnit;
                    defendingUnit = attackingArmy.get(random.nextInt(attackingArmy.size()));
                }
            }
            attackerDamage = attackingUnit.calculateDamage(defendingUnit, isRangedEngagement, attackingHeroicCharge);
            defendingUnit.takeDamage(attackerDamage);
            if (temp != null) defendingUnit = temp;
        }

        int result = 0;
        if (defendingUnit.isDefeated()) result = 1;

        if (!defendingUnit.isBroken()) {
            Unit temp = null;
            if (defendingUnit.getName().equals("elephant")) {
                Random random = new Random();
                int x = random.nextInt(10);
                if (x == 0) {
                    temp = attackingUnit;
                    attackingUnit = defendingArmy.get(random.nextInt(defendingArmy.size()));
                }
            }
            defenderDamage = defendingUnit.calculateDamage(attackingUnit, isRangedEngagement, defendingHeroicCharge);
            attackingUnit.takeDamage(defenderDamage);
            if (temp != null) defendingUnit = temp;
        }

        if (attackingUnit.isDefeated()) result = -1;

        defendingUnit.checkIfBroken(defenderDamage, defenderSize, attackerDamage, attackerSize, defendingDruidMultiplier, attackingHeroicCharge, attackingLegionaryCount, defending.getLegionaryDebuff());
        attackingUnit.checkIfBroken(attackerDamage, attackerSize, defenderDamage, defenderSize, attackingDruidMultiplier, defendingHeroicCharge, defendingLegionaryCount, attacking.getLegionaryDebuff());

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

    public ArrayList<BattleObserver> getObservers() {
        return observers;
    }
}
