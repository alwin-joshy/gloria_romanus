package unsw.gloriaromanus;

import java.io.Serializable;

public class ProjectDetails implements Serializable {
    private Faction f;
    private Project project;
    private int turnsRemaining;

    public ProjectDetails(Faction f, Project project) {
        this.project = project;
        turnsRemaining = project.getConstructionTime();
        if (project instanceof Infrastructure) {
            turnsRemaining -= f.getMineTurnReduction();
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

    public Faction getFaction() {
        return f;
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("PDs");
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        ProjectDetails pd = (ProjectDetails) obj;
        if (f.getName().equals(pd.getFaction().getName()) && project.equals(pd.getProject()) && 
            turnsRemaining == pd.getTurnsRemaining())
            return true;
        return false;
    }

    
}