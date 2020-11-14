package unsw.gloriaromanus;

import java.io.IOException;

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
        TroopProductionBuilding tb = province.getTroopProductionBuilding();
        for (int i = 1; i <= tb.getLevel(); i++) {
            if (tb.getUnitsOfLevel(i).contains(getName())) {
                return i;
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
