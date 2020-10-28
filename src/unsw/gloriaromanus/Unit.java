package unsw.gloriaromanus;

import java.io.Serializable;

import java.lang.Math;
import java.util.Random;

import org.json.JSONObject;

import javafx.geometry.Side;

public class Unit implements Serializable {
    private String name;
    private String type;
    private int numTroops;  // the number of troops in this unit (should reduce based on depletion)
    private boolean ranged;  // range of the unit
    private int armour;  // armour defense
    private int morale;  // resistance to fleeing
    private int speed;  // ability to disengage from disadvantageous battle
    private int attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    private int defenceSkill;  // skill to defend in battle. Does not protect from arrows!
    private int shieldDefence; // a shield
    private int baseCost;
    private int trainingTime;
    private int smithLevel;
    private int movementPoints;
    private int movementPointsRemaining;
    private boolean isBroken;
    private boolean isRouted;

    public Unit(String name) {
        this.name = name;
        JSONObject json = new JSONObject("units/" + name + ".json");
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
    }

    public int compareTo(Object obj) {
        Unit u = (Unit) obj;
        if (checkType("spearmen")) {
            if (u.checkType("heavy infantry")) return 1;
            if (u.checkType("missile infantry")) return 2;
        }
        return 0;
    }

    public boolean checkType(String type) {
        return type.equals(this.type);
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isRanged() {
        return ranged;
    }

    public String getType() {
        return type;
    }

    public int getNumTroops(){
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
        return movementPointsRemaining >= distance;
    }

    public int getMeleeDefense() {
        return defenceSkill + shieldDefence + armour;
    }

    public int getRangedDefense() {
        return armour + shieldDefence;
    }

    /*
    You should ensure the ranged attack damage above incorporates the effect of any bonuses/penalties 
    (e.g. the 10% loss of missile attack damage from fire arrows).
    NOTE: in the above formula, the Berserker special ability will result in a Zero Division Error. 
    Handle this by capping the following to 10 (rather than the infinity implied by zero division error):
    Missile attack damage of unit/(effective armor of enemy unit + effective shield of enemy unit)
    Melee cavalry/chariots/elephants will have an attack damage value in all engagements equal to 
    their melee attack damage + charge value. Infantry and artillery do not receive a charge statistic (only cavalry/chariots/elephants do).
    Note that all effective attributes in the formula should incorporate the effect of any bonuses/penalties 
    (such as formations such as phalanx formation, charge bonuses where applicable for cavalry/chariots/elephants).
    */

    public int calculateDamage(Unit enemyUnit, boolean isMeleeEngagement) {
        double damage;
        Random random = new Random();
        if (isMeleeEngagement) {
            damage = enemyUnit.getNumTroops() * 0.1 * (attack / (enemyUnit.getMeleeDefense())) * (random.nextGaussian() + 1);
        } else {
            damage = enemyUnit.getNumTroops() * 0.1 * (attack / (enemyUnit.getRangedDefense())) * (random.nextGaussian() + 1);
        }
        int roundedDamage = (int) Math.round(damage);
        return roundedDamage;
    }

    public void takeDamage(int damage) {
        numTroops -= damage;
        if (numTroops < 0) numTroops = 0;
    }

    public void checkIfBroken(int casualties, int size, int enemyCasualties, int enemySize) {
        if (isBroken) return;
        Random random = new Random();
        double breakChance = 100 - (morale * 10) + (casualties / size) / (enemyCasualties / enemySize) * 10;
        if (breakChance < 5)
            breakChance = 5;
        else if (breakChance > 100)
            breakChance = 100;
        int b = random.nextInt();
        if (b < breakChance - 1) isBroken = true;
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

    public boolean isDefeatedOrRouted() {
        return isRouted || (numTroops == 0);
    }
    
}
