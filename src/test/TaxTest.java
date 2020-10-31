package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.*;

public class TaxTest {
    private String initialOwnership = "{\r\n  \"Rome\": [\r\n    \"Lugdunensis\" ],\r\n  \"Gaul\": [\r\n    \"Achaia\" ]\r\n}";
    private String landlockedString = "[\r\n \"Germania Superior\",\r\n \"Raetia\",\r\n \"Alpes Graiae et Poeninae\",\r\n \"Alpes Cottiae\",\r\n \"XI\",\r\n \"Noricum\",\r\n \"Pannonia Superior\",\r\n \"Pannonia Inferior\",\r\n \"Moesia Superior\",\r\n \"Dacia\"\r\n]\r\n";
    private String adjacencyString = "{\r\n  \"Britannia\": {\r\n    \"Britannia\": false,\r\n    \"Lugdunensis\": true,\r\n    \"Belgica\": false,\r\n    \"Germania Inferior\": false,\r\n    \"Aquitania\": false,\r\n    \"Germania Superior\": false,\r\n    \"Alpes Graiae et Poeninae\": false,\r\n    \"XI\": false,\r\n    \"Alpes Cottiae\": false,\r\n    \"Alpes Maritimae\": false,\r\n    \"IX\": false,\r\n    \"Narbonensis\": false,\r\n    \"Tarraconensis\": false,\r\n    \"Baetica\": false,\r\n    \"Lusitania\": false,\r\n    \"Raetia\": false,\r\n    \"Noricum\": false,\r\n    \"X\": false,\r\n    \"VIII\": false,\r\n    \"VII\": false,\r\n    \"VI\": false,\r\n    \"IV\": false,\r\n    \"V\": false,\r\n    \"I\": false,\r\n    \"III\": false,\r\n    \"Sicilia\": false,\r\n    \"Pannonia Superior\": false,\r\n    \"Pannonia Inferior\": false,\r\n    \"Dalmatia\": false,\r\n    \"II\": false,\r\n    \"Sardinia et Corsica\": false,\r\n    \"Moesia Superior\": false,\r\n    \"Dacia\": false,\r\n    \"Moesia Inferior\": false,\r\n    \"Thracia\": false,\r\n    \"Macedonia\": false,\r\n    \"Achaia\": false,\r\n    \"Bithynia et Pontus\": false,\r\n    \"Cilicia\": false,\r\n    \"Creta et Cyrene\": false,\r\n    \"Cyprus\": false,\r\n    \"Aegyptus\": false,\r\n    \"Arabia\": false,\r\n    \"Iudaea\": false,\r\n    \"Syria\": false,\r\n    \"Africa Proconsularis\": false,\r\n    \"Numidia\": false,\r\n    \"Mauretania Caesariensis\": false,\r\n    \"Mauretania Tingitana\": false,\r\n    \"Galatia et Cappadocia\": false,\r\n    \"Lycia et Pamphylia\": false,\r\n    \"Asia\": false,\r\n    \"Armenia Mesopotamia\": false\r\n  }\r\n}";

    @Test
    public void defaultTaxTest() {
        Game g = new Game();
        initialSetup(g);
        Faction currentFaction = g.getCurrentFaction();
        Province p = currentFaction.getNthProvince(0);
        g.selectFaction(currentFaction.getName());
        int startingWealth = p.getWealth();
        int startingWealthGrowth = p.getWealthGrowth();
        int startingTreasury = currentFaction.getTreasury();
        g.endTurn();
        assertEquals(currentFaction.getTreasury(), Math.round(startingTreasury + ((startingWealth + startingWealthGrowth) * 0.15)));
        assertEquals(p.getWealth(), (startingWealth + startingWealthGrowth) - Math.round( 0.15 * (startingWealth + startingWealthGrowth)));
    }

    @Test
    public void lowTaxTest() {
        Game g = new Game();
        initialSetup(g);
        Faction currentFaction = g.getCurrentFaction();
        Province p = currentFaction.getNthProvince(0);
        g.selectFaction(currentFaction.getName());
        int startingWealth = p.getWealth();
        int startingWealthGrowth = p.getWealthGrowth();
        int startingTreasury = currentFaction.getTreasury();
        p.setTax("low");
        g.endTurn();
        assertEquals(currentFaction.getTreasury(), Math.round(startingTreasury + ((startingWealth + startingWealthGrowth + 10) * 0.1)));
        assertEquals(p.getWealth(), (startingWealth + startingWealthGrowth + 10) - Math.round( 0.1 * (startingWealth + startingWealthGrowth + 10)));
    }

    @Test
    public void highTaxTest() {
        Game g = new Game();
        initialSetup(g);
        Faction currentFaction = g.getCurrentFaction();
        Province p = currentFaction.getNthProvince(0);
        g.selectFaction(currentFaction.getName());
        int startingWealth = p.getWealth();
        int startingWealthGrowth = p.getWealthGrowth();
        int startingTreasury = currentFaction.getTreasury();
        p.setTax("high");
        g.endTurn();
        assertEquals(currentFaction.getTreasury(), Math.round(startingTreasury + ((startingWealth + startingWealthGrowth - 10) * 0.2)));
        assertEquals(p.getWealth(), (startingWealth + startingWealthGrowth - 10) - Math.round( 0.2 * (startingWealth + startingWealthGrowth - 10)));
    }

    @Test
    public void veryHighTaxTest() {
        Game g = new Game();
        initialSetup(g);
        Faction currentFaction = g.getCurrentFaction();
        Province p = currentFaction.getNthProvince(0);
        g.selectFaction(currentFaction.getName());
        int startingWealth = p.getWealth();
        int startingWealthGrowth = p.getWealthGrowth();
        int startingTreasury = currentFaction.getTreasury();
        p.setTax("very high");
        g.endTurn();
        assertEquals(currentFaction.getTreasury(), Math.round(startingTreasury + ((startingWealth + startingWealthGrowth - 30) * 0.25)));
        assertEquals(p.getWealth(), (startingWealth + startingWealthGrowth - 30) - Math.round( 0.25 * (startingWealth + startingWealthGrowth - 30)));
    }

    public void initialSetup(Game g) {
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        g.startGame(ownership, landlocked, adjacencyMap);
    }
}
