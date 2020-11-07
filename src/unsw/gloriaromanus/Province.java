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
    private int numLegionaryDeaths;
    private int unitsInTraining;
    private int unitTrainingLimit;
    private Boolean isSeaProvince;
    private BuildingObserver buildingObserver;

    public Province(String name, Faction faction, BuildingObserver buildingObserver) {
        this.name = name;
        this.faction = faction;
        this.buildingObserver = buildingObserver;
        units = new ArrayList<Unit>();
        infrastructure = new ArrayList<Infrastructure>();
        infrastructure.add(new Road(this));
        wealth = 100;
        wealthGrowth = 15;
        tax = new Tax(0.15, 0);
        projects = new ArrayList<ProjectDetails>();
        numLegionaryDeaths = 0;
        unitsInTraining = 0;
        unitTrainingLimit = 1;
        isSeaProvince = true;
    }

    public String getName() {
        return name;
    }

    public void setTax(String type) {
        switch (type) {
            case "low" :
                tax.setRate(0.10);
                tax.setWealthGrowthDelta(10);
                break;
            case "normal" :
                tax.setRate(0.15);
                tax.setWealthGrowthDelta(0);
                break;
            case "high" :
                tax.setRate(0.20);
                tax.setWealthGrowthDelta(-10);
                break;
            case "very high" :
                tax.setRate(0.25);
                tax.setWealthGrowthDelta(-30);
                break;
            default:
                System.out.println("Fatal error. Exiting...");
                System.exit(1);
        }
    }

    public int applyTax() {
        wealth += (wealthGrowth + tax.getWealthGrowthDelta());
        if (isSeaProvince) wealth += faction.getPortBonus();
        double taxedAmount = Math.round(wealth * tax.getRate());
        wealth -= taxedAmount;
        return (int) taxedAmount;
    }

    public int isVeryHighTax() {
        return tax.isVeryHighTax();
    }

    public void setLandlocked() {
        isSeaProvince = false;
    }

    public boolean build(Project project){
        double cost = project.getBaseCost();
        if (project instanceof Unit) {
            cost *= faction.getMineMultiplier();
        } else {
            cost *= faction.getMarketMultiplier();
        }

        if (project instanceof Unit) {
            if (unitsInTraining == unitTrainingLimit) return false;
            TroopProductionBuilding tb = getTroopProductionBuilding();
            if (tb == null || ! tb.isAvailable((Unit) project)) return false; 
        } else if (buildingInfrastructure()) {
            return false;
        }

        int integerCost = (int) Math.round(cost);

        if (! faction.purchase(integerCost)) return false;
        
        if (project instanceof Unit) {
            unitsInTraining++;
        }

        ProjectDetails p = new ProjectDetails(faction.getMineTurnReduction(), project);
        projects.add(p);

        return true;
    }

    public void updateProjects() {
        ArrayList<ProjectDetails> toRemove = new ArrayList<ProjectDetails>();
        for (ProjectDetails project : projects) {
            if (project.decrementTurnsRemaining()){
                Project p = project.getProject();
                if (p instanceof Infrastructure) {
                    if (p instanceof Farm) unitTrainingLimit = ((Farm) p).getBonus();
                    Infrastructure inf = (Infrastructure) p;
                    if (inf.getLevel() == 0) infrastructure.add(inf);
                    inf.levelUp();
                    if (inf instanceof WealthGenerationBuilding) {
                        wealth += 150 * inf.getLevel();
                        wealthGrowth += 50 * inf.getLevel();
                        buildingObserver.update(faction);
                    }
                } else {
                    units.add((Unit) p);
                    unitsInTraining--;
                }
                toRemove.add(project);
            }
        }
        for (ProjectDetails p : toRemove) {
            projects.remove(p);
        }
    }

    private void cancelOrder(ProjectDetails pd) {
        Project project = pd.getProject();
        double baseCost = (double) project.getBaseCost();
        if (project instanceof Unit)
            faction.increaseTreasury((int) Math.round(0.2 * baseCost * pd.getTurnsRemaining()));
        else
            faction.increaseTreasury((int) Math.round(0.05 * baseCost * pd.getTurnsRemaining()));
        projects.remove(pd);
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
        if (! faction.getName().equals("Rome")) {
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

    public Infrastructure getNthInfrastructure(int n) {
        return infrastructure.get(n);
    }

    public Unit getNthUnit(int n) {
        return units.get(n);
    }

    public ProjectDetails getNthProject(int n) {
        return projects.get(n);
    }

    public int getWealthGrowth() {
        return wealthGrowth;
    }

    public Tax getTax() {
        return tax;
    }

    public int getUnitsInTraining() {
        return unitsInTraining;
    }

    public int getUnitTrainingLimit() {
        return unitTrainingLimit;
    }

    public boolean isSeaProvince() {
        return isSeaProvince;
    }

    public TroopProductionBuilding getTroopProductionBuilding(){
        for (Infrastructure i : infrastructure) {
            if (i instanceof TroopProductionBuilding) return (TroopProductionBuilding) i;
        }
        return null;
    }

    public double getLegionaryDebuff() {
        return faction.getLegionaryDebuff();
    }

    public void incrementNumLegionaryDeaths() {
        numLegionaryDeaths++;
        faction.incrementLegionaryDebuff();
    }

    public void resetLegionaryDeaths() {
        faction.decreaseLegionaryDebuff(numLegionaryDeaths);
        numLegionaryDeaths = 0;
    }

    public void setWealth(int gold) {
        wealth = gold;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Province p = (Province) obj;
        for (int i = 0; i < infrastructure.size(); i++) {
            if (!infrastructure.get(i).equals(p.getNthInfrastructure(i))) return false; 
        }
        for (int i = 0; i < units.size(); i++) {
            if (!units.get(i).equals(p.getNthUnit(i))) return false; 
        }
        for (int i = 0; i < projects.size(); i++) {
            if (!projects.get(i).equals(p.getNthProject(i))) return false; 
        }
        return name.equals(p.getName()) && faction.getName().equals(p.getFaction().getName()) && wealth == p.getWealth()
               && wealthGrowth == p.getWealthGrowth() && tax.equals(p.getTax()) && unitsInTraining == p.getUnitsInTraining()
               && unitTrainingLimit == p.getUnitTrainingLimit() && isSeaProvince == p.isSeaProvince();
    }

}
