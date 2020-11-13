package unsw.gloriaromanus;

public class Road extends Infrastructure {
    private Province p;

    public Road(Province p) {
        super();
        this.p = p;
        setBaseCost(85);
        setBaseContructionTime(3);
        setName("Road");
    }

    public void levelUp() {
        updateCosts();
        Game.updateAdjacentProvinces(p);
    }

    public Province getProvince() {
        return p;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Road r = (Road) obj;
        return super.equals(obj) && p.getName().equals(r.getProvince().getName());
    }
}
