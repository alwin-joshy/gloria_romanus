package unsw.gloriaromanus;

public class VictoryObserver implements BattleObserver {
    private Game g;

    public void setGame(Game g) {
        this.g = g;
    }

    public void update(Faction f) {
        if (g.getVictoryCondition().checkVictory(f)) g.endGame();
    }
}
