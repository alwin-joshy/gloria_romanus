package unsw.gloriaromanus;

public class Smith extends Infrastructure{
    
    public Smith() {
        setBaseContructionTime(2);
        setBaseCost(60);
        setName("Smith");
        setDescription("Each Smith upgrade will give in-battle buff to troops depending on level; \nLevel 1: Upgraded Helmets \nLevel 2: Upgraded Armour Suits \nLevel 3: Upgraded Weapons \nLevel 4: Fire Arrows for Archers ");
    }

    public void levelUp() {
        updateCosts();
        
    }
}
