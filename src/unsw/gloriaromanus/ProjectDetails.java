package unsw.gloriaromanus;

import java.io.Serializable;

public class ProjectDetails implements Serializable{
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

    public boolean decrementTurnsRemaining(){
        turnsRemaining--;
        if (turnsRemaining == 0) return true;
        return false; 
    }
}