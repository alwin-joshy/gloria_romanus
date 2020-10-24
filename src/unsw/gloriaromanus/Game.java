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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.graalvm.compiler.hotspot.nodes.CurrentLockNode;
import org.json.JSONArray;
import org.json.JSONObject;

public class Game {

    private ArrayList<Faction> factions;
    private static Map<String, Map<String, Integer>> adjacentProvinces;
    private ArrayList<Province> provinces;
    private int currentYear;
    private boolean isRunning;
    private int currentFaction;
    private ArrayList<VictoryCondition> victories = new ArrayList<VictoryCondition>(Arrays.asList (new ConquestGoal(), new InfrastructureGoal(), 
                                                                                                   new WealthGoal(), new TreasuryGoal()));
    private VictoryCondition currentVictoryCondition;

    public Game() {
        factions = new ArrayList<Faction>();
        adjacentProvinces = new HashMap<String, Map<String, Integer>>();
        provinces = new ArrayList<Province>
        currentYear = -200;
        Random r = new Random();
        currentFaction = r.nextInt(victories.size());
        currentVictoryCondition = victories.get(r.nextInt(victories.size()));

    }

    public void startGame(JSONObject initialOwnership, JSONArray landlocked, JSONObject adjacencyMap) {
        initialiseFactions(initialOwnership, landlocked);
        initialiseAdjacencyMatrix(adjacencyMap);
        isRunning = true;
    } 

    public void selectFaction(String name) {
        for (Faction f : factions) {
            if (name.equals(f.getName())) {
                f.setPlayer();
            }
        }
    }

    public void endTurn() {
        if (currentVictoryCondition.checkCondition(factions.get(currentFaction))) {
            isRunning = false; 
        }
        currentFaction = (currentFaction++) % factions.size();
        currentYear++;
    }

    public void playAI() {
        Faction curr = factions.get(currentFaction); 
        while (! curr.isPlayer()) {
            if (currentVictoryCondition.checkCondition(curr)) {
                isRunning = false;
            }
            AI(curr);
            endTurn();
        }
        if (currentVictoryCondition.checkCondition(curr)) {
            isRunning = false; 
        }
    }

    public void initialiseFactions(JSONObject initialOwnership, JSONArray landlocked) {
        for (String key : initialOwnership.keySet()) {
            Faction f = new Faction(key);
            factions.add(f);
            JSONArray provinces = initialOwnership.getJSONArray(key);
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
                }
                // } else {
                //     distance.put(key2, 0);
                // }
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

    public int shortestPathLength(Province start, Province end) {
        Map<Province, Integer> dist = new HashMap<Province, Integer>();
        ArrayList<Province> visited = new ArrayList<Province>();

        for (Province p : dist.keySet()) {
            dist.put(p, Integer.MAX_VALUE);
        }
        dist.replace(p, 0)
        int prevSize;

        while (!visited.contains(end) || prevSize != visited.size()) {
            Province next = minVertex(dist, visited);
            prevSize = visted.size();
            visited.add(next);

            if (! factions.get(currentFaction).isAlliedProvince(next)) continue
            Map<Province, Integer> innerMap = adjacentProvinces.get(next);

            for (Province neighbour : innerMap.keySet()) {
                int d = dist.get(next) + innerMap.get(neighbour);

                if (dist.get(neighbour) > d) {
                    dist.replace(neighbour, d)
                }
            }
        }

        return dist.get(end);
    }

    public Province minVertex(Map<Province, Integer> dist, ArrayList<Province> visited) {
        int x = Integer.MAX_VALUE;
        Province p = null;
        for (Province curr : dist.keySet) {
            if (! visted.contains(p) && dist.get(p) < x) {
                p = curr;
                x = dist.get(p)
            }
        }
        return p;
    }


    // public int shortestPathLength(Province start, Province end) {
    //     String startName = start.getName();
    //     String endName = end.getName();
    //     Map<Province, Integer> dist = new HashMap<Province, Integer>();
    //     ArrayList<Province> visted = new ArrayList<Province>();
    //     PriorityQueue<ProvinceComparatror> pqueue = new PriorityQueue<ProvinceComparatror>(52, new ProvinceComparatror());
    //     for (Province p : provinces) {
    //         dist.put(p, Integer.MAX_VALUE)
    //     }

    //     pqueue.add(new ProvinceComparator(start, 0));
    //     dist.replace(start, 0);

    //     while (visited.size() != 52) {
    //         Province p = pqueue.remove().getProvince();

    //         visited.add(p);
    //         graphAdjacentNodes(p, endName, pqueue, visted, dist);
    //     }

    //     return dist.get(end);
    // }

    // private void graphAdjacentNodes(Province p, Province end, PriorityQueue<ProvinceComparator> pq, ArrayList<Province> visited, Map<Province, Integer> dist) {
    //     int edgeDistance = -1;
    //     int newDistance = -1;
        
    //     Map<String, Integer> innerMap = adjacentProvinces.get(p.getName()) 
    //     for (String innerProvinceName : innerMap.keySet()){
    //         int curr = innerMap.get(innerProvince)
    //         Province innerProvince = findProvince(innerProvinceName);
    //         if curr <= 0 continue;
    //         if (! innerProvince.equals(end) && ! factions.get(currentFaction).isAlliedProvince(innerProvince)) continue;
        
    //         if (! visited.contains(innerProvince)) {
    //             edgeDistance = curr;
    //             newDistance = dist.get(p) + edgeDistance;
                
    //             if (newDistance < dist.get(innerProvince)) {
    //                 dist.replace(innerProvince, newDistance)
    //             }
    //         }

    //         pq.add(new ProvinceComparator(innerProvince, dist.get(innerProvince)))

    //     }
    // }

    public Provice findProvice(String provinceName) {
        for (Province p : provinces) {
            if provinceName.equals(p.getName()) return p;
        }
        return null;
    }

    public void moveUnits(ArrayList<Unit> units, Province start, Province end) {
        int distance = shortestPathLength(start, end);
        factions.get(currentFaction).moveUnits(units, start, end, distance);
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
