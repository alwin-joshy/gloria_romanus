package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TroopProductionBuilding extends Infrastructure{
    private static Map<K,V> <Integer, ArrayList<Unit>> generalUnits = new Map<Integer, ArrayList<Unit>>;
    private Map<Integer, ArrayList<Unit>> uniqueUnits;

    public TroopProductionBuilding() {
        setLevel(1);
        uniqueUnits = new HashMap<Integer, ArrayList<Unit>>();
    }

    public static void populateGeneralUnits(){
        ArrayList<Unit> levelOne = new ArrayList<Unit>(new Peasant(), new SlingerMan());
        generalUnits.put(1, levelOne);
        ArrayList<Unit> levelTwo = new ArrayList<Unit>(new Swordsman(), new Spearman(), new Archer(), new HorseArcher(), new SiegeTower());
        generalUnits.put(2, levelTwo);
        ArrayList<Unit> levelThree = new ArrayList<Unit>(new Trebuchet(), new Pikeman(), new Catapult(), new Netman(), new AxeMan());
        generalUnits.put(3, levelThree);
        ArrayList<Unit> levelFour = new ArrayList<Unit>(new Crossbowman(), new Cannon(), new Trebuchet(), new Knight());
        generalUnits.put(4, levelFour);
    }



}