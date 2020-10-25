package unsw.gloriaromanus;

import java.io.Serializable;

import org.json.JSONObject;

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

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return armour + defenceSkill + shieldDefence;
    }

    public void removeNumTroops(int numRemove) {
        numTroops -= num;
    }

    
}
