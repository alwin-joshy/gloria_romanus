package unsw.gloriaromanus;

public class SmithLevelThree extends SmithLevelTwo {
    public SmithLevelThree() {
        setLevel(3);
    }
    
    @Override
    public double applyAttackBuff(double attack) {
        return (double) attack * 1.2;
    }

    @Override
    public double applyMoraleBuff(int morale) {
        return (double) morale * 1.1;
    }

    @Override
    public void nextLevel(Smith s) {
        s.setSmithLevel(new SmithLevelFour());
    }
    
}
