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

import org.json.JSONArray;
import org.json.JSONObject;

public class Game {

    private ArrayList<Faction> factions;
    private static Map<String, Map<String, Integer>> adjacentProvinces;
    private int currentYear;
    private boolean isRunning;
    private int currentFaction;
    private ArrayList<VictoryCondition> victories = new ArrayList<VictoryCondition>(Arrays.asList (new ConquestGoal(), new InfrastructureGoal(), 
                                                                                                   new WealthGoal(), new TreasuryGoal()));
    private Goal currentVictoryCondition;
    private BattleResolver br;
    private AI ai;

    public Game(BattleResolver br, AI ai) {
        factions = new ArrayList<Faction>();
        adjacentProvinces = new HashMap<String, Map<String, Integer>>();
        currentYear = -200;
        currentVictoryCondition = generateVictoryCondition();
        this.br = br;
        this.ai = ai;
    }

    public Game() {
        this((BattleResolver) new StandardBattleResolver(), (AI) new StandardAI());
    }

    public void initialiseGame(JSONObject initialOwnership, JSONArray landlocked, JSONObject adjacencyMap) {
        initialiseFactions(initialOwnership, landlocked);
        initialiseAdjacencyMatrix(adjacencyMap);
        Random r = new Random();
        currentFaction = r.nextInt(factions.size());
    }

    public void startGame() {
        factions.get(currentFaction).collectTax();
        isRunning = true;
        if (!factions.get(currentFaction).isPlayer()) {
            endTurn();
        }
    } 

    // Not sure if this is necessary
    public void selectBattleResolver(StandardBattleResolver br) {
        this.br = br;
    }

    public void selectFaction(String name) {
        for (Faction f : factions) {
            if (name.equals(f.getName())) {
                f.setPlayer();
            }
        }
    }

    public void endTurn() {
        if (currentVictoryCondition.checkVictory(factions.get(currentFaction))) {
            isRunning = false; 
        }
        currentFaction = (currentFaction + 1) % factions.size();
        currentYear++;
        factions.get(currentFaction).updateAllProjects();
        factions.get(currentFaction).collectTax();
        if (! factions.get(currentFaction).isPlayer()) {
            endTurn();
        }
    }

    public void playAI() {
        Faction curr = factions.get(currentFaction); 
        while (! curr.isPlayer()) {
            if (currentVictoryCondition.checkVictory(curr)) {
                isRunning = false;
            }
            
            int startingBalance = curr.getTreasury();
            // handle int to double
            while (curr.getTreasury() >= 0.5 * startingBalance) {
                ai.buildInfrastructure(curr);
            }

            while (curr.getTreasury() >= 0) {
                ai.recruitUnit(curr);
            }

            endTurn();
        }
        if (currentVictoryCondition.checkVictory(curr)) {
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

    // Might also need to save AI and BattleResolver

    public void saveGame(String filename) {
        FileOutputStream out;
        try {
            new File("saves").mkdirs();
            out = new FileOutputStream("saves/" + filename);
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(adjacentProvinces);
            os.writeObject(currentYear);
            os.writeObject(factions);
            os.writeObject(currentVictoryCondition);
            os.writeObject(br);
            os.writeInt(currentFaction);
            os.writeBoolean(isRunning);
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
            currentVictoryCondition = (Goal) ins.readObject();
            br = (BattleResolver) ins.readObject();
            currentFaction = ins.readInt();
            isRunning = ins.readBoolean();
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

    public int shortestPathLength(String start, String end) {
        Map<String, Integer> dist = new HashMap<String, Integer>();
        ArrayList<String> visited = new ArrayList<String>();

        for (String p : dist.keySet()) {
            dist.put(p, Integer.MAX_VALUE);
        }
        int movementPoints = 4;
        for (String p : adjacentProvinces.get(start).keySet()) {
            movementPoints = adjacentProvinces.get(start).get(p);
        }
        dist.replace(start, movementPoints);
        int prevSize = -1;

        while (! visited.contains(end) || prevSize != visited.size()) {
            String next = minVertex(dist, visited);
            prevSize = visited.size();
            visited.add(next);

            if (! factions.get(currentFaction).isAlliedProvince(next)) continue;
            Map<String, Integer> innerMap = adjacentProvinces.get(next);

            for (String neighbour : innerMap.keySet()) {

                int d = dist.get(next) + innerMap.get(neighbour);

                if (dist.get(neighbour) > d) {
                    dist.replace(neighbour, d);
                }
            }
        }
        return dist.get(end);
    }

    public String minVertex(Map<String, Integer> dist, ArrayList<String> visited) {
        int x = Integer.MAX_VALUE;
        String p = null;
        for (String curr : dist.keySet()) {
            if (! visited.contains(p) && dist.get(p) < x) {
                p = curr;
                x = dist.get(p);
            }
        }
        return p;
    }

    public void moveUnits(ArrayList<Unit> units, Province start, Province end) {
        int distance = shortestPathLength(start.getName(), end.getName());
        for (Unit u : units) {
            if (! u.canMove(distance)) return;
        }
        Faction curr = factions.get(currentFaction);
        boolean validMove = true;
        if (!curr.isAlliedProvince(end.getName())) {
            validMove = br.battle(start, units, end, end.getUnits());
        }
        if (validMove) curr.moveUnits(units, start, end, distance);
    }

    public VictoryCondition getRandomGoal() {
        Random random = new Random();
        VictoryCondition goal = victories.get(random.nextInt(victories.size()));
        victories.remove(goal);
        return goal;
    }

    public Goal generateVictoryCondition() {
        return recGenerateVictoryCondition(0, victories.size());
    }

    public Goal recGenerateVictoryCondition(int previous, int maxSubgoals) {
        if (victories.size() == 0) return null;
        Random random = new Random();
        int x = (int) Math.round(random.nextGaussian());
        if (x > 0.7 || x < -0.7) {
            int y = random.nextInt(maxSubgoals);
            if (y == 0 || x > 0.7 && previous > 0.7 || x < -0.7 && previous < -0.7) {
                VictoryCondition goal = getRandomGoal();
                return new Condition(goal);
            }
            Goal g;
            if (x > 0.7)
                g = new Subgoal(true);
            else
                g = new Subgoal(false);
            for (int i = 0; i <= y; i++) {
                Goal z = recGenerateVictoryCondition(x, y);
                if (z != null) g.add(z);
                else return g;
            }
            return g;
        } else {
            VictoryCondition goal = getRandomGoal();
            return new Condition(goal);
        }
    }

    public Goal getVictoryCondition() {
        return currentVictoryCondition;
    }

    public ArrayList<Faction> getFactions() {
        return factions;
    }

    public Faction getCurrentFaction() {
        return factions.get(currentFaction);
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public Map<String, Map<String, Integer>> getAdjacencyMatrix() {
        return adjacentProvinces;
    }

    public static void main(String[] args) {
        Game game = new Game();
        Game g = new Game();
        try {
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
            JSONObject map = new JSONObject(content);
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/landlocked_provinces.json"));
            JSONArray landlocked = new JSONArray(content);
            content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
            JSONObject a = new JSONObject(content);
            game.initialiseAdjacencyMatrix(a);
            game.initialiseFactions(map, landlocked);
            game.getVictoryCondition().showGoal();
            game.saveGame("xD");
            game.clear();
            game.loadGame("xD");
            game.printFactions();
            game.getVictoryCondition().showGoal();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
