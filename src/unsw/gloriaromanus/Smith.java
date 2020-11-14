package unsw.gloriaromanus;

public class Smith extends Infrastructure{
    
    public Smith() {
        setBaseContructionTime(2);
        setBaseCost(60);
        setName("Smith");
        setDescription("Each Smith upgrade will give in-battle buff to troops depending on level; \nLevel 1: EnemyDamageDebuff >= 1  \nLevel 2: EnemyDamageDebuff-0.5 >= 1 + AlliedSpeedBuff0.8  \n Level 3: AlliedAttackBuff*1.2 + AlliedMoraleBuff 1.1 \n Level 4: EnemyMoraleDebuff = 0.8 + EnemyMissileDebuff-10%.");
    }

    public void levelUp() {
        updateCosts();
        
    }
}
