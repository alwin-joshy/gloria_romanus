package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class Game {

    private ArrayList<Faction> factions;
    private Map<String, Object> adjacentProvinces;
    private int currentYear;

    public Game() {
        factions = new ArrayList<Faction>();
        adjacentProvinces = new HashMap<String, Object>();
        currentYear = -200;
    }


    public void initialiseFactions(JSONObject map, JSONArray landlocked) {
        for (String key : map.keySet()) {
            Faction f = new Faction(key);
            factions.add(f);
            JSONArray provinces = map.getJSONArray(key);
            for (int i = 0; i < provinces.length(); i++) {
                Province p = new Province(provinces.getString(i), f);
                isLandlocked(landlocked, p);
                f.addProvince(p);
            }
        }
    }

    public void isLandlocked(JSONArray landlocked, Province p) {
        for (int i = 0; i < landlocked.length(); i++) {
            if (landlocked.getString(i).equals(p.getName())) {
                p.setLandlocked();
            }
        }
    }

    public void initialiseAdjacencyMatrix(JSONObject map) {
        for (String key1 : map.keySet()) {
            JSONObject connected = map.getJSONObject(key1);
            Map<String, Integer> distance = new HashMap<String, Integer>();
            for (String key2 : connected.keySet()) {
                if (connected.getString(key2).equals("true")) {
                    distance.put(key2, 4);
                } else {
                    distance.put(key2, 0);
                }
            }
            adjacentProvinces.put(key1, connected);
        }
    }

}
