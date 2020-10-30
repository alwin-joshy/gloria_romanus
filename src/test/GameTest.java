package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jdk.jfr.Timestamp;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import unsw.gloriaromanus.*;

public class GameTest {

    @Test
    public void loadFactionsTest() {
        Game g = new Game();
        initialSetup(g);
        ArrayList<Faction> factions = g.getFactions();
        assertEquals(factions.get(0).getName(), "Rome");
        assertEquals(factions.get(1).getName(), "Gaul");
    }

    @Test
    public void provinceOwnerShipTest() {
        Game g = new Game();
        initialSetup(g);
        ArrayList<Faction> factions = g.getFactions();
        assertEquals(factions.get(0).getNumProvinces(), 25);
        assertEquals(factions.get(1).getNumProvinces(), 28);
    }

    @Test
    public void setPlayerTest() {
        Game g = new Game();
        initialSetup(g);
        ArrayList<Faction> factions = g.getFactions();
        g.selectFaction("Rome");
        assertEquals(factions.get(0).isPlayer(), true);
        assertEquals(factions.get(1).isPlayer(), false);
    }

    @Test 
    public void testTurn() {
        Game g = new Game();
        initialSetup(g);
        Faction prev = g.getCurrentFaction();
        assertEquals(g.getCurrentYear(), -200);
        g.endTurn();
        assertEquals(g.getCurrentFaction(), -199);
        assertNotSame(g.getCurrentFaction(), prev);
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
        g.selectFaction("Rome");
        g.saveGame("testSave");
        Game g2 = new Game();
        g2.loadGame("testSave");
        assertEquals(g2.getCurrentYear(), -200);
        assertArrayEquals(g.getFactions(), g2.getFactions());
        assertEquals(g.getCurrentFaction(), g2.getCurrentFaction());
        assertEquals(g2.getFactions().get(0).isPlayer(), true);
        assertEquals(g.getAdjacencyMatrix(), g2.getAdjacencyMatrix());
    }





    public ArrayList<Faction> initialSetup(Game g) {
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
        JSONObject ownership = new JSONObject(content);
        content = Files.readString(Paths.get("src/unsw/gloriaromanus/landlocked_provinces.json"));
        JSONObject landlocked = new JSONObject(content);
        content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        JSONObject adjacencyMap = new JSONObject(content);
        g.startGame(ownership, landlocked, adjacencyMap);
    }



}