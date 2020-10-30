package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONObject;

public class TroopProductionBuilding extends Infrastructure {
    private Faction f;
    private static Map<Integer, ArrayList<String>> units;

    public TroopProductionBuilding(Faction f) {
        this.f = f;
        units = new HashMap<Integer, ArrayList<String>>();
        JSONObject unitList = new JSONObject("units/units.json");
        int countLevel = 1;
        // converts json file containing general units into a hashmap
        for (String level : unitList.keySet()) {
            JSONArray jsonUnits = unitList.getJSONArray(level);
            ArrayList<String> unitsSubList = ArrayUtil.convert(jsonUnits);
            units.put(countLevel, unitsSubList);
            countLevel++;
        }
        JSONObject uniqueUnits = new JSONObject("units/uniqueUnits.json");
        JSONObject unit = uniqueUnits.getJSONObject(f.getName());
        // adds the unique unit to the list
        units.get(unit.getInt("level")).add(unit.getString("name"));

    }

    public void levelUp() {
        updateCosts();
    }

    // TODO Change this in iteration 3
    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()) return true;
        return false;
    }

}