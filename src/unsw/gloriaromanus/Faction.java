package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;

public class Faction implements Serializable{
    
    private ArrayList<Province> alliedProvinces;
    private int treasury;
    private String name;
    private int totalProvinceWealth;
    private Boolean isPlayer;
    private Double mineMultiplier;
    private int mineTurnReduction;
    private Double marketMultiplier;
    private int portBonus;


    public Faction(String name) {
        this.name = name;
        alliedProvinces = new ArrayList<Province>();
        treasury = 100;
        totalProvinceWealth = 0;
        isPlayer = false;
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
        mineMultiplier = 1.0;
        for (Province p : alliedProvinces) {
            Mine m = p.getMine();
            if (m != null){
                mineMultiplier *= m.getMultiplier();
            }
        } 
    }

    public void calculateMarketMultiplier() {
        marketMultiplier = 1.0;
        for (Province p : alliedProvinces) {
            Market m = p.getMarket();
            if (m != null) {
                marketMultiplier *= m.getMultiplier();
                if (m.getLevel() == 4) {
                    mineTurnReduction++;
                }
            }
        }
    }

    public void calculatePortBonus() {
        portBonus = 0;
        for (Province p : alliedProvinces) {
            Port pt = p.getPort();
            if (pt != null) {
                portBonus += pt.getBonus();
            }
        }
    }

    public String getName(){
        return name;
    }
    
    public int getPortBonus() {
        return portBonus;
    }

    public int getMineTurnReduction(){
        return mineTurnReduction;
    }

    public double getMarketMultiplier() {
        return marketMultiplier;
    }

    public double getMineMultiplier() {
        return mineMultiplier;
    }

    public void setPlayer() {
        isPlayer = true;
    }

    public int getTreasury() {
        return treasury;
    }

    public int getWealth() {
        int wealth = 0;
        for (Province p : alliedProvinces) {
            wealth += p.getWealth();
        }
        return wealth;
    }

    public int getNumProvinces() {
        return alliedProvinces.size();
    }

    public boolean checkMaxedInfrastructure() {
        for (Province p : alliedProvinces) {
            if (!p.maxedInfrastructure()) return false; 
        }
        return true;
    }

    public boolean purchase(int cost) {
        if (treasury - cost < 0) return false;
        treasury -= cost;
        return true;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void moveUnits(ArrayList<Unit> units, Province start, Province end, int distance) {
        for (Unit u : units) {
            if (! u.canMove(distance)) return;
        }

        for (Unit u : units) {
            start.removeUnit(u);
            end.addUnit(u);
        }
    }

    public boolean isAlliedProvince(String p) {
        for (Province ap : alliedProvinces) {
            if (ap.getName().equals(p)) return true;
        }
        return false;
    }

}

