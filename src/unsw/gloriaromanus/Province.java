package unsw.gloriaromanus;

import java.io.Serializable;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

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
        wealth += wealthGrowth + tax.getWealthGrowthDelta();
        if (isSeaProvince) wealth += faction.getPortBonus();
        int taxedAmount = (int) Math.round(wealth * tax.getRate());
        wealth -= taxedAmount;
        return (int) taxedAmount;
    }

    public int isVeryHighTax() {
        return tax.isVeryHighTax();
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
                    if (inf instanceof WealthGenerationBuilding) {
                        wealth += 150 * inf.getLevel();
                        wealthGrowth += 50 * inf.getLevel();
                    }
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

    public boolean hasWalls() {
        for (Infrastructure i : infrastructure) {
            if (i instanceof Walls) return true;
        }
        return false;
    }

    public void destroyAllUnits() {
        units.clear();
    }

    public void destroyUnits(ArrayList<Unit> army, int numToDestroy) {
        int numDestroyed = 0;
        Random r = new Random();
        while (numDestroyed < numToDestroy) {
            int index = r.nextInt(units.size());
            Unit toDelete = units.get(index);
            if (! army.contains(toDelete)) continue;
            if (numDestroyed + toDelete.getNumTroops() > numToDestroy) {
                toDelete.takeDamage(numToDestroy - numDestroyed);
                numDestroyed += numToDestroy - numDestroyed;
                continue;
            }
            numDestroyed += toDelete.getNumTroops();
            units.remove(toDelete);
            army.remove(toDelete);
        }
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction f) {
        projects.clear();
        this.faction = f;
    }

}
