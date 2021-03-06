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

public class MovementTest {
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
    public void cavalryMovementTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getFactions().get(0);
        Unit horseman = new Unit("horseman");
        Province A = gaul.getNthProvince(0);
        Province B = gaul.getNthProvince(1);
        Province E = gaul.getNthProvince(4);
        
        E.build(horseman);
        g.endTurn();
        ArrayList<Unit> army = new ArrayList<Unit>(Arrays.asList(horseman));
        assertFalse(g.moveUnits(army, E, A));
        assertTrue(g.moveUnits(army, E, B));

        assertFalse(A.getUnits().contains(horseman));
        assertTrue(B.getUnits().contains(horseman));

        assertFalse(g.moveUnits(army, B, A));

        g.endTurn();

        assertTrue(g.moveUnits(army, B, A));

    }

    @Test
    public void artilleryMovementTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Unit catapult = new Unit("catapult");
        Province E = g.getCurrentFaction().getNthProvince(4);
        Province D = g.getCurrentFaction().getNthProvince(3);
        Province C = g.getCurrentFaction().getNthProvince(2);
        g.getCurrentFaction().getNthProvince(4).build(catapult);
        g.endTurn();
        g.endTurn();

        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(catapult)),E, C));
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(catapult)),E, D));

        assertFalse(E.getUnits().contains(catapult));
        assertTrue(D.getUnits().contains(catapult));

        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(catapult)),D, C));

        g.endTurn();

        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(catapult)),D, C));
    }

    @Test
    public void shortestPathTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Province D = g.getCurrentFaction().getNthProvince(3);
        Province K = g.getCurrentFaction().getNthProvince(9);
        D.build(new TroopProductionBuilding(gaul));
        g.endTurn();
        Unit hman = new Unit("horseman");
        gaul.getNthProvince(3).build(hman);
        g.endTurn();
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(hman)), D, K));
        assertEquals(hman.getMovementPointsRemaining(), 7);
    }

    @Test 
    public void infantryMovementTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Unit peasant = new Unit("peasant");
        Province E = g.getCurrentFaction().getNthProvince(4);
        Province D = g.getCurrentFaction().getNthProvince(3);
        Province C = g.getCurrentFaction().getNthProvince(2);
        Province B = g.getCurrentFaction().getNthProvince(1);
        g.getCurrentFaction().getNthProvince(4).build(peasant);
        g.endTurn();

        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)),E, B));
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)),E, C));

        assertFalse(E.getUnits().contains(peasant));
        assertTrue(C.getUnits().contains(peasant));

        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)),C, D));
        g.endTurn();
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)),C, E));

    }

    @Test
    public void enemyProvinceTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getFactions().get(0);
        Unit horseman = new Unit("horseman");
        Province H = gaul.getNthProvince(7);
        Province E = gaul.getNthProvince(4);
        
        E.build(horseman);
        g.endTurn();
        ArrayList<Unit> army = new ArrayList<Unit>(Arrays.asList(horseman));
        assertTrue(g.moveUnits(army, E, H));
        assertEquals(horseman.getMovementPointsRemaining(), 3);
        assertTrue(H.getUnits().contains(horseman));
    }

    @Test
    public void pathThroughEnemyTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getFactions().get(0);
        Unit horseman = new Unit("horseman");
        Province E = gaul.getNthProvince(4);
        Province L = gaul.getNthProvince(10);
        E.build(horseman);
        g.endTurn();
        ArrayList<Unit> army = new ArrayList<Unit>(Arrays.asList(horseman));
        assertFalse(g.moveUnits(army, E, L));
    }

    @Test
    public void moveMultipleUnitsTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getFactions().get(0);
        Unit horseman = new Unit("horseman");
        Unit catapult = new Unit("catapult");
        Unit peasant = new Unit("peasant");
        Province H = gaul.getNthProvince(7);
        Province E = gaul.getNthProvince(4);
        Province G = gaul.getNthProvince(6);
        Province F = gaul.getNthProvince(5);
        
        E.build(horseman);
        g.endTurn();
        E.build(catapult);
        g.endTurn();
        g.endTurn();
        E.build(peasant);
        g.endTurn();
        ArrayList<Unit> army = new ArrayList<Unit>(Arrays.asList(horseman, catapult, peasant));
        assertFalse(g.moveUnits(army, E, G));
        assertFalse(g.moveUnits(army, E, H));
        assertTrue(g.moveUnits(army, E, F));
        assertTrue(F.getUnits().containsAll(army));

    }
    @Test
    public void moveSomeUnitsMoreTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getFactions().get(0);
        Unit catapult = new Unit("catapult");
        Unit peasant = new Unit("peasant");
        Province E = gaul.getNthProvince(4);
        Province G = gaul.getNthProvince(6);
        Province F = gaul.getNthProvince(5);
        E.build(catapult);
        g.endTurn();
        g.endTurn();
        E.build(peasant);
        g.endTurn();
        ArrayList<Unit> army = new ArrayList<Unit>(Arrays.asList(catapult, peasant));
        assertTrue(g.moveUnits(army, E, F));
        assertFalse(g.moveUnits(army, F, G));
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), F, G));
    }

    @Test
    public void moveToSelfTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getFactions().get(0);
        Province E = gaul.getNthProvince(4);
        Unit peasant = new Unit("peasant");
        E.build(peasant);
        g.endTurn();
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), E, E));
    }

}
