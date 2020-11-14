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
        // String content = "{\r\n    \"archer\": {\r\n        \"type\": \"missile infantry\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": true,\r\n        \"armour\": 3,\r\n        \"morale\": 5,\r\n        \"speed\": 3,\r\n        \"attack\": 5,\r\n        \"defenceSkill\": 3,\r\n        \"shieldDefence\": 3,\r\n        \"baseCost\": 50,\r\n        \"trainingTime\": 2,\r\n        \"movementPoints\": 10\r\n    },\r\n    \"horseman\": {\r\n        \"type\": \"cavalry\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": false,\r\n        \"armour\": 2,\r\n        \"morale\": 3,\r\n        \"speed\": 4,\r\n        \"attack\": 2,\r\n        \"chargeValue\": 2,\r\n        \"defenceSkill\": 2,\r\n        \"shieldDefence\": 3,\r\n        \"baseCost\": 35,\r\n        \"trainingTime\": 1,\r\n        \"movementPoints\": 15\r\n    },\r\n    \"catapult\": {\r\n        \"type\": \"artillery\",\r\n   \"towerDamage\":10,\r\n     \"numTroops\": 30,\r\n        \"ranged\": true,\r\n        \"armour\": 4,\r\n        \"morale\": 6,\r\n        \"speed\": 3,\r\n        \"attack\": 11,\r\n        \"defenceSkill\": 4,\r\n        \"shieldDefence\": 5,\r\n        \"baseCost\": 50,\r\n        \"trainingTime\": 2,\r\n        \"movementPoints\": 4\r\n    },\r\n    \"peasant\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": false,\r\n        \"armour\": 3,\r\n        \"morale\": 3,\r\n        \"speed\": 2,\r\n        \"attack\": 3,\r\n        \"defenceSkill\": 2,\r\n        \"shieldDefence\": 4,\r\n        \"baseCost\": 35,\r\n        \"trainingTime\": 1,\r\n        \"movementPoints\": 10\r\n    },\r\n    \"axeman\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 40,\r\n        \"ranged\": false,\r\n        \"armour\": 5,\r\n        \"morale\": 9,\r\n        \"speed\": 7,\r\n        \"attack\": 14,\r\n        \"defenceSkill\": 5,\r\n        \"shieldDefence\": 6,\r\n        \"baseCost\": 200,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"beserker\": {\r\n        \"type\": \"infantry\",\r\n        \"numTroops\": 25,\r\n        \"ranged\": false,\r\n        \"armour\": 0,\r\n        \"morale\": 30,\r\n        \"speed\": 8,\r\n        \"attack\": 22,\r\n        \"defenceSkill\": 0,\r\n        \"shieldDefence\": 0,\r\n        \"baseCost\": 190,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"cannon\": {\r\n        \"type\": \"artillery\",\r\n        \"numTroops\": 50,\r\n    \"towerDamage\":20,\r\n    \"ranged\": true,\r\n        \"armour\": 11,\r\n        \"morale\": 9,\r\n        \"speed\": 7,\r\n        \"attack\": 20,\r\n        \"defenceSkill\": 10,\r\n        \"shieldDefence\": 10,\r\n        \"baseCost\": 400,\r\n        \"trainingTime\": 4\r\n    },\r\n    \"crossbowman\": {\r\n        \"type\": \"missile infantry\",\r\n        \"numTroops\": 50,\r\n        \"ranged\": true,\r\n        \"armour\": 10,\r\n        \"morale\": 9,\r\n        \"speed\": 10,\r\n        \"attack\": 20,\r\n        \"defenceSkill\": 10,\r\n        \"shieldDefence\": 10,\r\n        \"baseCost\": 400,\r\n        \"trainingTime\": 4\r\n    },\r\n    \"druid\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 3,\r\n        \"ranged\": true,\r\n        \"armour\": 8,\r\n        \"morale\": 7,\r\n        \"speed\": 11,\r\n        \"attack\": 9,\r\n        \"defenceSkill\": 6,\r\n        \"shieldDefence\": 5,\r\n        \"baseCost\": 220,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"elephant\": {\r\n        \"type\": \"cavalry\",\r\n        \"numTroops\": 8,\r\n        \"ranged\": false,\r\n        \"armour\": 19,\r\n        \"morale\": 7,\r\n        \"speed\": 13,\r\n        \"attack\": 13,\r\n        \"chargeValue\": 10,\r\n        \"defenceSkill\": 18,\r\n        \"shieldDefence\": 18,\r\n        \"baseCost\": 410,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"elitecavalry\": {\r\n        \"type\":\"cavalry\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": false,\r\n        \"armour\": 4,\r\n        \"morale\": 7,\r\n        \"speed\": 6,\r\n        \"attack\": 4,\r\n        \"chargeValue\": 3,\r\n        \"defenceSkill\": 4,\r\n        \"shieldDefence\": 3,\r\n        \"baseCost\": 70,\r\n        \"trainingTime\": 1\r\n    },\r\n    \"elitehorsearcher\": {\r\n        \"type\": \"horse archer\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": true,\r\n        \"armour\": 6,\r\n        \"morale\": 7,\r\n        \"speed\": 9,\r\n        \"attack\": 9,\r\n        \"defenceSkill\": 4,\r\n        \"shieldDefence\": 5,\r\n        \"baseCost\": 85,\r\n        \"trainingTime\": 2\r\n    },\r\n    \"hoplite\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 40,\r\n        \"ranged\": false,\r\n        \"armour\": 11,\r\n        \"morale\": 8,\r\n        \"speed\": 10,\r\n        \"attack\": 15,\r\n        \"defenceSkill\": 9,\r\n        \"shieldDefence\": 9,\r\n        \"baseCost\": 360,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"horsearcher\": {\r\n        \"name\": \"horsearcher\",\r\n        \"type\": \"horse archer\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": true,\r\n        \"armour\": 4,\r\n        \"morale\": 5,\r\n        \"speed\": 6,\r\n        \"attack\": 5,\r\n        \"defenceSkill\": 2,\r\n        \"shieldDefence\": 4,\r\n        \"baseCost\": 50,\r\n        \"trainingTime\": 2\r\n    },\r\n    \"immortal\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 50,\r\n        \"ranged\": false,\r\n        \"armour\": 13,\r\n        \"morale\": 10,\r\n        \"speed\": 13,\r\n        \"attack\": 18,\r\n        \"defenceSkill\": 13,\r\n        \"shieldDefence\": 13,\r\n        \"baseCost\": 320,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"javelinist\": {\r\n        \"type\": \"missile infantry\",\r\n        \"numTroops\": 40,\r\n        \"ranged\": true,\r\n        \"armour\": 11,\r\n        \"morale\": 8,\r\n        \"speed\": 12,\r\n        \"attack\": 18,\r\n        \"defenceSkill\": 11,\r\n        \"shieldDefence\": 9,\r\n        \"baseCost\": 385,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"knight\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 50,\r\n        \"ranged\": false,\r\n        \"armour\": 12,\r\n        \"morale\": 9,\r\n        \"speed\": 9,\r\n        \"attack\": 18,\r\n        \"defenceSkill\": 11,\r\n        \"shieldDefence\": 12,\r\n        \"baseCost\": 400,\r\n        \"trainingTime\": 4\r\n    },\r\n    \"lancer\": {\r\n        \"type\": \"cavalry\",\r\n        \"numTroops\": 50,\r\n        \"ranged\": false,\r\n        \"armour\": 12,\r\n        \"morale\": 9,\r\n        \"speed\": 12,\r\n        \"attack\": 15,\r\n        \"chargeValue\": 10,\r\n        \"defenceSkill\": 9,\r\n        \"shieldDefence\": 9,\r\n        \"baseCost\": 400,\r\n        \"trainingTime\": 4\r\n    },\r\n    \"legionary\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 50,\r\n        \"ranged\": false,\r\n        \"armour\": 13,\r\n        \"morale\": 9,\r\n        \"speed\": 9,\r\n        \"attack\": 15,\r\n        \"defenceSkill\": 12,\r\n        \"shieldDefence\": 14,\r\n        \"baseCost\": 400,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"netman\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 15,\r\n        \"ranged\": false,\r\n        \"armour\": 8,\r\n        \"morale\": 7,\r\n        \"speed\": 7,\r\n        \"attack\": 12,\r\n        \"defenceSkill\": 5,\r\n        \"shieldDefence\": 5,\r\n        \"baseCost\": 200,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"pikeman\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 40,\r\n        \"ranged\": false,\r\n        \"armour\": 8,\r\n        \"morale\": 7,\r\n        \"speed\": 7,\r\n        \"attack\": 11,\r\n        \"defenceSkill\": 7,\r\n        \"shieldDefence\": 6,\r\n        \"baseCost\": 200,\r\n        \"trainingTime\": 3\r\n    },\r\n    \"slingerman\": {\r\n        \"name\": \"slingerman\",\r\n        \"type\": \"missile infantry\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": true,\r\n        \"armour\": 2,\r\n        \"morale\": 3,\r\n        \"speed\": 4,\r\n        \"attack\": 3,\r\n        \"defenceSkill\": 1,\r\n        \"shieldDefence\": 3,\r\n        \"baseCost\": 35,\r\n        \"trainingTime\": 1\r\n    },\r\n    \"spearman\": {\r\n        \"type\": \"spearman\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": false,\r\n        \"armour\": 5,\r\n        \"morale\": 5,\r\n        \"speed\": 4,\r\n        \"attack\": 5,\r\n        \"defenceSkill\": 5,\r\n        \"shieldDefence\": 6,\r\n        \"baseCost\": 50,\r\n        \"trainingTime\": 2\r\n    },\r\n    \"swordsman\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 30,\r\n        \"ranged\": false,\r\n        \"armour\": 5,\r\n        \"morale\": 5,\r\n        \"speed\": 5,\r\n        \"attack\": 6,\r\n        \"defenceSkill\": 4,\r\n        \"shieldDefence\": 5,\r\n        \"baseCost\": 50,\r\n        \"trainingTime\": 2\r\n    },\r\n    \"trebuchet\": {\r\n        \"type\": \"artillery\",\r\n        \"numTroops\": 40,\r\n    \"towerDamage\":15,\r\n    \"ranged\": true,\r\n        \"armour\": 7,\r\n        \"morale\": 7,\r\n        \"speed\": 5,\r\n        \"attack\": 15,\r\n        \"defenceSkill\": 8,\r\n        \"shieldDefence\": 8,\r\n        \"baseCost\": 200,\r\n        \"trainingTime\": 3\r\n    }\r\n}\r\n";
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
    
    public void reduceRemainingMovementPoints(int movementPoints) {
        movementPointsRemaining -= movementPoints;
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

    /*
     * You should ensure the ranged attack damage above incorporates the effect of
     * any bonuses/penalties (e.g. the 10% loss of missile attack damage from fire
     * arrows).
     * 
     * NOTE: in the above formula, the Berserker special ability will result in a
     * Zero Division Error.
     * 
     * Handle this by capping the following to 10 (rather than the infinity implied
     * by zero division error): Missile attack damage of unit/(effective armor of
     * enemy unit + effective shield of enemy unit)
     * 
     * Melee cavalry/chariots/elephants will have an attack damage value in all
     * engagements equal to their melee attack damage + charge value. Infantry and
     * artillery do not receive a charge statistic (only cavalry/chariots/elephants
     * do).
     * 
     * Note that all effective attributes in the formula should incorporate the
     * effect of any bonuses/penalties (such as formations such as phalanx
     * formation, charge bonuses where applicable for cavalry/chariots/elephants).
     */

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

    public void checkIfBroken(int casualties, int size, int enemyCasualties, int enemySize, ArmyBuff allyBuff, double legionaryDebuff, double smithDebuff, Random random) {
        if (isBroken)
            return;

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
