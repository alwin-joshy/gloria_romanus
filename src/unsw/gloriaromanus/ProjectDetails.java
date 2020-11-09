package unsw.gloriaromanus;

import java.io.Serializable;

public class ProjectDetails implements Serializable {
    private Project project;
    private int turnsRemaining;
    private int pricePaid;

    public ProjectDetails(int mineTurnReduction, Project project, int pricePaid) {
        this.project = project;
        this.pricePaid = pricePaid;
        turnsRemaining = project.getBaseConstructionTime();
        if (project instanceof Infrastructure) {
            turnsRemaining -= mineTurnReduction;
        }
        if (turnsRemaining < 1) {
            turnsRemaining = 1;
        }
    }

    public Project getProject() {
        return project;
    }

    public boolean decrementTurnsRemaining() {
        turnsRemaining--;
        if (turnsRemaining == 0)
            return true;
        return false;
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }


    public int getPricePaid() {
        return pricePaid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        ProjectDetails pd = (ProjectDetails) obj;
        return project.equals(pd.getProject()) && turnsRemaining == pd.getTurnsRemaining() && pricePaid == pd.getPricePaid();
    }

    
}