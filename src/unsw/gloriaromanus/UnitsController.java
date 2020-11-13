package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UnitsController {
    @FXML
    private Button disbandButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button backButton;

    @FXML
    private Button recruitButton;

    @FXML
    private Label unitsInTraining;

    @FXML
    private Label selectedUnit;

    @FXML
    private ListView<ListCell<String>> unitsInProvince;

    @FXML
    private TableView<Unit> unitTable;

    @FXML
    private TableColumn<Unit, String> unitName;

    @FXML
    private TableColumn<Unit, Integer> unitLevel;

    @FXML
    private TableColumn<Unit, String> unitType;

    @FXML
    private TableColumn<Unit, Integer> unitCost;

    @FXML
    private TableColumn<Unit, Integer> unitTime;

    @FXML
    private TableView<Unit> unitsInTrainingTable;

    @FXML
    private TableColumn<Unit, String> trainingUnitName;

    @FXML   
    private TableColumn<Unit, Integer> turnsRemaining;

    private GloriaRomanusController gloriaRomanusController;

    @FXML
    private void handleBackButton() {
        gloriaRomanusController.closeUnitsMenu();
        unitsInProvince.getItems().clear();
    }

    @FXML
    private void handleDisbandButton() {

    }

    @FXML
    private void handleRecruitButton() {

    }

    @FXML
    private void handleCancelButton() {
        
    }

    public UnitsController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    public void setupUnitDetails(Province p) {
        for (Unit u : p.getUnits()) {
            ListCell<String> lc = new ListCell<String>();
            lc.setText(u.getName());
            unitsInProvince.getItems().add(lc);
        }
    }

}