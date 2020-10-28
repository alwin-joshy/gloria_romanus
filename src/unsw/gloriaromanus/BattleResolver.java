package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Random;

public class BattleResolver {
    public static void battle(ArrayList<Unit> attackingArmy, ArrayList<Unit> defendingArmy) {
        while (attackingArmy.size() > 0 && defendingArmy.size() > 0) {
            Random random = new Random();
            // randomly choose a unit from each
            Unit attackingUnit = attackingArmy.get(random.nextInt(attackingArmy.size()));
            Unit defendingUnit = defendingArmy.get(random.nextInt(defendingArmy.size()));
            // removes the loser from its army
            int result = skirmish(attackingUnit, defendingUnit);
            if (result == 1) defendingArmy.remove(defendingUnit);
            if (result == -1) attackingArmy.remove(attackingUnit);
            if (result == 2) {
                defendingArmy.remove(defendingUnit);
                attackingArmy.remove(attackingUnit);
            }
        }
    }

    public static int skirmish(Unit attackingUnit, Unit defendingUnit) {
        int engagementCounter = 0;
        int rangedSpeed;
        int meleeSpeed;
        if (attackingUnit.isRanged()) {
            rangedSpeed = attackingUnit.getSpeed();
            meleeSpeed = defendingUnit.getSpeed();
        } else {
            rangedSpeed = defendingUnit.getSpeed();
            meleeSpeed = attackingUnit.getSpeed();
        }
        while (!attackingUnit.isDefeatedOrRouted() && !defendingUnit.isDefeatedOrRouted()) {
            // determine if the engagement is melee or ranged
            if (attackingUnit.isRanged() && defendingUnit.isRanged()) {
                engage(attackingUnit, defendingUnit, false);
            } else if (!attackingUnit.isRanged() && !defendingUnit.isRanged()) {
                engage(attackingUnit, defendingUnit, true);
            } else {
                Random random = new Random();
                int meleeChance = 50 + 10 * (meleeSpeed - rangedSpeed);
                if (meleeChance > 95)
                    meleeChance = 95;
                else if (meleeChance < 5)
                    meleeChance = 5;
                int eng = random.nextInt(100);
                if (eng < meleeChance) {
                    engage(attackingUnit, defendingUnit, true);
                } else {
                    engage(attackingUnit, defendingUnit, false);
                }
            }
            if (attackingUnit.isBroken() && defendingUnit.isBroken()) return 2;
            engagementCounter++;
            // draw
            if (engagementCounter > 200) return 0;
        }

        if (defendingUnit.isDefeatedOrRouted())
            return 1;
        else 
            return -1;
    }

    public static void engage(Unit attackingUnit, Unit defendingUnit, boolean isMeleeEngagement) {
        int attackerSize = attackingUnit.getNumTroops();
        int defenderSize = defendingUnit.getNumTroops();

        int attackerDamage = 0;
        int defenderDamage = 0;

        if (!attackingUnit.isBroken()) {
            attackerDamage = attackingUnit.calculateDamage(defendingUnit, isMeleeEngagement);
            defendingUnit.takeDamage(attackerDamage);
        }

        if (!defendingUnit.isBroken()) {
            defenderDamage = defendingUnit.calculateDamage(attackingUnit, isMeleeEngagement);
            attackingUnit.takeDamage(defenderDamage);
        }

        defendingUnit.checkIfBroken(defenderDamage, defenderSize, attackerDamage, attackerSize);
        attackingUnit.checkIfBroken(attackerDamage, attackerSize, defenderDamage, defenderSize);

        Random random = new Random();
        int r = random.nextInt();
        int routeChance;

        if (attackingUnit.isBroken()) {
            routeChance = 50 + 10 * (attackingUnit.getSpeed() - defendingUnit.getSpeed());
            if (r < routeChance) attackingUnit.setAsRouted();
        } else {
            routeChance = 50 + 10 * (defendingUnit.getSpeed() - attackingUnit.getSpeed());
            if (r < routeChance) defendingUnit.setAsRouted();
        }
    }
    
}
