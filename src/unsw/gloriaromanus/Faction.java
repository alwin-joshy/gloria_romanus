package unsw.gloriaromanus;

import java.util.ArrayList;

public class Faction {
    
    private ArrayList<Province> alliedProvinces;
    private int treasury;
    private String name;
    private int totalProvinceWealth;
    private Boolean isPlayer;

    public Faction(String name) {
        this.name = name;
        alliedProvinces = new ArrayList<Province>();
        treasury = 100;
        totalProvinceWealth = 0;
    }

    public void addProvince(Province p) {
        alliedProvinces.add(p);
    }

    public void collectTax() {
        for (Province p : alliedProvinces) {
            treasury += p.applyTax();
        }
    }
}
