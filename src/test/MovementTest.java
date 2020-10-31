package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.*;

public class MovementTest {
    private String initialOwnership = "{\r\n    \"Rome\": [\r\n        \"I\"\r\n    ],\r\n    \"Gaul\": [\r\n        \"A\",\r\n        \"B\",\r\n        \"C\",\r\n        \"D\",\r\n        \"E\",\r\n        \"F\",\r\n        \"G\",\r\n        \"H\"\r\n    ]\r\n}";
    private String landlockedString = "[]";
    private String adjacencyString = "{\r\n    \"A\": {\r\n        \"B\": true\r\n    },\r\n    \"B\": {\r\n        \"A\": true,\r\n        \"C\": true\r\n    },\r\n    \"C\": {\r\n        \"B\": true,\r\n        \"D\": true\r\n    },\r\n    \"D\": {\r\n        \"E\": true,\r\n        \"C\": true\r\n    },\r\n    \"E\": {\r\n        \"I\": true,\r\n        \"F\": true,\r\n        \"D\": true\r\n    },\r\n    \"F\": {\r\n        \"E\": true,\r\n        \"G\": true\r\n    },\r\n    \"G\": {\r\n        \"F\": true,\r\n        \"H\": true\r\n    },\r\n    \"H\": {\r\n        \"G\": true,\r\n        \"I\": true\r\n    },\r\n    \"I\": {\r\n        \"H\": true,\r\n        \"E\": true\r\n    }\r\n}";
 
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
    }

    @Test
    public void cavalryMovementTest() {
        Game g = new Game();
        initialSetup(g);
    }
}
