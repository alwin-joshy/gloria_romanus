package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONObject;

public class EngagementObserver {
    private GloriaRomanusController controller;
    private HashMap<String, String> factionMap;

    public EngagementObserver(GloriaRomanusController controller) throws IOException {
        this.controller = controller;
        factionMap = new HashMap<String, String>();
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/factionMappings.json"));
        JSONObject map = new JSONObject(content);
        for (String faction : map.keySet()) {
            factionMap.put(faction, map.getString(faction));
        }
    }

    public void notifyBattle(String attacker, String defender) {
        controller.printMessageToTerminal("Commencing battle between " + attacker + " and " + defender);
    }

    public void notifySkirmish(String attacker, String defender, String attackingFaction, String defendingFaction) {
        controller.printMessageToTerminal("Commencing skirmish between " + factionMap.get(attackingFaction) + " " + attacker + " and " + factionMap.get(defendingFaction) + " " + defender);
    }

    public void notifyEngagement(String attacker, int attackerDamage, String defender, String attackingFaction, String defendingFaction) {
        controller.printMessageToTerminal(factionMap.get(attackingFaction) + " " + attacker + " dealt " + attackerDamage + " damage to " + factionMap.get(defendingFaction) + " " + defender);
    }

    public void notifyBreak(String unit, String faction) {
        controller.printMessageToTerminal(factionMap.get(faction) + " " + unit + "has been broken");
    }

    public void notifyRoute(String unit, String faction) {
        controller.printMessageToTerminal(factionMap.get(faction) + " " + unit + " has routed");
    }
}
