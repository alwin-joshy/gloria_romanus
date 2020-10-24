package unsw.gloriaromanus;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers, heavy cavalry, elephants, chariots, archers, slingers, horse-archers, onagers, ballista, etc...
 * higher classes include ranged infantry, cavalry, infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent armour and morale)
 */
public abstract class Unit implements Serializable{
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

    public Unit(String filename){
        // TODO = obtain these values from the file for the unit
        JSONObject json = new JSONObject("units/" + filename + ".json");
        numTroops = json.getInt("numTroops");
        ranged = json.getBoolean("ranged");
        armour = json.getInt("armour");
        morale = json.getInt("morale");
        speed = json.getInt("speed");
        attack = json.getInt("attack");
        defenceSkill = json.getInt("defenceSkill");
        shieldDefence = json.getInt("shieldDefence");
        baseCost = json.getInt("baseCost");
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

    
}
