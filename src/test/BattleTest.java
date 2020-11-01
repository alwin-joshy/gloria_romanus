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

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

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
        B.addUnit(peasant);
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
        B.addUnit(peasant);
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), B, C);
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), C, B));
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), C, D));
        g.endTurn();
        System.out.println(g.getFactions().size());
        assertTrue(g.getFactions().size() == 3);
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), C, D));
        assertTrue(g.getFactions().size() == 2);
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
        B.addUnit(peasant1);
        C.addUnit(peasant2);
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
        B.addUnit(peasant1);
        C.addUnit(peasant2);
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
        B.addUnit(peasant1);
        C.addUnit(peasant2);
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
        B.addUnit(archer1);
        C.addUnit(archer2);
        assertEquals(B.getUnits().size(), C.getUnits().size());
        g.setBRSeed(1);
        g.moveUnits(new ArrayList<Unit>(Arrays.asList(archer1)), B, C);
        assertTrue(B.getUnits().contains(archer1));
        assertTrue(C.getUnits().contains(archer2));
    }

    @Test
    public void peasantArcherMeleeTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant = new Unit("peasant");
        Unit archer = new Unit("archer");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        B.addUnit(peasant);
        C.addUnit(archer);
        g.setBRSeed(2);
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), B, C));
        assertEquals(peasant.getNumTroops(), 19);
        assertEquals(archer.getNumTroops(), 19);
    }

    @Test
    public void peasantArcherRangedTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Unit peasant = new Unit("peasant");
        Unit archer = new Unit("archer");
        Province B = gaul.getNthProvince(1);
        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        B.addUnit(peasant);
        C.addUnit(archer);
        g.setBRSeed(6);
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant)), B, C));
        assertEquals(peasant.getNumTroops(), 17);
        assertEquals(archer.getNumTroops(), 20);
    }

    @Test
    public void heroicChargeAttackingTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Faction rome = g.getFaction("Rome");
        Province B = gaul.getNthProvince(1);
        Province C = rome.getNthProvince(0);
        Unit h1 = new Unit("horseman");
        Unit h2 = new Unit("horseman");
        Unit h3 = new Unit("horseman");
        Unit h4 = new Unit("horseman");
        B.addUnit(h1);
        C.addUnit(h2);
        C.addUnit(h3);
        C.addUnit(h4);
        g.setBRSeed(13);
        assertFalse(g.moveUnits(new ArrayList<Unit>(Arrays.asList(h1)), B, C));
    }

    @Test
    public void heroicChargeDefendingTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Faction rome = g.getFaction("Rome");
        Province B = gaul.getNthProvince(1);
        Province C = rome.getNthProvince(0);
        Unit h1 = new Unit("horseman");
        Unit h2 = new Unit("horseman");
        Unit h3 = new Unit("horseman");
        Unit h4 = new Unit("horseman");
        B.addUnit(h1);
        B.addUnit(h2);
        B.addUnit(h3);
        C.addUnit(h4);
        g.setBRSeed(1);
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(h1, h2, h3)), B, C));
    }

    @Test
    public void horseArchersReducedMissileTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Unit ha = new Unit("horsearcher");
        Unit a = new Unit("archer");
        Random r = new Random(1);
        double haExpectedDmg = (r.nextGaussian() + 1) * ha.getNumTroops() * 0.1 * ((double) a.getAttack()/2 / ((double) (ha.getArmour() + ha.getShieldDefence())));
        r.setSeed(1);
        assertEquals(a.calculateDamage(ha, true, false, r), Math.round(haExpectedDmg));
    }

    @Test
    public void victoryRoutedAttackersTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Faction gaul = g.getCurrentFaction();
        Faction rome = g.getFaction("Rome");
        Province B = gaul.getNthProvince(1);
        Province C = rome.getNthProvince(0);
        Unit h1 = new Unit("horsearcher");
        Unit n1 = new Unit("netman");
        Unit i1 = new Unit("immortal");
        B.addUnit(i1);
        B.addUnit(h1);
        C.addUnit(n1);
        g.setBRSeed(11);
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(i1, h1)), B, C));
        assertTrue(C.getUnits().contains(i1));
        assertTrue(C.getUnits().contains(h1));
    }

    @Test
    public void shieldChargeTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Unit h = new Unit("hoplite");
        Unit k = new Unit("knight");
        Random r = new Random(1);
        h.incrementEngagementCount();
        assertEquals(h.calculateDamage(k, false, false, r), 5);
        r.setSeed(1);
        h.incrementEngagementCount();
        assertEquals(h.calculateDamage(k, false, false, r), 5);
        r.setSeed(1);
        h.incrementEngagementCount();
        assertEquals(h.calculateDamage(k, false, false, r), 5);
        r.setSeed(1);
        h.incrementEngagementCount();
        assertEquals(h.calculateDamage(k, false, false, r), 9);
        //assertEquals(, );
        System.out.println(r.nextInt(10));
    }

    @Test
    public void javelinSkirmisherTest() throws IOException {
        Game g = new Game();
        initialSetup(g);
        Unit j = new Unit("javelinist");
        Unit k = new Unit("knight");
        Random r = new Random(1);
        double knightExpectedDmg = (r.nextGaussian() + 1) * k.getNumTroops() * 0.1 * ((double) j.getAttack() / ((double) (k.getArmour()/2 + k.getShieldDefence())));
        r.setSeed(1);
        assertEquals(j.calculateDamage(k, true, false, r), Math.round(knightExpectedDmg));
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
        g.setBRSeed(7);elephan
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

    @Test
    public void elephantTest() throws IOException{
        Game g = new Game();
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        g.initialiseGame(ownership, landlocked, adjacencyMap);
        g.selectFaction("Gaul");
        g.setBRSeed(2);
        g.startGame();
        Faction gaul = g.getCurrentFaction();
        Province B = gaul.getNthProvince(1);
        
        Unit elephant = new Unit("elephant");
        Unit peasant = new Unit("peasant");
        B.addUnit(elephant);
        B.addUnit(peasant);

        Faction rome = g.getFaction("Rome");
        Province C = rome.getNthProvince(0);
        
        Unit lancer = new Unit("lancer");
        C.addUnit(lancer);
        assertTrue(g.moveUnits(new ArrayList<Unit>(Arrays.asList(peasant, elephant)), B, C));        
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
