package unsw.gloriaromanus;

public class SmithLevelFour extends SmithLevelThree {
    @Override
    public double getEnemyMoraleDebuff() {
        return 0.8;
    }

    @Override
    public double applyMissileDamageDebuff(double damage) {
        return (damage * 0.9 >= 1 ? damage * 0.9 : 1);
    }

    @Override
    public void nextLevel(Province p) {
        // Do nothing
    }
}
