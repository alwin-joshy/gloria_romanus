package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.*;

public class BattleTest {
    private String initialOwnership = "{\r\n    \"Gaul\": [\r\n        \"A\",\r\n        \"B\"\r\n    ],\r\n    \"Rome\": [\r\n        \"C\",\r\n        \"D\"\r\n    ],\r\n    \"Spain\": [\r\n        \"E\"\r\n    ]\r\n}";
    private String adjacencyString = "{\r\n    \"A\": {\r\n        \"B\": true,\r\n        \"E\": true\r\n    },\r\n    \"B\": {\r\n        \"A\": true,\r\n        \"C\": true\r\n    },\r\n    \"C\": {\r\n        \"B\": true,\r\n        \"D\": true\r\n    },\r\n    \"D\": {\r\n        \"C\": true\r\n    },\r\n    \"E\": {\r\n        \"A\": true\r\n    }\r\n}";
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

    @Test 
    public void twoPeasantsAttackerBreaksTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant1 = new Unit("peasant");
        Unit peasant2 = new Unit("peasant");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        assertTrue(B.build(peasant1));
        assertTrue(C.build(new TroopProductionBuilding(rome)));
        g.endTurn();
        assertTrue(C.build(peasant2));
        g.endTurn();
        assertEquals(B.getUnits().size(), C.getUnits().size());
        g.setBRSeed(1);
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant1)), B, C);
        assertTrue(B.getUnits().contains(peasant1));
        assertFalse(C.getUnits().contains(peasant2));
    }

    @Test
    public void twoPeasantsDrawTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant1 = new Unit("peasant");
        Unit peasant2 = new Unit("peasant");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        assertTrue(B.build(peasant1));
        assertTrue(C.build(new TroopProductionBuilding(rome)));
        g.endTurn();
        assertTrue(C.build(peasant2));
        g.endTurn();
        assertEquals(B.getUnits().size(), C.getUnits().size());
        g.setBRSeed(2);
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant1)), B, C);
        assertTrue(B.getUnits().contains(peasant1));
        assertFalse(C.getUnits().contains(peasant2));
    }

    @Test
    public void twoPeasantsDefenderBreaksTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant1 = new Unit("peasant");
        Unit peasant2 = new Unit("peasant");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        assertTrue(B.build(peasant1));
        assertTrue(C.build(new TroopProductionBuilding(rome)));
        g.endTurn();
        assertTrue(C.build(peasant2));
        g.endTurn();
        assertEquals(B.getUnits().size(), C.getUnits().size());
        g.setBRSeed(8);
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant1)), B, C);
        assertTrue(C.getUnits().contains(peasant1));
        assertFalse(C.getUnits().contains(peasant2));
        assertFalse(B.getUnits().contains(peasant1));
    }


    @Test
    public void twoArchers() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit archer1 = new Unit("archer");
        Unit archer2 = new Unit("archer");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        B.build(B.getTroopProductionBuilding());
        C.build(new TroopProductionBuilding(rome));
        g.endTurn();
        C.build(C.getTroopProductionBuilding());
        g.endTurn();
        B.build(archer1);
        g.endTurn();
        C.build(archer2);
        g.endTurn();
        g.endTurn();
        assertEquals(B.getUnits().size(), C.getUnits().size());
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(archer1)), B, C);
        assertTrue(B.getUnits().contains(archer1));
        assertTrue(C.getUnits().contains(archer2));
    }



    @Test
    public void invadeThroughTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant1 = new Unit("peasant");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        Province D = rome.getNthProvince(1);
        assertTrue(B.build(peasant1));
        g.endTurn();
        Unit peasant2 = new Unit("peasant");
        assertTrue(B.build(peasant2));
        g.endTurn();
        assertEquals(B.getUnits().size(), 2);
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant1)), B, C));
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant2)), B, D));

    }

    @Test
    public void invadedProvinceAndBackTest() throws IOException{
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant1 = new Unit("peasant");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        Province D = rome.getNthProvince(1);
        assertTrue(B.build(peasant1));
        g.endTurn();
        Unit peasant2 = new Unit("peasant");
        assertTrue(B.build(peasant2));
        g.endTurn();
        assertEquals(B.getUnits().size(), 2);
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant1)), B, C));
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant2)), B, C));
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant2)), C, B));
    }

    @Test
    public void battleDraw200EngagementsTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        ArrayList<Unit> army = new ArrayList<Unit>();
        g.setBRSeed(7);
        // both armies should never entirely route
        for (int i = 0; i < 1000; i++) {
            B.addUnit(new Unit("knight"));
            army.add(new Unit("knight"));
            C.addUnit(new Unit("knight"));
        }
        assertFalse(g.moveUnits(army, B, C));
        assertTrue(C.getUnits().size() > 0 && B.getUnits().size() > 0);
    }

    // unit cant route
    @Test
    public void failedRouteTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        Unit lancer = new Unit("lancer");
        g.setBRSeed(7);
        B.addUnit(lancer);
        C.addUnit(new Unit("peasant"));
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(lancer)), B, C);
    }

    // when an attacking army loses at battle, all routed units return
    // to the original province
    @Test
    public void routedAttackersReturnTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        g.setBRSeed(1);
        ArrayList<Unit> army = new ArrayList<Unit>();
        for (int i = 0; i < 10; i++) {
            Unit catapult = new Unit("catapult");
            B.addUnit(catapult);
            army.add(catapult);
            C.addUnit(new Unit("lancer"));
        }
        assertFalse(g.moveUnits(army, B, C));
        assertTrue(B.getUnits().size() > 0);
    }

    @Test
    public void routedDefendersDestroyedTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        g.setBRSeed(23);
        ArrayList<Unit> army = new ArrayList<Unit>();
        army.add(new Unit("lancer"));
        army.add(new Unit("lancer"));
        B.addUnit(new Unit("lancer"));
        B.addUnit(new Unit("lancer"));
        C.addUnit(new Unit("lancer"));
        C.addUnit(new Unit("lancer"));
        C.addUnit(new Unit("lancer"));
        assertFalse(g.moveUnits(army, B, C));
        assertTrue(C.getUnits().size() == 2);
    }

    @Test
    public void druidTest() throws IOException {
        Game g = new Game();
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        g.initialiseGame(ownership, landlocked, adjacencyMap);
        g.selectFaction("Spain");
        g.setBRSeed(1);
        g.startGame();
        Faction spain = g.getCurrentFaction();
        Province M = spain.getNthProvince(0);
        ArrayList<Unit> army = new ArrayList<Unit>();
        for (int i = 0; i < 6; i++) {
            Unit druid = new Unit("druid");
            M.addUnit(druid);
            army.add(druid);
        }
        Faction gaul = g.getFaction("Gaul");
        Province A = gaul.getNthProvince(0);
        A.addUnit(new Unit("peasant"));
        assertTrue(g.moveUnits(army, M, A));
    }

    @Test
    public void legionaryTest() throws IOException {
        Game g = new Game();
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        g.initialiseGame(ownership, landlocked, adjacencyMap);
        g.selectFaction("Gaul");
        g.setBRSeed(1);
        g.startGame();
        Faction gaul = g.getCurrentFaction();
        Province B = gaul.getNthProvince(1);
        ArrayList<Unit> army = new ArrayList<Unit>();
        
        for (int i = 0; i < 20; i++) {
            Unit lancer = new Unit("lancer");
            army.add(lancer);
            B.addUnit(lancer);
        }

        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        for (int i = 0; i < 5; i++) {
            Unit legionary = new Unit("legionary");
            C.addUnit(legionary);
        }
        assertTrue(g.moveUnits(army, B, C));
    }


    public void initialSetup(Game g) throws IOException {
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
