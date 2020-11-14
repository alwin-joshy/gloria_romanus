package unsw.gloriaromanus;

public class Road extends Infrastructure {
    private Province p;

    public Road(Province p) {
        super();
        this.p = p;
        setBaseCost(85);
        setBaseContructionTime(3);
        setName("Road");
        setDescription("Each Road upgrade will decrease the amount of movement points spent travelling to Provinces by 4/3/2/1* movement points depending on level. \n \n * Only Romans will be able to build the maximum level, however once built any other units can use the highway.*");
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
