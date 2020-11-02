package unsw.gloriaromanus;

public class DefeatObserver implements BattleObserver {
    private Game g;

    public void setGame(Game g) {
        this.g = g;
    }

    public void update(Faction f) {
        if (f.getNumProvinces() == 0) g.getFactions().remove(f);
    }
}
