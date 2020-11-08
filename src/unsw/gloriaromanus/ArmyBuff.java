package unsw.gloriaromanus;

import java.util.ArrayList;

public class ArmyBuff {
    private int numDruids;
    private double druidMultiplier;
    private int numLegionary;
    private boolean heroicCharge;
    private ArrayList<Unit> army;
    private Faction f;
    private Province p;

    public ArmyBuff(ArrayList<Unit> army, Province p) {
        this.army = army;
        this.p = p;
        this.f = p.getFaction();
        initArmyBuffs();
        resetEngagementCount();
        setTaxDebuff();

    }
    
    public int countNumUnit(String name) {
        int count = 0;;
        for (Unit u : army) {
            if (u.getName().equals(name)) count++;
        }
        return count;
    }

    public void initArmyBuffs() {
        numDruids = 0;
        numLegionary = 0;
        if (f.getName().equals("Spain")) {
            numDruids = countNumUnit("druid");
        } else if (f.getName().equals("Rome")) {
            numLegionary = countNumUnit("legionary");
        }


    }

    private void resetEngagementCount() {
        for (Unit u : army) {
            u.resetUnit();
        }
    }

    public void druidKilled(int numEnemyDruids) {
        numDruids--;
        calculateDruidMultiplier(numEnemyDruids);
    }

    public void legionaryKilled(boolean defending) {
        numLegionary--;
        if (defending) p.incrementNumLegionaryDeaths();
    }

    public boolean getHeroicCharge() {
        return heroicCharge;
    }

    public int getLegionaryBuff() {
        return numLegionary;
    }

    public void setHeroicCharge(int enemyArmySize) {
        heroicCharge = false; 
        if (enemyArmySize > 2 * army.size()) heroicCharge = true;
    }


    public void calculateDruidMultiplier(int numEnemyDruids) {
        int effectiveAlly = numDruids;
        int effectiveEnemy = numEnemyDruids;
        if (effectiveAlly > 5) effectiveAlly = 5;
        if (effectiveEnemy > 5) effectiveEnemy = 5;
        druidMultiplier = 1.0 + effectiveAlly * 0.1 - effectiveEnemy * 0.05;
    }

    public double getDruidMultiplier() {
        return druidMultiplier;
    }

    public int getNumDruids() {
        return numDruids;
    }

    private void setTaxDebuff() {
        boolean isVeryHighTax = p.isVeryHighTax();
        for (Unit u : army) {
            u.setTaxDebuff(isVeryHighTax);
        }
    }


}
