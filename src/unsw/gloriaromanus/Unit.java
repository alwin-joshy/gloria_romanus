package unsw.gloriaromanus;

import java.io.IOException;
import java.io.Serializable;

import java.lang.Math;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

import org.json.JSONObject;

public class Unit implements Serializable, Project {
    private String name;
    private String type;
    private int numTroops; // the number of troops in this unit (should reduce based on depletion)
    private boolean ranged; // range of the unit
    private int armour; // armour defense
    private double morale; // resistance to fleeing
    private int speed; // ability to disengage from disadvantageous battle
    private int attack; // can be either missile or melee attack to simplify. Could improve
                        // implementation by differentiating!
    private int chargeValue;
    private int defenceSkill; // skill to defend in battle. Does not protect from arrows!
    private int shieldDefence; // a shield
    private int baseCost;
    private int trainingTime;
    private int movementPoints;
    private int movementPointsRemaining;
    private int towerDamage; 
    private boolean isBroken;
    private int taxDebuff;
    private int engagementCount;
    private SmithLevel smithBuff;

    public Unit(String name) {
        this.name = name;
        String content = "";
        try {
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/units/units.json"));
        } catch (IOException e) {
            System.out.println("Units config file not found!!");
            e.printStackTrace();
        }
        JSONObject units = new JSONObject(content);
        JSONObject json = units.getJSONObject(name);

        if (Arrays.asList("elephant", "horseman", "elite cavalry", "lancer").contains(name))
            chargeValue = json.getInt("chargeValue");
        else 
            chargeValue = 0;
        
        type = json.getString("type");


        if (type.equals("artillery"))
            towerDamage = json.getInt("towerDamage");
        else
            towerDamage = 0;


        numTroops = json.getInt("numTroops");
        ranged = json.getBoolean("ranged");
        armour = json.getInt("armour");
        morale = json.getInt("morale");
        speed = json.getInt("speed");
        attack = json.getInt("attack");
        defenceSkill = json.getInt("defenceSkill");
        shieldDefence = json.getInt("shieldDefence");
        baseCost = json.getInt("baseCost");
        trainingTime = json.getInt("trainingTime");
        if (type.equals("cavalry") || type.equals("horse archer")) movementPoints = 15;
        else if (type.equals("artillery")) movementPoints = 4;
        else movementPoints = 10;
        movementPointsRemaining = movementPoints;
        isBroken = false;
        taxDebuff = 0;
        engagementCount = 0;
        smithBuff = new SmithLevelZero();
    }

    public String getName() {
        return name;
    }

    public boolean checkType(String type) {
        return type.equals(this.type);
    }

    public void setSmithLevel(SmithLevel smithBuff) {
        this.smithBuff = smithBuff;
    }

    public double getSpeed() {
        return smithBuff.applySpeedDebuff(speed);
    }

    public boolean isRanged() {
        return ranged;
    }

    public String getType() {
        return type;
    }

    public int getNumTroops() {
        return numTroops;
    }

