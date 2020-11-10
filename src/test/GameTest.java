package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import org.json.JSONArray;
import org.json.JSONObject;

import unsw.gloriaromanus.*;

public class GameTest {
    private String initialOwnership = "{\r\n  \"Rome\": [\r\n    \"Lugdunensis\",\r\n    \"Lusitania\",\r\n    \"Lycia et Pamphylia\",\r\n   \"Macedonia\",\r\n    \"Mauretania Caesariensis\",\r\n    \"Mauretania Tingitana\",\r\n    \"Moesia Inferior\",\r\n    \"Moesia Superior\",\r\n    \"Narbonensis\",\r\n    \"Noricum\",\r\n    \"Numidia\",\r\n    \"Pannonia Inferior\",\r\n    \"Pannonia Superior\",\r\n    \"Raetia\",\r\n    \"Sardinia et Corsica\",\r\n    \"Sicilia\",\r\n    \"Syria\",\r\n    \"Tarraconensis\",\r\n    \"Thracia\",\r\n    \"V\",\r\n    \"VI\",\r\n    \"VII\",\r\n    \"VIII\",\r\n    \"X\",\r\n    \"XI\"\r\n  ],\r\n  \"Gaul\": [\r\n    \"Achaia\",\r\n    \"Aegyptus\",\r\n    \"Africa Proconsularis\",\r\n    \"Alpes Cottiae\",\r\n    \"Alpes Graiae et Poeninae\",\r\n    \"Alpes Maritimae\",\r\n    \"Aquitania\",\r\n    \"Arabia\",\r\n    \"Armenia Mesopotamia\",\r\n    \"Asia\",\r\n    \"Baetica\",\r\n    \"Belgica\",\r\n    \"Bithynia et Pontus\",\r\n    \"Britannia\",\r\n    \"Cilicia\",\r\n    \"Creta et Cyrene\",\r\n    \"Cyprus\",\r\n    \"Dacia\",\r\n    \"Dalmatia\",\r\n    \"Galatia et Cappadocia\",\r\n    \"Germania Inferior\",\r\n    \"Germania Superior\",\r\n    \"I\",\r\n    \"II\",\r\n    \"III\",\r\n    \"IV\",\r\n    \"IX\",\r\n    \"Iudaea\"\r\n  ]\r\n}";
    private String landlockedString = "[\r\n \"Germania Superior\",\r\n \"Raetia\",\r\n \"Alpes Graiae et Poeninae\",\r\n \"Alpes Cottiae\",\r\n \"XI\",\r\n \"Noricum\",\r\n \"Pannonia Superior\",\r\n \"Pannonia Inferior\",\r\n \"Moesia Superior\",\r\n \"Dacia\"\r\n]\r\n";
    private String adjacencyString = "{\r\n  \"Britannia\": {\r\n    \"Britannia\": false,\r\n    \"Lugdunensis\": true,\r\n    \"Belgica\": false,\r\n    \"Germania Inferior\": false,\r\n    \"Aquitania\": false,\r\n    \"Germania Superior\": false,\r\n    \"Alpes Graiae et Poeninae\": false,\r\n    \"XI\": false,\r\n    \"Alpes Cottiae\": false,\r\n    \"Alpes Maritimae\": false,\r\n    \"IX\": false,\r\n    \"Narbonensis\": false,\r\n    \"Tarraconensis\": false,\r\n    \"Baetica\": false,\r\n    \"Lusitania\": false,\r\n    \"Raetia\": false,\r\n    \"Noricum\": false,\r\n    \"X\": false,\r\n    \"VIII\": false,\r\n    \"VII\": false,\r\n    \"VI\": false,\r\n    \"IV\": false,\r\n    \"V\": false,\r\n    \"I\": false,\r\n    \"III\": false,\r\n    \"Sicilia\": false,\r\n    \"Pannonia Superior\": false,\r\n    \"Pannonia Inferior\": false,\r\n    \"Dalmatia\": false,\r\n    \"II\": false,\r\n    \"Sardinia et Corsica\": false,\r\n    \"Moesia Superior\": false,\r\n    \"Dacia\": false,\r\n    \"Moesia Inferior\": false,\r\n    \"Thracia\": false,\r\n    \"Macedonia\": false,\r\n    \"Achaia\": false,\r\n    \"Bithynia et Pontus\": false,\r\n    \"Cilicia\": false,\r\n    \"Creta et Cyrene\": false,\r\n    \"Cyprus\": false,\r\n    \"Aegyptus\": false,\r\n    \"Arabia\": false,\r\n    \"Iudaea\": false,\r\n    \"Syria\": false,\r\n    \"Africa Proconsularis\": false,\r\n    \"Numidia\": false,\r\n    \"Mauretania Caesariensis\": false,\r\n    \"Mauretania Tingitana\": false,\r\n    \"Galatia et Cappadocia\": false,\r\n    \"Lycia et Pamphylia\": false,\r\n    \"Asia\": false,\r\n    \"Armenia Mesopotamia\": false\r\n  }\r\n}";
    
    @Test
    public void loadFactionsTest() {
        Game g = new Game();
        initialSetup(g);
        ArrayList<Faction> factions = g.getFactions();
        assertEquals(factions.get(0).getName(), "Gaul");
        assertEquals(factions.get(1).getName(), "Rome");
    }

    @Test
    public void provinceOwnerShipTest() {
        Game g = new Game();
        initialSetup(g);
        ArrayList<Faction> factions = g.getFactions();
        assertEquals(factions.get(0).getNumProvinces(), 28);
        assertEquals(factions.get(1).getNumProvinces(), 25);
    }

    @Test
    public void setPlayerTest() {
        Game g = new Game();
        initialSetup(g);
        ArrayList<Faction> factions = g.getFactions();
        assertEquals(factions.get(1).isPlayer(), true);
    }

    @Test 
    public void testTurn() {
        Game g = new Game();
        initialSetup(g);
        Faction prev = g.getCurrentFaction();
        assertEquals(g.getCurrentYear(), -200);
        g.endTurn();
        assertEquals(g.getCurrentYear(), -199);
        if (prev.getName().equals("Rome")) {
            assertEquals(g.getCurrentFaction().getName(), "Gaul");
        } else {
            assertEquals(g.getCurrentFaction().getName(), "Rome");
        }
    }

    @Test 
    public void testAdjacencyMatrix() {
        Game g = new Game();
        initialSetup(g);
        // TODO
    }

    @Test
    public void testSaveLoad(){
        Game g = new Game();
        initialSetup(g);
        Goal prevGoal = g.getVictoryCondition();
        g.saveGame("testSave");
        Game g2 = new Game();
        g2.loadGame("testSave");
        assertEquals(g2.getCurrentYear(), -200);
        assertEquals(g.getFactions(), g2.getFactions());
        assertEquals(g.getCurrentFaction(), g2.getCurrentFaction());
        assertEquals(g2.getFactions().get(1).isPlayer(), true);
        assertEquals(g.getAdjacencyMatrix(), g2.getAdjacencyMatrix());
        assertEquals(g2.getVictoryCondition(), prevGoal);
        assertEquals(g.getMovedUnits(), g2.getMovedUnits());
        assertEquals(g.getToRecalculateBonuses(), g2.getToRecalculateBonuses());
        assertEquals(g.getProvincesInvadedThisTurn(), g2.getProvincesInvadedThisTurn());
    }


    public void initialSetup(Game g) {
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        g.initialiseGame(ownership, landlocked, adjacencyMap);
        g.selectFaction("Rome");
        g.selectFaction("Gaul");
        g.startGame();
    }

}