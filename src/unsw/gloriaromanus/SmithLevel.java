package unsw.gloriaromanus;

public interface SmithLevel {
    public double applyEnemyAttackDebuff(double damage);
    public double applySpeedDebuff(int speed);
    public double applyAttackBuff(double attack);
    public double applyMoraleBuff(int morale);
    public double getEnemyMoraleDebuff();
    public double applyMissileDamageDebuff(double damage);
    public void nextLevel(Province p);
}