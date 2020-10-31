package unsw.gloriaromanus;

public class Smith extends Infrastructure{
    
    public Smith(int factionBonus) {
        super(factionBonus);
    }

    public void levelUp() {
        updateCosts();
        
    }
}
