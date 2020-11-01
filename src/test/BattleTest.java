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

public class BattleTest {
    private String initialOwnership = "{\r\n    \"Gaul\": [\r\n        \"A\",\r\n        \"B\"\r\n    ],\r\n    \"Rome\": [\r\n        \"C\",\r\n        \"D\"\r\n    ]\r\n}";
    private String adjacencyString = "{\r\n    \"A\": {\r\n        \"B\": true\r\n    },\r\n    \"B\": {\r\n        \"A\": true,\r\n        \"C\": true\r\n    },\r\n    \"C\": {\r\n        \"B\": true,\r\n        \"D\": true\r\n    },\r\n    \"D\": {\r\n        \"C\": true\r\n    }\r\n}";
    private String landlockedString = "[]";

    @Test
    public void invadeNoEnemiesTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant = new Unit("peasant");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        assertTrue(B.build(peasant));
        g.endTurn();
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), B, C);
        assertTrue(gaul.isAlliedProvince("C"));
        assertFalse(rome.isAlliedProvince("C"));
    }

    @Test
    public void invadeThroughEnemyTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant = new Unit("peasant");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province D = rome.getNthProvince(1);
        assertTrue(B.build(peasant));
        g.endTurn();
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), B, D));
        assertFalse(gaul.isAlliedProvince("C"));
        assertTrue(rome.isAlliedProvince("C"));
    }

    @Test
    public void invasionTwiceTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant = new Unit("peasant");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        Province D = rome.getNthProvince(1);
        assertTrue(B.build(peasant));
        g.endTurn();
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), B, C);
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), C, B));
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), C, D));
        
        g.endTurn();
        assertTrue(g.getFactions().size() == 2);
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), C, D));
        assertTrue(g.getFactions().size() == 1);
    }


    public void initialSetup(Game g) throws IOException{
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        g.initialiseGame(ownership, landlocked, adjacencyMap);
        g.selectFaction("Gaul");
        g.startGame();
        Faction gaul = g.getCurrentFaction();
        gaul.getNthProvince(1).build(new TroopProductionBuilding(gaul));
        g.endTurn();
    }
}
