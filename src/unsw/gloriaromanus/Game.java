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
    private StandardBattleResolver br;
    private StandardAI ai;

    public Game() {
        factions = new ArrayList<Faction>();
        adjacentProvinces = new HashMap<String, Map<String, Integer>>();
        currentYear = -200;
        Random r = new Random();
        currentFaction = r.nextInt(victories.size());
        currentVictoryCondition = gvc();
        br = new StandardBattleResolver();
        ai = new StandardAI();
    }

    public void startGame(JSONObject initialOwnership, JSONArray landlocked, JSONObject adjacencyMap) {
        initialiseFactions(initialOwnership, landlocked);
        initialiseAdjacencyMatrix(adjacencyMap);
        isRunning = true;
    } 

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
        currentFaction = (currentFaction++) % factions.size();
        currentYear++;
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
        dist.replace(start, 0);
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

    public Goal gvc() {
        if (victories.size() == 0) return null;
        Random random = new Random();
        int x = random.nextInt(3);
        if (x != 0) {
            int y = random.nextInt(victories.size());
            Goal g;
            if (x == 1)
                g = new Subgoal(true);
            else
                g = new Subgoal(false);
            for (int i = 0; i < y; i++) {
                Goal z = gvc();
                if (z != null) g.add(gvc());
                else return g;
            }
            return g;
        } else {
            VictoryCondition goal = getRandomGoal();
            return new Condition(goal);
        }
    }

    public void generateVictoryCondition() {
        Random random = new Random();
        VictoryCondition goal = getRandomGoal();
        int x = random.nextInt(3);
        if (x == 0) {
            Goal l = new Condition(goal);
            currentVictoryCondition = l;
        } else {
            Goal l = null;
            if (x == 1) 
                l = new Subgoal(true);
            else if (x == 2) 
                l = new Subgoal(false);
            l.add(new Condition(goal));
            goal = getRandomGoal();
            l.add(new Condition(goal));
            int y = random.nextInt(3);
            if (y == x) {
                goal = getRandomGoal();
                l.add(new Condition(goal));
                int z = random.nextInt(3);
                if (z == y) {
                    goal = getRandomGoal();
                    l.add(new Condition(goal));
                    currentVictoryCondition = l;
                } else if (z != 0) {
                    Goal m;
                    if (z == 1) {
                        m = new Subgoal(true);
                    } else {
                        m = new Subgoal(false);
                    }
                    goal = getRandomGoal();
                    m.add(new Condition(goal));
                    m.add(l);
                    currentVictoryCondition = m;
                }
            } else if (y != 0) {
                Goal m;
                if (y == 1) {
                    m = new Subgoal(true);
                } else {
                    m = new Subgoal(false);
                }
                m.add(l);
                goal = getRandomGoal();
                int a = random.nextInt(3);
                if (a == 0) m.add(new Condition(goal));
                else if (a == 1) {
                    m.add(new Condition(goal));
                    goal = getRandomGoal();
                    m.add(new Condition(goal));
                } else {
                    int b = random.nextInt(2);
                    Goal n;
                    if (b == 0) 
                        n = new Subgoal(true);
                    else 
                        n = new Subgoal(false);
                    n.add(new Condition(goal));
                    goal = getRandomGoal();
                    n.add(new Condition(goal));
                    m.add(n);
                }
                currentVictoryCondition = m;
            } else {
                currentVictoryCondition = l;
            }
        }
    }

    public Goal getVictoryCondition() {
        return currentVictoryCondition;
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
