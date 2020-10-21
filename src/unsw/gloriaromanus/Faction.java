package unsw.gloriaromanus;

import java.util.ArrayList;

public class Faction {
    
    private ArrayList<Province> alliedProvinces;
    private int treasury;
    private String name;
    private int totalProvinceWealth;
    private Boolean isPlayer;
    private Double mineMultiplier;
    private Double marketMultiplier;
    private int portBonus;


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

    public void calculateMineMultiplier() {
        mineMultiplier = 1;
        for (Province p : alliedProvinces) {
            if (Mine m = p.getMine()){
                mineMultiplier *= m.getMultiplierValue();
            }
        } 
    }

    public void calculueMarketMultiplier() {
        marketMultiplier = 1;
        for (Province p : alliedProvinces) {
            if (Market m = p.getMarket()) {
                marketMultiplier *= m.getMultiplierValue();
            }
        }
    }

    public void calculatePortBonus() {
        portBonus = 0;
        for (Province p : alliedProvinces) {
            if (Port pt = p.getPort()) {
                portBonus += pt.getBonus();
            }
        }
    }
}
