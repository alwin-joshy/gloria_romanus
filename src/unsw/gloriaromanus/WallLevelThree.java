package unsw.gloriaromanus;

public class WallLevelThree implements WallLevel {

    @Override
    public void goNext(Walls w) {
        w.setState(new WallLevelFour());

    }

    @Override
    public void addTowers(Province p) {
        p.addUnit(new Unit("ballistatower"));
    }
    
}
