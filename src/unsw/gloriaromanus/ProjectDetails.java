package unsw.gloriaromanus;

public class ProjectDetails {
    private Project project;
    private int turnsRemaining;

    public ProjectDetails(Project project) {
        this.project = project;
        turnsRemaining = project.getBaseConstructionTime();
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