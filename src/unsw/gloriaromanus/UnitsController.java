package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class UnitsController {
    @FXML
    private GridPane statGrid;
    
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
    private Label quantity;
    
    @FXML
    private Label ranged;

    @FXML
    private Label armour;

    @FXML
    private Label morale;
    
    @FXML
    private Label defenseSkill;

    @FXML
    private Label shieldDefense;

    @FXML
    private Label speed;

    @FXML
    private Label movementPoints;

    @FXML
    private ListView<ListCell<String>> unitsInProvince;

    @FXML
    private TableView<UnitDetails> unitTable;

    @FXML
    private TableColumn<UnitDetails, String> unitName;

    @FXML
    private TableColumn<UnitDetails, Integer> unitLevel;

    @FXML
    private TableColumn<UnitDetails, String> unitType;

    @FXML
    private TableColumn<UnitDetails, Integer> unitCost;

    @FXML
    private TableColumn<UnitDetails, Integer> unitTime;

    @FXML
    private TableView<UnitDetails> unitsInTrainingTable;

    @FXML
    private TableColumn<UnitDetails, String> trainingUnitName;

    @FXML   
    private TableColumn<UnitDetails, Integer> turnsRemaining;

    private GloriaRomanusController gloriaRomanusController;

    private Province province;

    @FXML
    private void handleBackButton() {
        gloriaRomanusController.closeUnitsMenu();
        unitsInProvince.getItems().clear();
        unitsInTrainingTable.getItems().clear();
        unitTable.getItems().clear();
        quantity.setText("N/A");
        ranged.setText("N/A");
        armour.setText("N/A");
        morale.setText("N/A");
        defenseSkill.setText("N/A");
        shieldDefense.setText("N/A");
        speed.setText("N/A");
        movementPoints.setText("N/A");
        selectedUnit.setText("None");
    }

    @FXML
    private void handleDisbandButton() {
        ListCell<String> selected = unitsInProvince.getSelectionModel().getSelectedItem();
        province.removeUnit(new Unit(selected.getText()));
        unitsInProvince.getItems().remove(selected);
    }

    @FXML
    private void handleRecruitButton() {
        UnitDetails selected = unitTable.getSelectionModel().getSelectedItem();
        Unit toBuild = new Unit(selected.getUnit().getName());
        province.build(toBuild);
        unitsInTrainingTable.getItems().add(new UnitDetails(province, toBuild));
        updateTrainingCount();
        if (province.getUnitTrainingLimit() == province.getUnitsInTraining())
            recruitButton.setDisable(true);
        System.out.println(province.getFaction().getMineMultiplier());
    }

    @FXML
    private void handleCancelButton() {
        UnitDetails selected = unitsInTrainingTable.getSelectionModel().getSelectedItem();
        int turns = selected.getTurnsRemaining();
        for (ProjectDetails pd : province.getProjects()) {
            if (pd.getProject().equals(selected.getUnit()) && pd.getTurnsRemaining() == turns) {
                province.cancelOrder(pd);
                unitsInTrainingTable.getItems().remove(selected);
                break;
            }
        }
        updateTrainingCount();
    }

    private void updateTrainingCount() {
        unitsInTraining.setText(Integer.toString(province.getUnitsInTraining()) + " / " + Integer.toString(province.getUnitTrainingLimit()));
    }

    @FXML
    public void initialize() {
        unitsInTrainingTable.setPlaceholder(new Label());
        cancelButton.disableProperty().bind(unitsInTrainingTable.getSelectionModel().selectedItemProperty().isNull());
        unitTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object old_val, Object new_val) {
                UnitDetails selected = unitTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    Unit chosenUnit = selected.getUnit();
                    quantity.setText(Integer.toString(chosenUnit.getNumTroops()));
                    ranged.setText(Boolean.toString(chosenUnit.isRanged()));
                    armour.setText(Integer.toString(chosenUnit.getArmour()));
                    morale.setText(Integer.toString((int) chosenUnit.getMorale()));
                    defenseSkill.setText(Integer.toString(chosenUnit.getDefenceSkill()));
                    shieldDefense.setText(Integer.toString(chosenUnit.getShieldDefence()));
                    speed.setText(Integer.toString((int) chosenUnit.getSpeed()));
                    movementPoints.setText(Integer.toString(chosenUnit.getMovementPoints()));
                    selectedUnit.setText(chosenUnit.getName());
                    if (recruitButton.isDisabled())
                        recruitButton.setDisable(false);
                } else {
                    recruitButton.setDisable(true);
                }
            }
        });

        // set method to populate columns
        unitName.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitType.setCellValueFactory(new PropertyValueFactory<>("type"));
        unitLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        unitCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        unitTime.setCellValueFactory(new PropertyValueFactory<>("trainingTime"));
        turnsRemaining.setCellValueFactory(new PropertyValueFactory<>("turnsRemaining"));
        trainingUnitName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public UnitsController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    public void setupUnitDetails(Province province) throws IOException {
        this.province = province;
        updateTrainingCount();
        // populate current units
        for (Unit u : province.getUnits()) {
            ListCell<String> lc = new ListCell<String>();
            lc.setText(u.getName());
            lc.setPrefWidth(200);
            unitsInProvince.getItems().add(lc);
        }

        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/units/unitLevels.json"));
        JSONArray unitLevels = new JSONArray(content);
        // populate buy-able units
        for (int i = 0; i < province.getTroopProductionBuilding().getLevel(); i++) {
            JSONArray unitList = unitLevels.getJSONArray(i);
            for (int j = 0; j < unitList.length(); j++) {
                unitTable.getItems().add(new UnitDetails(province, new Unit((String) unitList.get(j))));
            }
        }

        // populate training units
        for (ProjectDetails pd : province.getProjects()) {
            if (pd.getProject() instanceof Unit) {
                unitsInTrainingTable.getItems().add(new UnitDetails(province, (Unit) pd.getProject()));
            }
        }
        recruitButton.setDisable(true);
    }

}