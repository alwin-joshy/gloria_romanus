package unsw.gloriaromanus;

import java.io.Serializable;

public interface WallLevel extends Serializable{
    public void goNext(Walls w);
    public void addTowers(Province p);
}
