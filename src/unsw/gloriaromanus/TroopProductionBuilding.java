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
    private Map<Integer, ArrayList<String>> units;

    public TroopProductionBuilding(Faction f){
        super();
        this.f = f;
        this.setBaseContructionTime(1);
        this.setBaseCost(40);
        setName("Troop Production Building");
        setDescription("Each Troop Production Building upgrade will allow for more advance troops for the faction to produce; Spearmen, Missile infantry, Melee cavalry");
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
        try {
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/units/uniqueUnits.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //content = "{\r\n    \"Rome\": { \"level\": 3, \"name\": \"legionary\" },\r\n    \"Carthage\" : { \"level\": 3, \"name\": \"elephant\" },\r\n    \"Gaul\" : { \"level\": 2, \"name\": \"berserker\" },\r\n    \"Britons\" : { \"level\": 2, \"name\": \"berserker\" },\r\n    \"Spain\" : { \"level\": 3, \"name\": \"druid\" },\r\n    \"Numidia\" : { \"level\": 1, \"name\": \"eliteCavalry\" },\r\n    \"Egypt\" : { \"level\": 2, \"name\": \"eliteHorseArcher\" },\r\n    \"Seleucid Empire\" : { \"level\": 3, \"name\": \"immortals\" }, \r\n    \"Pontus\" : { \"level\": 3, \"name\": \"hoplites\" },\r\n    \"Armenia\" : { \"level\": 1, \"name\": \"eliteCavalry\"},\r\n    \"Parthia\" : { \"level\": 2, \"name\": \"eliteHorseArcher\" },\r\n    \"Germanics\" : { \"level\": 2, \"name\": \"berserker\" },\r\n    \"Greek\" : { \"level\": 3, \"name\": \"hoplites\" },\r\n    \"Macedonia\" : { \"level\": 3, \"name\": \"hoplites\" },\r\n    \"Thracia\" : { \"level\": 3, \"name\": \"javelinist\" },\r\n    \"Dacia\" : { \"level\": 3, \"name\": \"javenlinist\" }\r\n}";
        JSONObject uniqueUnits = new JSONObject(content);
        if (! f.getName().equals("Rebel")) {
            JSONObject unit = uniqueUnits.getJSONObject(f.getName());
            // adds the unique unit to the list
            units.get(unit.getInt("level")).add(unit.getString("name"));
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

    public ArrayList<String> getUnitsOfLevel(int level) {
        return units.get(level);
    }

}