package unsw.gloriaromanus;

import com.esri.arcgisruntime.internal.io.handler.GlobalRequestHandler;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label setDescription;

    @FXML
    private Button buildButton;

    @FXML
    private Button cancelButton;


    private GloriaRomanusController gloriaRomanusController;
    private Province p;
    private ProjectDetails curr;
    private InfrastructureDetails currDetails;

    public InfrastructureController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    @FXML 
    private void handleBackButton() {
        curr = null;
        currDetails = null;
        gloriaRomanusController.closeInfrastructureMenu();
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
        curr = null;
        currDetails = null;

        setDescription.setWrapText(true);
        
        upgrades.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            InfrastructureDetails selected = upgrades.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setDescription.setText(selected.getDescription());
            }
            });

        buildButton.setDisable(true);
        cancelButton.setDisable(true);

        //buildButton.disableProperty().bind(Bindings.size(currentProject.getItems()).isEqualTo(1).and(upgrades.getSelectionModel().selectedItemProperty().isNotNull()));
        //cancelButton.disableProperty().bind(Bindings.size(currentProject.getItems()).isEqualTo(0).or(currentProject.getSelectionModel().selectedItemProperty().isNotNull()));
        buildButton.disableProperty().bind(upgrades.getSelectionModel().selectedItemProperty().isNull().or(Bindings.size(currentProject.getItems()).isEqualTo(1)));
        cancelButton.disableProperty().bind(currentProject.getSelectionModel().selectedItemProperty().isNull());

    }

    @FXML 
    private void handleCancelButton() {
        p.cancelOrder(curr);
        currentProject.getItems().remove(currDetails);
        upgrades.getItems().add(currDetails);
        curr = null;
        currDetails = null;
    }

    @FXML
    private void handleBuildButton() {
        ProjectDetails buildResult = p.build(upgrades.getSelectionModel().getSelectedItem().getInfrastructure());
        if (buildResult != null) {
            currDetails = upgrades.getSelectionModel().getSelectedItem();
            curr = buildResult;
            currentProject.getItems().add(currDetails);
            upgrades.getItems().remove(currDetails);
        } else {
            gloriaRomanusController.handleNotEnoughGold();
        }
    }




    public void setupScreen(Province p)  {
        this.p = p;
        builtInfrastructure.getItems().clear();
        upgrades.getItems().clear();
        currentProject.getItems().clear();
        ProjectDetails inf = p.getInfrastructureProjectDetails();
        if (inf != null) {
            currDetails = new InfrastructureDetails(p, (Infrastructure) inf.getProject());
            currentProject.getItems().add(currDetails);
            curr = inf;
        }
        for (Infrastructure i : p.getInfrastructure()) {
            if (curr != null && (Infrastructure) curr.getProject() == i) continue;

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

        public String getDescription() {
            return inf.getDescription();
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

        public int getTurnsRemaining() {
            return p.getTurnsRemaining(inf);
        }

        public Infrastructure getInfrastructure() {
            return inf;
        }

    }
    
}
