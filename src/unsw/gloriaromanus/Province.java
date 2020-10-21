package unsw.gloriaromanus;

import java.lang.Math;
import java.util.ArrayList;

public class Province {
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
        wealth = 100;
        wealthGrowth = 10;
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
        if (project instanceof Unit){
            if (unitsInTraining == unitTrainingLimit) return;
            unitsInTraining++;
        } else if (buildingInfrastructure()) {
            return;
        }
        ProjectDetails p = new ProjectDetails(project);
        projects.add(p);
    }

    public void updateProjects() {
        for (ProjectDetails project : projects) {
            if (project.decrementTurnsRemaining()){
                if (project.getProject() instanceof Infrastructure) {
                    infrastructure.add(project);
                } else {
                    units.add(project);
                }
                projects.remove(project);
            }
        }
    }



    private boolean buildingInfrastructure(){
        for (ProjectDetails project : projects) {
            if (project.getProject() instanceof Infrastructure)  return true; 
        }
        return false;
    }

    public boolean checkSeaProvince() {
        return isSeaProvince;
    }
}
