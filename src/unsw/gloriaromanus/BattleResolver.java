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
        }
    }

    public static int skirmish(Unit attackingUnit, Unit defendingUnit) {
        int engagementCounter = 0;
        while (!attackingUnit.DefeatedOrRouted() && !defendingUnit.DefeatedOrRouted()) {
            engage(attackingUnit, defendingUnit);
            engagementCounter++;
            // draw
            if (engagementCounter > 200) return 0;
        }

        if (defendingUnit.DefeatedOrRouted()) return 1;
        if (attackingUnit.DefeatedOrRouted()) return -1;
    }

    public static void engage(Unit attackingUnit, Unit defendingUnit) {
        
    }
}
