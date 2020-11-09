package unsw.gloriaromanus;

public class WallLevelFour implements WallLevel {
    private boolean upgraded;

    @Override
    public void goNext(Walls w) {
        return; 
    }

    @Override
    public void addTowers(Province p) {
        if (upgraded == false) {
            p.replaceArcherTowers();
            upgraded = true;
        }
    }
    
}
