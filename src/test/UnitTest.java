package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.*;

public class UnitTest{
    private String initialOwnership = "{\r\n  \"Rome\": [\r\n    \"Lugdunensis\" ],\r\n  \"Gaul\": [\r\n    \"Achaia\" ]\r\n}";
    private String landlockedString = "[\r\n \"Germania Superior\",\r\n \"Raetia\",\r\n \"Alpes Graiae et Poeninae\",\r\n \"Alpes Cottiae\",\r\n \"XI\",\r\n \"Noricum\",\r\n \"Pannonia Superior\",\r\n \"Pannonia Inferior\",\r\n \"Moesia Superior\",\r\n \"Dacia\"\r\n]\r\n";
    private String adjacencyString = "{\r\n  \"Britannia\": {\r\n    \"Britannia\": false,\r\n    \"Lugdunensis\": true,\r\n    \"Belgica\": false,\r\n    \"Germania Inferior\": false,\r\n    \"Aquitania\": false,\r\n    \"Germania Superior\": false,\r\n    \"Alpes Graiae et Poeninae\": false,\r\n    \"XI\": false,\r\n    \"Alpes Cottiae\": false,\r\n    \"Alpes Maritimae\": false,\r\n    \"IX\": false,\r\n    \"Narbonensis\": false,\r\n    \"Tarraconensis\": false,\r\n    \"Baetica\": false,\r\n    \"Lusitania\": false,\r\n    \"Raetia\": false,\r\n    \"Noricum\": false,\r\n    \"X\": false,\r\n    \"VIII\": false,\r\n    \"VII\": false,\r\n    \"VI\": false,\r\n    \"IV\": false,\r\n    \"V\": false,\r\n    \"I\": false,\r\n    \"III\": false,\r\n    \"Sicilia\": false,\r\n    \"Pannonia Superior\": false,\r\n    \"Pannonia Inferior\": false,\r\n    \"Dalmatia\": false,\r\n    \"II\": false,\r\n    \"Sardinia et Corsica\": false,\r\n    \"Moesia Superior\": false,\r\n    \"Dacia\": false,\r\n    \"Moesia Inferior\": false,\r\n    \"Thracia\": false,\r\n    \"Macedonia\": false,\r\n    \"Achaia\": false,\r\n    \"Bithynia et Pontus\": false,\r\n    \"Cilicia\": false,\r\n    \"Creta et Cyrene\": false,\r\n    \"Cyprus\": false,\r\n    \"Aegyptus\": false,\r\n    \"Arabia\": false,\r\n    \"Iudaea\": false,\r\n    \"Syria\": false,\r\n    \"Africa Proconsularis\": false,\r\n    \"Numidia\": false,\r\n    \"Mauretania Caesariensis\": false,\r\n    \"Mauretania Tingitana\": false,\r\n    \"Galatia et Cappadocia\": false,\r\n    \"Lycia et Pamphylia\": false,\r\n    \"Asia\": false,\r\n    \"Armenia Mesopotamia\": false\r\n  }\r\n}";

    @Test
    public void recruitUnitTest() {
        Game g = new Game();
        initialSetup(g);
        Faction currentFaction = g.getCurrentFaction();
        assertNull(currentFaction.getNthProvince(0).getTroopProductionBuilding());
        try {
            currentFaction.getNthProvince(0).build(new TroopProductionBuilding(currentFaction));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        g.endTurn();
        assertNotNull(currentFaction.getNthProvince(0).getTroopProductionBuilding());
        int prevTreasury = currentFaction.getTreasury();
        try {
            currentFaction.getNthProvince(0).build(new Unit("peasant"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        assertEquals(prevTreasury, currentFaction.getTreasury() + 35);
        g.endTurn();
        assertTrue(currentFaction.getNthProvince(0).getNthUnit(0).getName().equals("peasant"));
    }

    @Test
    public void recruitMultipleConsecutiveUnitTest(){
        Game g = new Game();
        initialSetup(g);
        Faction currentFaction = g.getCurrentFaction();
        try {
            currentFaction.getNthProvince(0).build(new TroopProductionBuilding(currentFaction));
            g.endTurn();
            assertTrue(currentFaction.getNthProvince(0).build(new Unit("peasant")));
            g.endTurn();
            assertTrue(currentFaction.getNthProvince(0).build(new Unit("peasant")));
            g.endTurn();
            assertTrue(currentFaction.getNthProvince(0).build(new Unit("peasant")));
            g.endTurn();
            assertTrue(currentFaction.getNthProvince(0).build(new Unit("peasant")));
            g.endTurn();
            assertFalse(currentFaction.getNthProvince(0).build(new Unit("peasant")));
            g.endTurn();
            assertTrue(currentFaction.getNthProvince(0).build(new Unit("peasant")));
            g.endTurn();
            assertFalse(currentFaction.getNthProvince(0).build(new Unit("peasant")));
            g.endTurn();
            assertFalse(currentFaction.getNthProvince(0).build(new Unit("peasant")));
            g.endTurn();
            assertTrue(currentFaction.getNthProvince(0).build(new Unit("peasant")));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    public void recruitMultipleConcurrentUnitTest() {
        Game g = new Game();
        initialSetup(g);
        Faction currentFaction = g.getCurrentFaction();
        try {
            currentFaction.getNthProvince(0).build(new TroopProductionBuilding(currentFaction));
            g.endTurn();
            assertEquals(currentFaction.getNthProvince(0).build(new Unit("peasant")), true);
            assertEquals(currentFaction.getNthProvince(0).build(new Unit("peasant")), false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // @Test
    // public void recruitLevel2UnitTest() {
    //     Game g = new Game();
    //     initialSetup(g);
    //     Faction currentFaction = g.getCurrentFaction();
    //     try {
    //         currentFaction.getNthProvince(0).build(new TroopProductionBuilding(currentFaction));
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         System.exit(1);
    //     }
    //     g.endTurn();
    //     TroopProductionBuilding tb = currentFaction.getNthProvince(0).getTroopProductionBuilding();
    //     g.endTurn();
    //     g.endTurn();
    //     assertEquals(currentFaction.getNthProvince(0).build(tb), true);
    //     g.endTurn();
    //     g.endTurn();
    //     try {
    //         assertEquals(currentFaction.getNthProvince(0).build(new Unit("archer")), true);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         System.exit(1);
    //     }
    //     g.endTurn();
    //     assertEquals(currentFaction.getNthProvince(0).getUnits().size(), 0);
    //     g.endTurn();
    //     assertEquals(currentFaction.getNthProvince(0).getUnits().size(), 1);
    //     assertTrue(currentFaction.getNthProvince(0).getNthUnit(0).getName().equals("archer"));
    // }

    public void initialSetup(Game g) {
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        g.initialiseGame(ownership, landlocked, adjacencyMap);
        Faction currentFaction = g.getCurrentFaction();
        g.selectFaction(currentFaction.getName());
        g.startGame();
    }

    
    // @Test
    // public void blahTest2(){
    //     Unit u = new Unit("peasant");
    //     assertEquals(u.getNumTroops(), 50);
    // }
}

