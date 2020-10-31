package unsw.gloriaromanus;

import java.io.IOException;
import java.io.Serializable;

import java.lang.Math;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

import org.json.JSONObject;

import javafx.geometry.Side;

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
    private boolean isBroken;
    private boolean isRouted;
    private int taxDebuff;
    private int engagementCount;

    public Unit(String name) throws IOException{
        this.name = name;
        String content = "{\r\n    \"archer\": {\r\n        \"type\": \"missile infantry\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": true,\r\n        \"armour\": 3,\r\n        \"morale\": 5,\r\n        \"speed\": 5,\r\n        \"attack\": 5,\r\n        \"defenceSkill\": 3,\r\n        \"shieldDefence\": 3,\r\n        \"baseCost\": 50,\r\n        \"trainingTime\": 2,\r\n        \"movementPoints\": 10\r\n    },\r\n    \"horseman\": {\r\n        \"type\": \"cavalry\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": false,\r\n        \"armour\": 2,\r\n        \"morale\": 3,\r\n        \"speed\": 4,\r\n        \"attack\": 2,\r\n        \"chargeValue\": 2,\r\n        \"defenceSkill\": 2,\r\n        \"shieldDefence\": 3,\r\n        \"baseCost\": 35,\r\n        \"trainingTime\": 1,\r\n        \"movementPoints\": 15\r\n    },\r\n    \"catapult\": {\r\n        \"type\": \"artillery\",\r\n        \"numTroops\": 30,\r\n        \"ranged\": true,\r\n        \"armour\": 4,\r\n        \"morale\": 6,\r\n        \"speed\": 3,\r\n        \"attack\": 11,\r\n        \"defenceSkill\": 4,\r\n        \"shieldDefence\": 5,\r\n        \"baseCost\": 50,\r\n        \"trainingTime\": 2,\r\n        \"movementPoints\": 4\r\n    },\r\n    \"peasant\": {\r\n        \"type\": \"heavy infantry\",\r\n        \"numTroops\": 20,\r\n        \"ranged\": false,\r\n        \"armour\": 3,\r\n        \"morale\": 3,\r\n        \"speed\": 2,\r\n        \"attack\": 3,\r\n        \"defenceSkill\": 2,\r\n        \"shieldDefence\": 4,\r\n        \"baseCost\": 35,\r\n        \"trainingTime\": 1,\r\n        \"movementPoints\": 10\r\n    }\r\n}";
        //String content = Files.readString(Paths.get("src/unsw/gloriaromanus/units/", name, ".json"));
        JSONObject units = new JSONObject(content);
        JSONObject json = units.getJSONObject(name);
        if (Arrays.asList("elephant", "horseman", "elite cavalry", "lancer").contains(name))
            chargeValue = json.getInt("chargeValue");
        else 
            chargeValue = 0;
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
        movementPoints = json.getInt("movementPoints");
        movementPointsRemaining = movementPoints;
        isBroken = false;
        isRouted = false;
        taxDebuff = 0;
        engagementCount = 0;
    }

    public String getName() {
        return name;
    }

    public int compareTo(Object obj) {
        Unit u = (Unit) obj;
        if (checkType("spearmen")) {
            if (u.checkType("heavy infantry"))
                return 1;
            if (u.checkType("missile infantry"))
                return 2;
        }
        return 0;
    }

    public boolean checkType(String type) {
        return type.equals(this.type);
    }

    public int getSpeed() {
        if (name.equals("pikemen") || name.equals("hoplite"))
            return speed / 2;
        return speed;
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

    public int getConstructionTime() {
        return trainingTime;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
        this.movementPointsRemaining = movementPoints;
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

    public int calculateDamage(Unit enemyUnit, boolean isRangedEngagement, boolean heroicCharge) {
        double damage;
        Random random = new Random();
        double damageQuotient = enemyUnit.getName().equals("beserker") ? 10 : attack / (enemyUnit.getRangedDefence(name.equals("javelinist")));
        if (isRangedEngagement) {
            if (!ranged) return 0;
            if (name.equals("horsearcher") || name.equals("elitehorsearcher")) damageQuotient /= 2;
            damage = enemyUnit.getNumTroops() * 0.1 * damageQuotient;
        } else {
            damage = enemyUnit.getNumTroops() * 0.1 * ((getAttack() + getChargeValue(heroicCharge)) / (enemyUnit.getMeleeDefence()));
        }
        damage *= random.nextGaussian() + 1;
        int roundedDamage = (int) Math.round(damage);
        return roundedDamage;
    }

    public void takeDamage(int damage) {
        numTroops -= damage;
        if (numTroops < 0)
            numTroops = 0;
    }

    public void checkIfBroken(int casualties, int size, int enemyCasualties, int enemySize, double druidMultiplier, boolean heroicCharge, int legionaryCount, double legionaryDebuff) {
        if (isBroken)
            return;
        double heroicChargeMultiplier = 1.0;
        if (heroicCharge && type.equals("cavalry") && !ranged) heroicChargeMultiplier = 1.5;
        Random random = new Random();
        double finalMorale = (morale + legionaryCount - taxDebuff - legionaryDebuff) * druidMultiplier;
        if (finalMorale < 1) finalMorale = 1;
        double breakChance = 1.0 - finalMorale * heroicChargeMultiplier * 0.1 + (casualties / size) / (enemyCasualties / enemySize) * 0.1;
        if (breakChance < 0.05)
            breakChance = 0.05;
        else if (breakChance > 1.0)
            breakChance = 1.0;
        double b = random.nextDouble();
        if (b < breakChance)
            isBroken = true;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setAsRouted() {
        isRouted = true;
    }

    public boolean isRouted() {
        return isRouted;
    }

    public boolean isDefeated() {
        return numTroops == 0;
    }

    public void setTaxDebuff(int isHighTax) {
        taxDebuff = isHighTax;
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

    public int getAttack() {
        int countShieldCharge = engagementCount % 4;
        if (countShieldCharge == 0 && type.equals("heavy infantry")) return attack + shieldDefence;
        return attack;
    }

    public int getChargeValue(boolean heroicCharge) {
        int adjustedChargeValue = chargeValue;
        if (heroicCharge) adjustedChargeValue *= 2;
        return adjustedChargeValue;
    }

    public void incrementEngagementCount() {
        engagementCount++;
    }

    public void resetEngagementCount() {
        engagementCount = 0;
    }

    public void resetMovementPoints() {
        movementPointsRemaining = movementPoints;
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
            isBroken == u.isBroken() && isRouted == u.isRouted() && taxDebuff == u.getTaxDebuff() && chargeValue == u.getChargeValue(false);
    }
}
