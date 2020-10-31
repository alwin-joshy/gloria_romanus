package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONObject;

public class TroopProductionBuilding extends Infrastructure {
    Faction f;
    private static Map<Integer, ArrayList<String>> units;

    public TroopProductionBuilding(Faction f) throws IOException{
        super(f.getMineTurnReduction());
        this.f = f;
        this.setBaseContructionTime(1);
        this.setBaseCost(40);
        units = new HashMap<Integer, ArrayList<String>>();
        String content = "[\r\n\t[\"peasant\", \"horseman\", \"slingerman\"],\r\n  \t[\"archer\", \"catapult\", \"horsearcher\", \"spearman\", \"swordsman\"],\r\n  \t[\"axeman\", \"netman\", \"pikeman\", \"trebuchet\"],\r\n  \t[\"cannon\", \"crossbowman\", \"knight\", \"lancer\"]\r\n]";
        // This wont work with the test file ;-;
        //String content = Files.readString(Paths.get("src/unsw/gloriaromanus/units/units.json"));
        JSONArray unitList = new JSONArray(content);
        int countLevel = 1;
        // converts json file containing general units into a hashmap
        for (int i = 0; i < unitList.length(); i++) {
            JSONArray jsonUnits = unitList.getJSONArray(i);
            ArrayList<String> unitsSubList = ArrayUtil.convert(jsonUnits);
            units.put(countLevel, unitsSubList);
            countLevel++;
        }
        //content = Files.readString(Paths.get("src/unsw/gloriaromanus/units/uniqueUnits.json"));
        content = "{\r\n    \"Rome\": { \"level\": 3, \"name\": \"legionaire\" },\r\n    \"Carthaginians\" : { \"level\": 3, \"name\": \"elephant\" },\r\n    \"Gaul\" : { \"level\": 2, \"name\": \"berserker\" },\r\n    \"Celtic Britons\" : { \"level\": 2, \"name\": \"berserker\" },\r\n    \"Spanish\" : { \"level\": 3, \"name\": \"druid\" },\r\n    \"Numidians\" : { \"level\": 1, \"name\": \"eliteCavalry\" },\r\n    \"Egyptians\" : { \"level\": 2, \"name\": \"eliteHorseArcher\" },\r\n    \"Seleucid Empire\" : { \"level\": 3, \"name\": \"immortals\" }, \r\n    \"Pontus\" : { \"level\": 3, \"name\": \"hoplites\" },\r\n    \"Amenians\" : { \"level\": 1, \"name\": \"eliteCavalry\"},\r\n    \"Parthians\" : { \"level\": 2, \"name\": \"eliteHorseArcher\" },\r\n    \"Germanics\" : { \"level\": 2, \"name\": \"berserker\" },\r\n    \"Greek City States\" : { \"level\": 3, \"name\": \"hoplites\" },\r\n    \"Macedonians\" : { \"level\": 3, \"name\": \"hoplites\" },\r\n    \"Thracians\" : { \"level\": 3, \"name\": \"javelinist\" },\r\n    \"Dacians\" : { \"level\": 3, \"name\": \"javenlinist\" }\r\n}";
        JSONObject uniqueUnits = new JSONObject(content);
        JSONObject unit = uniqueUnits.getJSONObject(f.getName());
        // adds the unique unit to the list
        units.get(unit.getInt("level")).add(unit.getString("name"));

    }

    public static void main(String[] args) {
        try {
            new TroopProductionBuilding(new Faction("Romans"));
        } catch (IOException e) {
            System.out.println("Config file missing"); // This should probably be checked in initialization
            System.exit(1);
        }
    }

    public void levelUp() {
        updateCosts();
    }

    public boolean isAvailable(Unit u) {
        for (int i = 1; i <= getLevel(); i++) {
            if (units.get(i).contains(u.getName())) return true;
        }
        return false;
    }

    // TODO Change this in iteration 3
    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()) return true;
        return false;
    }

}