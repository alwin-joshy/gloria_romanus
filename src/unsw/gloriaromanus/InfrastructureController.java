package unsw.gloriaromanus;

import javax.swing.text.TabableView;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InfrastructureController {

    @FXML
    private TableView<InfrastructureDetails> builtInfrastructure;

    @FXML
    private TableColumn<InfrastructureDetails, String> buildingNameColumn;

    @FXML
    private TableColumn<InfrastructureDetails, Integer> builtLevelColumn;

    @FXML 
    private TableView<InfrastructureDetails> currentProject;

    @FXML
    private TableColumn<InfrastructureDetails, String> currentProjectName;

    @FXML
    private TableColumn<InfrastructureDetails, String> currentProjectLevel;

    @FXML
    private TableColumn<InfrastructureDetails, String> currentProjectTurnsRemaining;

    @FXML 
    private TableView<InfrastructureDetails> upgrades;

    @FXML
    private TableColumn<InfrastructureDetails, String> upgradesName;

    @FXML
    private TableColumn<InfrastructureDetails, String> upgradesLevel;

    @FXML
    private TableColumn<InfrastructureDetails, String> upgradesCost;

    @FXML
    private TableColumn<InfrastructureDetails, String> upgradesConstructionTime;


    private GloriaRomanusController gloriaRomanusController;

    public InfrastructureController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    @FXML 
    private void handleBackButton() {

    }

    @FXML
    private void initialize() {
        buildingNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        builtLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        currentProjectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentProjectLevel.setCellValueFactory(new PropertyValueFactory<>("nextLevel"));
        currentProjectTurnsRemaining.setCellValueFactory(new PropertyValueFactory<>("turnsRemaining"));
        upgradesName.setCellValueFactory(new PropertyValueFactory<>("name"));
        upgradesLevel.setCellValueFactory(new PropertyValueFactory<>("nextLevel"));
        upgradesCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        upgradesConstructionTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    public void setupScreen(Province p)  {
        builtInfrastructure.getItems().clear();
        for (Infrastructure i : p.getInfrastructure()) {
            
            if (i.getLevel() != 0) {
                builtInfrastructure.getItems().add(new InfrastructureDetails(p, i));
            }
            if (! (i instanceof Road) && i.getLevel() != 4) {
                upgrades.getItems().add(new InfrastructureDetails(p, i));
            } else if (i instanceof Road && i.getLevel() < 2) {
                upgrades.getItems().add(new InfrastructureDetails(p, i));
            } else if (i instanceof Road && i.getLevel() == 2 && p.getFaction().getName().equals("Rome")) {
                upgrades.getItems().add(new InfrastructureDetails(p, i));
            }
        }
        Infrastructure inf = p.getInfrastructureProject();
        if (inf != null) {
            currentProject.getItems().add(new InfrastructureDetails(p, inf));
        }
    
    }

    public class InfrastructureDetails {
        private Province p;
        private Infrastructure inf;

        public InfrastructureDetails(Province p, Infrastructure inf) {
            this.p = p;
            this.inf = inf;
        }

        public String getName() {
            return inf.getName();
        }

        public int getLevel() {
            return inf.getLevel();
        }

        public int getCost() {
            return p.getInfrastructureCost(inf.getBaseCost());
        }

        public int getTime() {
            return p.getConstructionTime(inf.getBaseConstructionTime());
        }

        public int getNextLevel(){
            return inf.getLevel() + 1;
        }

        public int getTurnsRemaining(Infrastructure inf) {
            return p.getTurnsRemaining(inf);
        }

        

    }
    
}
