package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Random;

public interface BattleResolver {
    public boolean battle(Province attacking, ArrayList<Unit> attackingArmy, Province defending, ArrayList<Unit> defendingArmy);
}