    public int getBaseConstructionTime() {
        return trainingTime;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public int getSmithLevel() {
        return smithBuff.getLevel();
    }
    
    public void reduceRemainingMovementPoints(int movementPoints) {
        movementPointsRemaining -= movementPoints;
        if (movementPointsRemaining < 0) {
            movementPointsRemaining = 0;
        }
    }

    public int getMovementPointsRemaining() {
        return movementPointsRemaining;
    }

    public Boolean canMove(int distance) {
        return (movementPointsRemaining >= distance);
    }

    public int getMeleeDefence() {
        int meleeDefence = defenceSkill + shieldDefence + armour;
        if (name.equals("pikemen") || name.equals("hoplite"))
            meleeDefence *= 2;
        return meleeDefence;
    }

    public double getRangedDefence(boolean vsJavelinist) {
        double adjustedArmour = (double) armour;
        if (vsJavelinist) adjustedArmour /= 2;
        return adjustedArmour + shieldDefence;
    }

    public int calculateDamage(Unit enemyUnit, boolean isRangedEngagement, boolean heroicCharge, boolean wallsDebuff, Random random) {
        double damage;
        if (isRangedEngagement) {
            if (!ranged) return 0;

            double damageQuotient = enemyUnit.getName().equals("beserker") ? 10 : enemyUnit.getSmithReducedDamage(getAttack()) / (double) (enemyUnit.getRangedDefence(name.equals("javelinist")));
            if (wallsDebuff && name.equals("horsearcher")) {
                damageQuotient /= attack; // reduce the missile attack damage of attacking horse archers to 1.
            }
            else if (wallsDebuff && name.equals("elitehorsearcher")) {
                damageQuotient /= attack;
                damageQuotient *= 2;
            } else if (wallsDebuff && name.equals("archer")) {
                damageQuotient /= 2; //reduce the missile attack damage of attacking archers by 50% (to a minimum of 1),
            }

            if (enemyUnit.getType().equals("horse archer")) damageQuotient /= 2;
            
            damage = enemyUnit.getNumTroops() * 0.1 * damageQuotient;
        } else {
            damage = enemyUnit.getNumTroops() * 0.1 * ((enemyUnit.getSmithReducedDamage(getAttack()) + (double) getChargeValue(heroicCharge, wallsDebuff)) / ((double) enemyUnit.getMeleeDefence()));
            if (wallsDebuff && ! enemyUnit.getType().equals("artillery")) damage *= 0.5; // Walls double the melee defence of all troops defending a settlement (except when fighting artillery),
        }
        damage = damage * (random.nextGaussian() + 1);
        int roundedDamage = (int) Math.round(damage);
        if (roundedDamage < 0) roundedDamage = 0;
        if (roundedDamage > enemyUnit.getNumTroops()) roundedDamage = enemyUnit.getNumTroops();
        return roundedDamage;
    }

    public void takeDamage(int damage) {
        numTroops -= damage;
        if (numTroops < 0)
            numTroops = 0;
    }

    public boolean checkIfBroken(int casualties, int size, int enemyCasualties, int enemySize, ArmyBuff allyBuff, double legionaryDebuff, double smithDebuff, Random random) {
        if (isBroken)
            return false;

        double heroicChargeMultiplier = 1.0;
        if (allyBuff.getHeroicCharge() && type.equals("cavalry") && !ranged) 
            heroicChargeMultiplier = 1.5;

        
        double finalMorale = (morale + (double) allyBuff.getLegionaryBuff() - (double) taxDebuff - legionaryDebuff) * allyBuff.getDruidMultiplier() * smithDebuff;
        if (finalMorale < 1) 
            finalMorale = 1;

        double breakChance = 1.0 - finalMorale * heroicChargeMultiplier * 0.1;
        if (enemyCasualties != 0) {
            breakChance += (((double) casualties / (double) size) / ((double) enemyCasualties / (double) enemySize)) * 0.1;
        }

        if (breakChance < 0.05)
            breakChance = 0.05;
        else if (breakChance > 1.0)
            breakChance = 1.0;

        double b = random.nextDouble();
        if (b < breakChance) {
            isBroken = true;
        }
        return isBroken;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public boolean isDefeated() {
        return numTroops == 0;
    }

    public void setTaxDebuff(boolean isVeryHighTax) {
        taxDebuff = isVeryHighTax ? 1 : 0;
    }

    public int getArmour() {
        return armour;
    }

    public double getMorale() {
        return morale;
    }

    public int getDefenceSkill() {
        return defenceSkill;
    }

    public int getShieldDefence() {
        return shieldDefence;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public int getTaxDebuff() {
        return taxDebuff;
    }

    public double getSmithReducedDamage(double damage) {
        return smithBuff.applyEnemyAttackDebuff(damage);
    }

    public double getSmithMoraleDebuff() {
        if (name.equals("archer")) return smithBuff.getEnemyMoraleDebuff();
        return 1.0;
    }

    public double getAttack() {
        int countShieldCharge = engagementCount % 4;
        double adjustedAttack = attack;
        if (countShieldCharge == 0 && type.equals("heavy infantry")) adjustedAttack += shieldDefence;
        adjustedAttack = smithBuff.applyAttackBuff(adjustedAttack);
        if (name.equals("archer")) adjustedAttack = smithBuff.applyMissileDamageDebuff(adjustedAttack);
        return adjustedAttack;
    }

    public int getChargeValue(boolean heroicCharge, boolean wallsDebuff) {
        int adjustedChargeValue = chargeValue;
        if (heroicCharge) adjustedChargeValue *= 2;
        if (wallsDebuff) adjustedChargeValue = 0;
        return adjustedChargeValue;
    }

    public void incrementEngagementCount() {
        engagementCount++;
    }

    public void resetMovementPoints() {
        movementPointsRemaining = movementPoints;
    }

    public void resetUnit() {
        isBroken = false;
        engagementCount = 0;
    }

    public int getTowerDamage() {
		return towerDamage;
	}
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Unit u = (Unit) obj;
        return name.equals(u.getName()) && numTroops == u.getNumTroops() && ranged == u.isRanged() && armour == u.getArmour() &&
            morale == u.getMorale() && speed == u.getSpeed() && attack == u.getAttack() && defenceSkill == u.getDefenceSkill() &&
            shieldDefence == u.getShieldDefence() && baseCost == u.getBaseCost() && trainingTime == u.getTrainingTime() &&
            movementPoints == u.getMovementPoints() && movementPointsRemaining == u.getMovementPointsRemaining() &&
            isBroken == u.isBroken() && taxDebuff == u.getTaxDebuff() && chargeValue == u.getChargeValue(false, false);
    }

}
