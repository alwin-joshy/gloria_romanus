package unsw.gloriaromanus;

import java.io.Serializable;

public abstract class Infrastructure implements Project, Serializable {
    private int baseCost;
    private int baseConstructionTime;
    private int level;
    private String name;

    public Infrastructure() {
        level = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public abstract void levelUp();

    public void updateCosts() {
        if (level == 4) return;
        level++;
        baseConstructionTime *= 2;
        baseCost *= (int) (level * 2.5);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setBaseCost(int cost) {
        baseCost = cost;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setBaseContructionTime(int turns) {
        baseConstructionTime = turns;
    }

    public int getBaseConstructionTime(){
        return baseConstructionTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        Infrastructure i = (Infrastructure) obj;
        return baseCost == i.getBaseCost() && baseConstructionTime == i.getBaseConstructionTime() &&
               level == i.getLevel();
    }
}   
