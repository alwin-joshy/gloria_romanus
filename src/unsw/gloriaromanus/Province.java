package unsw.gloriaromanus;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.Serializable;
import java.lang.Math;
import java.util.ArrayList;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.fields.AllIndexesSelector;

public class Province implements Serializable {
    private String name;
    private Faction faction;
    private ArrayList<Unit> units;
    private ArrayList<Infrastructure> infrastructure;
    private int wealth;
    private int wealthGrowth;
    private Tax tax;
    private ArrayList<ProjectDetails> projects;
    private int unitsInTraining;
    private int unitTrainingLimit;
    private Boolean isSeaProvince;

    public Province(String name, Faction faction) {
        this.name = name;
        this.faction = faction;
        units = new ArrayList<Unit>();
        infrastructure = new ArrayList<Infrastructure>();
        infrastructure.add(new Road(this));
        wealth = 100;
        wealthGrowth = 15;
        tax = new Tax(0.15, 0);
        projects = new ArrayList<ProjectDetails>();
        unitsInTraining = 0;
        unitTrainingLimit = 1;
        isSeaProvince = true;
    }

    public String getName() {
        return name;
    }

    public void setTax(double rate, int growthDelta) {
        tax = new Tax(rate, growthDelta);
    }

    public int applyTax() {
        wealth += wealthGrowth;
        if (isSeaProvince) wealth += faction.getPortBonus();
        int taxedAmount = (int) Math.round(wealth * tax.getRate());
        wealth -= taxedAmount;
        return (int) taxedAmount;
    }

    public void setLandlocked() {
        isSeaProvince = false;
    }

    public void build(Project project){
        double cost = project.getBaseCost();

        if (project instanceof Unit) {
            cost *= faction.getMineMultiplier();
        } else {
            cost *= faction.getMarketMultiplier();
        }

        int integerCost = (int) Math.round(cost);

        if (! faction.purchase(integerCost)) return;

        if (project instanceof Unit) {
            if (unitsInTraining == unitTrainingLimit) return;
            unitsInTraining++;
        } else if (buildingInfrastructure()) {
            return;
        }
 
        ProjectDetails p = new ProjectDetails(faction, project);
        projects.add(p);
    }

    public void updateProjects() {
        for (ProjectDetails project : projects) {
            if (project.decrementTurnsRemaining()){
                Project p = project.getProject();
                if (p instanceof Infrastructure) {
                    if (p instanceof Farm) unitTrainingLimit = ((Farm) p).getBonus();
                    Infrastructure inf = (Infrastructure) p;
                    inf.levelUp();
                    infrastructure.add(inf);
                } else {
                    units.add((Unit) p);
                }
                projects.remove(project);
            }
        }
    }

    private boolean buildingInfrastructure(){
        for (ProjectDetails project : projects) {
            if (project.getProject() instanceof Infrastructure) return true; 
        }
        return false;
    }

    public boolean checkSeaProvince() {
        return isSeaProvince;
    }

	public Market getMarket() {
		for (Infrastructure i : infrastructure) {
            if (i instanceof Market) return (Market) i;
        }
        return null;
    }
    
    public Port getPort() {
        for (Infrastructure i : infrastructure) {
            if (i instanceof Port) return (Port) i;
        }
        return null;
    }

    public Mine getMine() {
        for (Infrastructure i : infrastructure) {
            if (i instanceof Mine) return (Mine) i;
        }
        return null;
    }

    public int getWealth() {
        return wealth;
    }

    public boolean maxedInfrastructure() {
        if (infrastructure.size() != 8) return false;
        for (Infrastructure in : infrastructure) {
            if (!checkMaxLevel(in)) {
                return false; 
            }
        }
        return true;
    }

    public boolean checkMaxLevel(Infrastructure in) {
        if (! faction.getName().equals("Roman")) {
            if (in instanceof Road && in.getLevel() != 3 ) return false; 
        } else {
            if (in.getLevel() != 4) return false; 
        }
        return true; 
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit u) {
        units.add(u);
    }

    public void removeUnit(Unit u) {
        units.remove(u);
    }

}
