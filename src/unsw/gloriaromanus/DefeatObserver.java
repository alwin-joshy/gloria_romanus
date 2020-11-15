package unsw.gloriaromanus;

public class DefeatObserver implements BattleObserver {
    private Game g;
    private transient GloriaRomanusController controller;

    public DefeatObserver(GloriaRomanusController controller, Game g) {
        this.controller = controller;
        this.g = g;
    }

    public void setGame(Game g) {
        this.g = g;
    }

    public void update(Faction f) {
        System.out.println(f.getName());
        if (f.getNumProvinces() == 0 && ! f.getName().equals("Rebel")){
            g.getFactions().remove(f);
            controller.setLosingFaction(f.getName());
        }
    }

    public void setController(GloriaRomanusController controller) {
        this.controller = controller;
    }
}
