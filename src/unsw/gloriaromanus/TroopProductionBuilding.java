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
        JSONObject unitList = new JSONObject("units/units");
        int countLevel = 1;
        // converts json file containing general units into a hashmap
        for (String level : unitList.keySet()) {
            JSONArray jsonUnits = unitList.getJSONArray(level);
            ArrayList<String> unitsSubList = ArrayUtil.convert(jsonUnits);
            units.put(countLevel, unitsSubList);
            countLevel++;
        }
    }

    public void levelUp() {
        updateCosts();
    }

}