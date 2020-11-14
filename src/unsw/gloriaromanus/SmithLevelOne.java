package unsw.gloriaromanus;

public class SmithLevelOne extends SmithLevelZero {
    private int level = 1;
    
    @Override
    public double applyEnemyAttackDebuff(double damage) {
        return (damage - 1.0 >= 1.0 ? damage - 1.0 : 1.0);
    }

    @Override
    public void nextLevel(Province p) {
        p.setSmithLevel(new SmithLevelTwo());
    }
}
