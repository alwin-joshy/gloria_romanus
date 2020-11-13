package unsw.gloriaromanus;

public class Smith extends Infrastructure{
    
    public Smith() {
        setBaseContructionTime(2);
        setBaseCost(60);
        setName("Smith");
    }

    public void levelUp() {
        updateCosts();
        
    }
}
