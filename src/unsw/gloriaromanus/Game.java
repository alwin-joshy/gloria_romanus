package unsw.gloriaromanus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Game {

    private ArrayList<Faction> factions;
    private static Map<String, Map<String, Integer>> adjacentProvinces;
    private int currentYear;

    public Game() {
        factions = new ArrayList<Faction>();
        adjacentProvinces = new HashMap<String, Map<String, Integer>>();
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
                if (connected.getBoolean(key2)) {
                    distance.put(key2, 4);
                } else {
                    distance.put(key2, 0);
                }
            }
            adjacentProvinces.put(key1, distance);
        }
    }

    public static void updateAdjacentProvinces(Province p) {
        Map<String, Integer> province = adjacentProvinces.get(p.getName());
        for (String key : province.keySet()) {
            province.compute(key, (k, v) -> v--);
        }
    }

    public void saveGame(String filename) {
        FileOutputStream out;
        try {
            new File("saves").mkdirs();
            out = new FileOutputStream("saves/" + filename);
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(adjacentProvinces);
            os.writeObject(currentYear);
            os.writeObject(factions);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(String filename) {
        FileInputStream in;
        ObjectInputStream ins;
        try {
            in = new FileInputStream("saves/" + filename);
            ins = new ObjectInputStream(in);
            adjacentProvinces = (Map<String, Map<String, Integer>>) ins.readObject();
            currentYear = (int) ins.readObject();
            factions = (ArrayList<Faction>) ins.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        factions.clear();
        adjacentProvinces.clear();
        currentYear = 0;
    }

    public void printFactions() {
        for (Faction f : factions) {
            System.out.println(f.getName());
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        try {
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
            JSONObject map = new JSONObject(content);
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/landlocked_provinces.json"));
            JSONArray landlocked = new JSONArray(content);
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
            JSONObject a = new JSONObject(content);
            game.initialiseAdjacencyMatrix(a);
            game.initialiseFactions(map, landlocked);
            game.saveGame("xD");
            game.clear();
            game.loadGame("xD");
            game.printFactions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
