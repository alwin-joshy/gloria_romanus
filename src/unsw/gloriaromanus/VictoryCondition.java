package unsw.gloriaromanus;

import java.io.Serializable;

public interface VictoryCondition extends Serializable {
    public boolean checkCondition(Faction f);
}
