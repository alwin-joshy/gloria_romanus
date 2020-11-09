package unsw.gloriaromanus;

public class WallLevelTwo implements WallLevel{

    @Override
    public void goNext(Walls w) {
        w.setState(new WallLevelThree());
    }

    @Override
    public void addTowers(Province p) {
        p.addUnit(new Unit("archertower"));
        p.addUnit(new Unit("archertower"));
    }
    
}
