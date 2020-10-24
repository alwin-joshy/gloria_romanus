package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TroopProductionBuilding extends Infrastructure{
    private Faction f;
    private static Map<Integer, ArrayList<String>> generalUnits = new HashMap<Integer, ArrayList<String>>();
    private Map<Integer, ArrayList<String>> uniqueUnits;

    public TroopProductionBuilding(Faction f) {
        this.f = f;
        uniqueUnits = new HashMap<Integer, ArrayList<String>>();
    }

    public static void populateGeneralUnits(){
        ArrayList<String> levelOne = new ArrayList<String>(Arrays.asList("peasant", "slingerman", "horseman"));
        generalUnits.put(1, levelOne);
        ArrayList<String> levelTwo = new ArrayList<String>(Arrays.asList("swordsman", "spearman", "archer", "horsearcher", "catapult"));
        generalUnits.put(2, levelTwo);
        ArrayList<String> levelThree = new ArrayList<String>(Arrays.asList("trebuchet", "pikeman", "netman", "axeman"));
        generalUnits.put(3, levelThree);
        ArrayList<String> levelFour = new ArrayList<String>(Arrays.asList("crossbowman", "cannon", "knight", "lancer"));
        generalUnits.put(4, levelFour);
    }

    public void levelUp() {
        updateCosts();
    }

}