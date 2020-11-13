package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;

public class UnitDetails {
    private Province province;
    private Unit unit;

    public UnitDetails(Province province, Unit unit) {
        this.province = province;
        this.unit = unit;
    }

    public String getName() {
        return unit.getName();
    }

    public String getType() {
        return unit.getType();
    }

    public int getLevel() throws IOException {
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/units/unitLevels.json"));
        JSONArray unitLevels = new JSONArray(content);
        for (int i = 0; i < unitLevels.length(); i++) {
            for (int j = 0; j < unitLevels.getJSONArray(i).length(); j++) {
                if (getName().equals(unitLevels.getJSONArray(i).get(j)))
                    return i + 1;
            }
        }
        return 0;
    }
    
    public int getCost() {
        return (int) Math.round(unit.getBaseCost() * province.getFaction().getMineMultiplier());
    }

    public int getTrainingTime() {
        return unit.getTrainingTime();
    }

    public Unit getUnit() {
        return unit;
    }

    public int getTurnsRemaining() {
        for (ProjectDetails pd : province.getProjects()) {
            if (pd.getProject() instanceof Unit) {
                if (((Unit) pd.getProject()) == unit) 
                    return pd.getTurnsRemaining();
            }
        }
        return 0;
    }
}
