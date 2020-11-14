package unsw.gloriaromanus;

public class SmithLevelZero implements SmithLevel {
    private int level = 0;

    public int getLevel() {
        return level;
    }

    public double applyEnemyAttackDebuff(double damage) {
        return damage;
    }

    public double applySpeedDebuff(int speed) {
        return speed;
    }

    public double applyAttackBuff(double attack) {
        return attack;
    }

    public double applyMoraleBuff(int morale) {
        return morale;
    }

    public double getEnemyMoraleDebuff() {
        return 1.0;
    }

    public double applyMissileDamageDebuff(double damage) {
        return damage;
    }

    public void nextLevel(Province p) {
        p.setSmithLevel(new SmithLevelOne());
    }
}
