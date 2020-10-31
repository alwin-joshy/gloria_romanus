package unsw.gloriaromanus;

import java.io.Serializable;

public interface BattleObserver extends Serializable {
    public void update(Faction f);
    public void setGame(Game g);
}
