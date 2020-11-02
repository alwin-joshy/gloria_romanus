package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.*;

public class VictoryTest {
    private String initialOwnership = "{\r\n    \"Rome\": [\r\n        \"I\"\r\n    ],\r\n    \"Gaul\": [\r\n        \"A\",\r\n        \"B\",\r\n        \"C\",\r\n        \"D\",\r\n        \"E\",\r\n        \"F\",\r\n        \"G\",\r\n        \"H\",\r\n        \"J\",\r\n        \"K\",\r\n        \"L\"\r\n    ]\r\n}";
    private String landlockedString = "[]";
    private String adjacencyString = "{\r\n    \"A\": {\r\n        \"B\": true\r\n    },\r\n    \"B\": {\r\n        \"A\": true,\r\n        \"C\": true\r\n    },\r\n    \"C\": {\r\n        \"B\": true,\r\n        \"D\": true\r\n    },\r\n    \"D\": {\r\n        \"E\": true,\r\n        \"C\": true\r\n    },\r\n    \"E\": {\r\n        \"I\": true,\r\n        \"F\": true,\r\n        \"D\": true,\r\n        \"J\": true,\r\n        \"K\": true\r\n    },\r\n    \"F\": {\r\n        \"E\": true,\r\n        \"G\": true\r\n    },\r\n    \"G\": {\r\n        \"F\": true,\r\n        \"H\": true\r\n    },\r\n    \"H\": {\r\n        \"G\": true,\r\n        \"I\": true\r\n    },\r\n    \"I\": {\r\n        \"H\": true,\r\n        \"E\": true,\r\n        \"L\": true\r\n    },\r\n    \"J\" : {\r\n        \"E\": true,\r\n        \"K\": true\r\n    }, \r\n    \"K\" : {\r\n        \"J\": true,\r\n        \"E\": true\r\n    },\r\n    \"L\" : {\r\n        \"I\": true\r\n    }\r\n}";

     
    public void initialSetup(Game g) throws IOException {
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        g.initialiseGame(ownership, landlocked, adjacencyMap);
        g.selectFaction("Gaul");
        g.startGame();
        Faction gaul = g.getFactions().get(0);
        gaul.setTreasury(10000);
        gaul.getNthProvince(4).build(new TroopProductionBuilding(gaul));
        g.endTurn();
        gaul.getNthProvince(4).build(gaul.getNthProvince(4).getTroopProductionBuilding());
        g.endTurn();
        g.endTurn();
    }

    @Test
    public void wealthVictoryNaturalTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Goal vg = new Condition(new WealthGoal());
        g.setVictoryCondition(vg);
        Faction gaul = g.getFactions().get(0);
        Province A = gaul.getNthProvince(0);
        A.setWealth(399999);
        g.endTurn();
        assertTrue(g.isFinished());
    }

    @Test
    public void wealthVictoryInvadeTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Goal vg = new Condition(new WealthGoal());
        g.setVictoryCondition(vg);
        Faction gaul = g.getFactions().get(0);
        Province E = gaul.getNthProvince(4);
        Province I = g.getFactions().get(1).getNthProvince(0);
        Unit peasant = new Unit("peasant");
        E.build(peasant);
        g.endTurn();
        I.setWealth(399999);
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), E, I);
        assertTrue(g.isFinished());
    }

    @Test 
    public void treasuryVictoryTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Goal vg = new Condition(new TreasuryGoal());
        g.setVictoryCondition(vg);
        Faction gaul = g.getFactions().get(0);
        gaul.setTreasury(99999);
        g.endTurn();
        assertTrue(g.isFinished());
    }

    @Test
    public void disjunctionVictoryTreasuryTest() throws IOException{
        Game g = new Game();
        initialSetup(g);
        Goal tg = new Condition(new TreasuryGoal());
        Goal wg = new Condition(new WealthGoal());
        Goal vg = new Subgoal(false);
        vg.add(tg);
        vg.add(wg);
        g.setVictoryCondition(vg);
        Faction gaul = g.getFactions().get(0);
        gaul.setTreasury(99999);
        g.endTurn();
    }
    
    @Test
    public void conquestVictoryInvadeTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Goal vg = new Condition(new ConquestGoal(g.getNumProvinces()));
        g.setVictoryCondition(vg);
        Faction gaul = g.getFactions().get(0);
        Province E = gaul.getNthProvince(4);
        Province I = g.getFactions().get(1).getNthProvince(0);
        Unit peasant = new Unit("peasant");
        E.build(peasant);
        g.endTurn();
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), E, I);
        assertTrue(g.isFinished());
    }

    @Test
    public void disjunctionVictoryWealthTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Goal tg = new Condition(new TreasuryGoal());
        Goal wg = new Condition(new WealthGoal());
        Goal vg = new Subgoal(false);
        vg.add(tg);
        vg.add(wg);
        g.setVictoryCondition(vg);
        Faction gaul = g.getFactions().get(0);
        Province A = gaul.getNthProvince(0);
        A.setWealth(399999);
        g.endTurn();
        assertTrue(g.isFinished());
    }


    public void conjunctionVictoryTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Goal vg1 = new Condition(new ConquestGoal(g.getNumProvinces()));
        Goal vg2 = new Condition(new WealthGoal());
        Goal vg3 = new Subgoal(true);
        vg3.add(vg1);
        vg3.add(vg2);
        g.setVictoryCondition(vg3);
        Faction gaul = g.getFactions().get(0);
        Province E = gaul.getNthProvince(4);
        Province I = g.getFactions().get(1).getNthProvince(0);
        Unit peasant = new Unit("peasant");
        E.build(peasant);
        g.endTurn();
        E.setWealth(400000);
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), E, I);
        assertTrue(g.isFinished());
    }
}
