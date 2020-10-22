package unsw.gloriaromanus;

import java.io.Serializable;

public abstract class Infrastructure implements Project, Serializable{
    private int baseCost;
    private int baseConstructionTime;
    private int level;
    private Faction f;

    public Infrastructure() {
        level = 0;
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

    public int getConstructionTime() {
        int constructionTime = baseConstructionTime - f.getMineTurnReduction();
        return constructionTime >= 1 ? constructionTime : 1;
    }

    public int getBaseConstructionTime(){
        return baseConstructionTime;
    }
}   
