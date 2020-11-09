package unsw.gloriaromanus;

public class WallLevelOne implements WallLevel {

    @Override
    public void goNext(Walls w) {
        w.setState(new WallLevelTwo());
    }

    @Override
    public void addTowers(Province p) {
        // No towers in level one
        return;
    }
    
}
