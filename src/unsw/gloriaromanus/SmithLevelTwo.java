package unsw.gloriaromanus;

public class SmithLevelTwo extends SmithLevelOne {
    public SmithLevelTwo() {
        setLevel(2);
    }

    @Override
    public double applyEnemyAttackDebuff(double damage) {
        return super.applyEnemyAttackDebuff(damage) * 0.5 >= 1 ? super.applyEnemyAttackDebuff(damage) * 0.5  : 1;
    }

    @Override
    public double applySpeedDebuff(int speed) {
        return (double) speed * 0.8;
    }

    @Override
    public void nextLevel(Province p) {
        p.setSmithLevel(new SmithLevelThree());
    }
}
