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
    private Faction f;
    private Map<Integer, ArrayList<String>> units;

    public TroopProductionBuilding(Faction f) {
        super();
        this.f = f;
        this.setBaseContructionTime(1);
        this.setBaseCost(40);
        setName("Troop Production Building");
        setDescription("Each Troop Production Building upgrade will allow for more advance troops for the faction to produce; Spearmen, Missile infantry, Melee cavalry");
        units = new HashMap<Integer, ArrayList<String>>();
        String content = "";
        try {
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/units/unitLevels.json"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        TroopProductionBuilding tb = (TroopProductionBuilding) obj;
        return this.f.getName().equals(tb.getFaction().getName());
    }

    public Faction getFaction() {
        return f;
    }

    public ArrayList<String> getUnitsOfLevel(int level) {
        return units.get(level);
    }

}