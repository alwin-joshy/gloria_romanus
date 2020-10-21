package unsw.gloriaromanus;

public abstract class Infrastructure implements Project{
    private Faction fn; 
    private int baseCost;
    private int baseConstructionTime;
    private int level;

    public setLevel(int level) {
        this.level = level;
    }

    public int getConstructionTime(){
        return baseConstructionTime;
    }
}   
