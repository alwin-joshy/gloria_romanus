package unsw.gloriaromanus;

import java.io.Serializable;

public class BuildingObserver implements Serializable {
    private Game g;

    public void setGame(Game g) {
        this.g = g;
    }

    public void update(Faction f) {
        g.setFactionToRecalculateBonus(f.getName());
    }

    public void update(Faction f1, Faction f2) {
        update(f1);
        update(f2);
    }
}
