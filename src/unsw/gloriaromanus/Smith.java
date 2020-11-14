package unsw.gloriaromanus;

public class Smith extends Infrastructure{
    
    public Smith() {
        setBaseContructionTime(2);
        setBaseCost(60);
        setName("Smith");
        setDescription("Each Smith upgrade will give in-battle buff to troops depending on level; Level 1: EnemyDamageDebuff >= 1  Level 2: EnemyDamageDebuff-0.5 >= 1 + AlliedSpeedBuff0.8  Level 3: AlliedAttackBuff*1.2 + AlliedMoraleBuff 1.1  Level 4: EnemyMoraleDebuff = 0.8 + EnemyMissileDebuff-10%.");
    }

    public void levelUp() {
        updateCosts();
        
    }
}
