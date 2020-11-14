package unsw.gloriaromanus;

import java.io.IOException;
import java.io.Serializable;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
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
    private SmithLevel currentSmithLevel;
    private double taxPublicOrderDebuff;
    private double unitPublicOrderDebuff;
    private double publicOrder;

    public Province(String name, BuildingObserver buildingObserver) {
        this.name = name;
        this.faction = null;
        this.buildingObserver = buildingObserver;
        units = new ArrayList<Unit>();
        infrastructure = new ArrayList<Infrastructure>();
        ArrayList<Infrastructure> levelZero = new ArrayList<Infrastructure>(Arrays.asList(new Road(this), new Farm(), new Market(), new Smith(), new Mine(), new Walls(), new TownHall(), new Port()));
        infrastructure.addAll(levelZero);
        wealth = 100;
        wealthGrowth = 15;
        tax = new Tax(0.15, 0);
        projects = new ArrayList<ProjectDetails>();
        numLegionaryDeaths = 0;
        unitsInTraining = 0;
        unitTrainingLimit = 1;
        isSeaProvince = true;
        currentSmithLevel = new SmithLevelZero();
        taxPublicOrderDebuff = 0.3;
        unitPublicOrderDebuff = 0;
        publicOrder = 0.7;
    }

    public ArrayList<ProjectDetails> getProjects() {
        return projects;
    }

    public String getName() {
        return name;
    }

    public boolean isFullyUpgraded() {
        for (Infrastructure i : infrastructure) {
            if (i instanceof Road) {
                if (faction.getName().equals("Rome"))
                    if (i.getLevel() != 3) return false;
                else if (i.getLevel() != 2) return false;
            } else {
                if (i.getLevel() != 4) return false;
            }
        }
        return true;
    }

    public ArrayList<Infrastructure> getInfrastructure() {
        return infrastructure;
    }

    public void setTax(String type) {
        switch (type) {
            case "low" :
                tax.setRate(0.10);
                tax.setWealthGrowthDelta(10);
                taxPublicOrderDebuff = 0.15;
                break;
            case "normal" :
                tax.setRate(0.15);
                tax.setWealthGrowthDelta(0);
                taxPublicOrderDebuff = 0.3;
                break;
            case "high" :
                tax.setRate(0.20);
                tax.setWealthGrowthDelta(-10);
                taxPublicOrderDebuff = 0.4;
                break;
            case "very high" :
                tax.setRate(0.25);
                tax.setWealthGrowthDelta(-30);
                taxPublicOrderDebuff = 0.55;
                break;
            default:
                System.out.println("Fatal error. Exiting...");
                System.exit(1);
        }
        checkRevoltStatus();
    }

    public void checkRevoltStatus() {
        updatePublicOrder();
        if ( publicOrder < 0.3 && ! faction.couldRevolt(this)) {
            faction.addPossibleRevolt(this);
        } else if (publicOrder >= 0.3 && faction.couldRevolt(this)) {
            faction.removePossibleRevolt(this);
        }
    }

    public void updatePublicOrder() {
        publicOrder = 1 - taxPublicOrderDebuff - unitPublicOrderDebuff + getTownHallBuff();
    }

    public double getPublicOrder() {
        return publicOrder;
    }

    private double getTownHallBuff() {
        if (getTownHall() != null) {
            return getTownHall().getBuff();
        }
        return 0;
    }

    private TownHall getTownHall() {
        for (Infrastructure inf : infrastructure) {
            if (inf instanceof TownHall) return (TownHall) inf;
        }
        return null;
    }

    public int applyTax() {
        wealth += (wealthGrowth + tax.getWealthGrowthDelta());
        if (isSeaProvince) wealth += faction.getPortBonus();
        double taxedAmount = Math.round(wealth * tax.getRate());
        wealth -= taxedAmount;
        return (int) taxedAmount;
    }

    public boolean isVeryHighTax() {
        return tax.isVeryHighTax();
    }

    public void setLandlocked() {
        isSeaProvince = false;
        Infrastructure toRemove = null;
        for (Infrastructure i : infrastructure) {
            if (i instanceof Port) toRemove = i;
        }
        infrastructure.remove(toRemove);
    }

    public int getInfrastructureCost(double baseCost) {
        return (int) Math.round(baseCost * faction.getMarketMultiplier());
    }

    public int getUnitCost(double baseCost) {
        return (int) Math.round(baseCost * faction.getMineMultiplier());
    }

    public int getConstructionTime(int baseTime) {
        baseTime -= faction.getMineTurnReduction();
        if (baseTime < 1) {
            baseTime = 1;
        }
        return baseTime;
    }

    public ProjectDetails getInfrastructureProjectDetails() {
        for (ProjectDetails pd : projects) {
            if (pd.getProject() instanceof Infrastructure) return pd;
        }
        return null;
    }

    public int getTurnsRemaining(Infrastructure inf){
        for (ProjectDetails pd :  projects) {
            if (pd.getProject() == inf) {
                return pd.getTurnsRemaining();
            }
        }
        return 0;
    }



    public ProjectDetails build(Project project){
        double cost = project.getBaseCost();
        int integerCost = 0;
        if (project instanceof Unit) {
            integerCost = getUnitCost(cost);
        } else {
            integerCost = getInfrastructureCost(cost);
        }

        if (project instanceof Unit) {
            if (unitsInTraining == unitTrainingLimit) return null;
            TroopProductionBuilding tb = getTroopProductionBuilding();
            if (tb == null || ! tb.isAvailable((Unit) project)) return null; 
        } else if (buildingInfrastructure()) {
            return null;
        }

        if (! faction.purchase(integerCost)) return null;

        
        if (project instanceof Unit) {
            unitsInTraining++;
        }

        ProjectDetails p = new ProjectDetails(faction.getMineTurnReduction(), project, integerCost);
        projects.add(p);

        return p;
    }

    public void updateProjects() {
        ArrayList<ProjectDetails> toRemove = new ArrayList<ProjectDetails>();
        for (ProjectDetails project : projects) {
            if (project.decrementTurnsRemaining()){
                Project p = project.getProject();
                if (p instanceof Infrastructure) {
                    Infrastructure inf = (Infrastructure) p;
                    inf.levelUp();
                    System.out.println("HERE");

                    if (inf instanceof Walls) {
                        ((Walls) inf).levelUpTowers(this);
                    } else if (inf instanceof Smith) {
                        currentSmithLevel.nextLevel(this);
                    } else if (inf instanceof TownHall) {
                        checkRevoltStatus();
                    } else if (inf instanceof Farm) {
                        System.out.println(((Farm) inf).getBonus());
                        unitTrainingLimit = ((Farm) inf).getBonus();
                    }

                    if (inf instanceof WealthGenerationBuilding) {
                        wealth += 150 * inf.getLevel();
                        wealthGrowth += 50 * inf.getLevel();
                        buildingObserver.update(faction);
                    }

                } else {
                    units.add((Unit) p);
                    ((Unit) p).setSmithLevel(currentSmithLevel);
                    System.out.println(((Unit) p).getSmithLevel());
                    unitsInTraining--;
                    unitPublicOrderDebuff += 0.03;
                    checkRevoltStatus();
                }
                toRemove.add(project);
            }
        }
        for (ProjectDetails p : toRemove) {
            projects.remove(p);
        }
    }

    public void cancelOrder(ProjectDetails pd) {
        Project project = pd.getProject();
        double cost = (double) pd.getPricePaid();
        if (project instanceof Unit) {
            faction.increaseTreasury((int) Math.round(0.2 * cost * pd.getTurnsRemaining()));
            unitsInTraining--;
        } else
            faction.increaseTreasury((int) Math.round(0.05 * cost * pd.getTurnsRemaining()));
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

    public void replaceArcherTowers() {
        ArrayList<Unit> toRemove = new ArrayList<Unit>();
        for (Unit u : units) {
            if (u.getName().equals(name)) {
                toRemove.add(u);
                units.add(new Unit("ballistatower"));
            }
        }
        units.removeAll(toRemove);
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

    public void setSmithLevel(SmithLevel sl) {
        currentSmithLevel = sl;
    }

    public void setFaction(Faction f) {
        projects.clear();
        TroopProductionBuilding newTroop = new TroopProductionBuilding(f);
        if (getTroopProductionBuilding() != null) {
            for (int i = 0; i < getTroopProductionBuilding().getLevel(); i++) {
                newTroop.levelUp();
            }
        } 
        infrastructure.add(newTroop);
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

    public int getTotalTroops() {
        int total = 0;
        for (Unit u : units) {
            total += u.getNumTroops();
        }
        return total;
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
