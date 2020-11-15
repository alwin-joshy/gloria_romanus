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
    private ArrayList<Unit> defeatedTowers;
    private ArrayList<Unit> attackingArmy;
    private ArrayList<Unit> defendingArmy;
    private int engagementCounter;
    private ArrayList<BattleObserver> battleObservers;
    private transient EngagementObserver engagementObserver;
    private BuildingObserver buildingObserver;
    private Random r;
    private ArmyBuff attackingBuffs;
    private ArmyBuff defendingBuffs;

    public StandardBattleResolver(int seed) {
        routedAttackers = new ArrayList<Unit>();
        defeatedTowers = new ArrayList<Unit>();
        engagementCounter = 0;
        battleObservers = new ArrayList<BattleObserver>(Arrays.asList(new VictoryObserver(), new DefeatObserver()));
        buildingObserver = new BuildingObserver();
        if (seed != 0) {
            r = new Random(seed);
        } else {
            r = new Random();
        }
    }

    public void setEngagementObserver(EngagementObserver engagementObserver) {
        this.engagementObserver = engagementObserver;
    }

    public void notifyBattleObservers(Faction f) {
        for (BattleObserver bo : battleObservers) {
            bo.update(f);
        }
    }

    public boolean battle(Province attacking, ArrayList<Unit> attackingArmy, Province defending, ArrayList<Unit> defendingArmy) {
        this.attacking = attacking;
        this.attackingArmy = attackingArmy;
        this.defending = defending;
        this.defendingArmy = defendingArmy;

        attackingBuffs = new ArmyBuff(attackingArmy, attacking);
        defendingBuffs = new ArmyBuff(defendingArmy, defending);

        defendingBuffs.calculateDruidMultiplier(attackingBuffs.getNumDruids());
        attackingBuffs.calculateDruidMultiplier(defendingBuffs.getNumDruids());

        engagementCounter = 0;
        engagementObserver.notifyBattle(attacking.getName(), defending.getName());

        while (attackingArmy.size() > 0 && defendingArmy.size() > 0 && engagementCounter <= 200) {
            // randomly choose a unit from each
            attackingBuffs.setHeroicCharge(defendingArmy.size());
            defendingBuffs.setHeroicCharge(attackingArmy.size());

            Unit attackingUnit = attackingArmy.get(r.nextInt(attackingArmy.size()));
            Unit defendingUnit = defendingArmy.get(r.nextInt(defendingArmy.size()));
            // removes the loser from its army
            skirmish(attackingUnit, defendingUnit);
            if (onlyTowersRemaining()) {
                break;
            }

        }

        restoreTowers();

        if (defendingArmy.size() == 0 && attackingArmy.size() != 0) {
            Faction defendingTemp = defending.getFaction();
            Game.transferProvinceOwnership(defending.getFaction(), attacking.getFaction(), defending);
            attackingArmy.addAll(routedAttackers);
            if (attacking.getFactionName().equals("Roman")) {
                defending.resetLegionaryDeaths();
            }
            notifyBattleObservers(attacking.getFaction());
            notifyBattleObservers(defendingTemp);
            engagementObserver.notifyBattleWon(attacking.getFactionName(), defending.getName());
            buildingObserver.update(attacking.getFaction(), defendingTemp);
            return true;
        } else {
            defending.resetLegionaryDeaths();
            for (Unit u : routedAttackers) {
                attacking.addUnit(u);
            }
            attackingArmy.addAll(routedAttackers);
            engagementObserver.notifyBattleLost(defending.getFactionName(), attacking.getName());
            return false;
        }
    }

    private void skirmish(Unit attackingUnit, Unit defendingUnit) {
        int result = 0;
        engagementObserver.notifySkirmish(attackingUnit.getName(), defendingUnit.getName(), attacking.getFactionName(), defending.getFactionName());
        while (result == 0) {
            if (defendingUnit.getType().equals("tower")){
                result = towerEngagement(attackingUnit, defendingUnit);
            } else {
                result = engage(attackingUnit, defendingUnit);
                if (attackingUnit.isBroken() && defendingUnit.isBroken()) {
                    if (!routedAttackers.contains(attackingUnit)) {
                        routedAttackers.add(attackingUnit);
                    }
                    result = 0;
                    break;
                } else if (attackingUnit.isDefeated() && defendingUnit.isDefeated()) {
                    result = 0;
                    break;
                }
                engagementCounter++;
                if (engagementCounter > 200) {
                    return;
                }
            }
        }


        if (result == 1 || result == 0) {

            if (defendingUnit.getName().equals("druid")) {
                defendingBuffs.druidKilled(attackingBuffs.getNumDruids());
            } else if (defendingUnit.getName().equals("legionary")) {
                defendingBuffs.legionaryKilled(true);
            }
            defendingArmy.remove(defendingUnit);
            defending.removeUnit(defendingUnit);
        }

        if (result == -1 || result == 0) {

            if (attackingUnit.getName().equals("druid")) {
                attackingBuffs.druidKilled(defendingBuffs.getNumDruids());
            } else if (attackingUnit.getName().equals("legionary")) {
                attackingBuffs.legionaryKilled(false);
            }

            attackingArmy.remove(attackingUnit);
            attacking.removeUnit(attackingUnit);
        }

    }

    private int towerEngagement(Unit attackingUnit, Unit tower) {
        
        int towerDamage = tower.calculateDamage(attackingUnit, true, attackingBuffs.getHeroicCharge(), false, r);
        attackingUnit.takeDamage(towerDamage);
        engagementObserver.notifyEngagement(tower.getName(), towerDamage, attackingUnit.getName(), defending.getFactionName(), attacking.getFactionName());

        int attackingDamage = attackingUnit.getTowerDamage();
        tower.takeDamage(attackingDamage);
        engagementObserver.notifyEngagement(attackingUnit.getName(), attackingDamage, tower.getName(), attacking.getFactionName(), defending.getFactionName());
        
        if (attackingUnit.isDefeated()) {
            return -1;
        }
        else if (tower.isDefeated()) {
            defeatedTowers.add(tower);
            return 1;
        }

        double escapeChance = 0.5 + attackingUnit.getSpeed() * 0.1;
        if (escapeChance > 1.0) {
            return -2;
        }

        return 0;
    }

    private void restoreTowers() {
        for (Unit t : defeatedTowers) {
            defending.addUnit(t);
        }
    }

    private boolean onlyTowersRemaining() {
        for (Unit u : defendingArmy) {
            if ( ! u.getType().equals("tower")){
                return false;
            }
        }
        return true;
    }

    private int engage(Unit attackingUnit, Unit defendingUnit) {
        boolean isRangedEngagement = decideEngagementType(defending, attackingUnit, defendingUnit);
        int attackerSize = attackingUnit.getNumTroops();
        int defenderSize = defendingUnit.getNumTroops();

        attackingUnit.incrementEngagementCount();
        defendingUnit.incrementEngagementCount();

        int attackerDamage = 0;
        int defenderDamage = 0;

        if (!attackingUnit.isBroken()) {
    
            Unit temp = null;
            if (attackingUnit.getName().equals("elephant") && attackingArmy.size() > 1) {
                int x = r.nextInt(10);
                if (x == 0) {
                    int flag = 0;
                    while (flag == 0) {
                        temp = defendingUnit;
                        defendingUnit = attackingArmy.get(r.nextInt(attackingArmy.size()));
                        if (defendingUnit != attackingUnit) flag = 1;
                    }
                }
            }
            attackerDamage = attackingUnit.calculateDamage(defendingUnit, isRangedEngagement, attackingBuffs.getHeroicCharge(), defending.hasWalls() ,r);
            defendingUnit.takeDamage(attackerDamage);
            engagementObserver.notifyEngagement(attackingUnit.getName(), attackerDamage, defendingUnit.getName(), attacking.getFactionName(), defending.getFactionName());
            if (temp != null) defendingUnit = temp;
        }

        int result = 0;
        if (defendingUnit.isDefeated()) result = 1;

        if (!defendingUnit.isBroken()) {
            Unit temp = null;
            if (defendingUnit.getName().equals("elephant") && defendingArmy.size() > 1) {
                int x = r.nextInt(10);
                if (x == 0) {
                    int flag = 0;
                    while (flag == 0) {
                        temp = attackingUnit;
                        attackingUnit = defendingArmy.get(r.nextInt(defendingArmy.size()));
                        if (attackingUnit != defendingUnit) flag = 1;
                    }
                }
            }
            defenderDamage = defendingUnit.calculateDamage(attackingUnit, isRangedEngagement, defendingBuffs.getHeroicCharge(), false, r);
            attackingUnit.takeDamage(defenderDamage);
            engagementObserver.notifyEngagement(defendingUnit.getName(), defenderDamage, attackingUnit.getName(), defending.getFactionName(), attacking.getFactionName());
            if (temp != null) defendingUnit = temp;
        }

        if (attackingUnit.isDefeated()) result = -1;

        double route = r.nextDouble();
        double routeChance;

        if (attackingUnit.isBroken()) {
            routeChance = 0.5 + 0.1 * (attackingUnit.getSpeed() - defendingUnit.getSpeed());
            if (routeChance < 0.1) routeChance = 0.1;
            if (route < routeChance) {
                routedAttackers.add(attackingUnit);
                result = -1;
                engagementObserver.notifyRoute(attackingUnit.getName(), attacking.getFactionName());
            }
        } else if (defendingUnit.isBroken()) {
            routeChance = 0.5 + 0.1 * (defendingUnit.getSpeed() - attackingUnit.getSpeed());
            if (routeChance < 0.1) routeChance = 0.1;
            if (route < routeChance) {
                result = 1;
                engagementObserver.notifyRoute(defendingUnit.getName(), defending.getFactionName());
            }
        }

        if (defendingUnit.checkIfBroken(defenderDamage, defenderSize, attackerDamage, attackerSize, attackingBuffs, defending.getLegionaryDebuff(), attackingUnit.getSmithMoraleDebuff(), r))
            engagementObserver.notifyBreak(defendingUnit.getName(), defending.getFactionName());
        if (attackingUnit.checkIfBroken(attackerDamage, attackerSize, defenderDamage, defenderSize, defendingBuffs, attacking.getLegionaryDebuff(), defendingUnit.getSmithMoraleDebuff(), r))
            engagementObserver.notifyBreak(attackingUnit.getName(), attacking.getFactionName());
        return result;

    }

    private boolean decideEngagementType(Province defending, Unit attackingUnit, Unit defendingUnit) {
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
            double rangedThreshold = defending.hasWalls() ? 0.9 : 0.5;
            rangedThreshold -= 0.1 * (melee.getSpeed() - ranged.getSpeed());
            if (rangedThreshold > 0.95) rangedThreshold = 0.95;
            if (rangedThreshold < 0.05) rangedThreshold = 0.05;
            rangedEngagement = r.nextDouble() <= rangedThreshold ? true : false;
        }
        return rangedEngagement;
    }

    public ArrayList<BattleObserver> getBattleObservers() {
        return battleObservers;
    }

    public BuildingObserver getBuildingObserver() {
        return buildingObserver;
    }

    public void setSeed(int seed) {
        r.setSeed(seed);
    }
}
