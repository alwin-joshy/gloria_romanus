package unsw.gloriaromanus;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SelectUnitsController {
    @FXML
    private TableView<UnitDetails> availableUnitsTable;

    @FXML
    private TableColumn<UnitDetails, String> name;

    @FXML
    private TableColumn<UnitDetails, String> type;

    @FXML
    private TableColumn<UnitDetails, Integer> movementPoints;

    @FXML
    private TableColumn<UnitDetails, Integer> numTroops;

    @FXML
    private TableColumn<UnitDetails, Integer> smithLevel;

    @FXML
    private Button actionButton;

    @FXML
    private Button backButton;

    private GloriaRomanusController gloriaRomanusController;
    private Province start;
    private Province end;

    public SelectUnitsController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    @FXML
    private void handleBackButton() {
        gloriaRomanusController.closeSelectUnitsMenu();
        gloriaRomanusController.resetTargetSelection();
    }

    @FXML
    private void handleActionButton() throws IOException {
        ObservableList<UnitDetails> selected = availableUnitsTable.getSelectionModel().getSelectedItems();
        ArrayList<Unit> toMove = new ArrayList<Unit>();
        for (UnitDetails ud : selected) {
            toMove.add(ud.getUnit());
        }
        gloriaRomanusController.moveUnits(toMove, start, end);
        gloriaRomanusController.closeSelectUnitsMenu();
    }


    @FXML
    public void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        movementPoints.setCellValueFactory(new PropertyValueFactory<>("remainingMovementPoints"));
        numTroops.setCellValueFactory(new PropertyValueFactory<>("numTroops"));
        smithLevel.setCellValueFactory(new PropertyValueFactory<>("smithLevel"));
        availableUnitsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        actionButton.setDisable(true);
        availableUnitsTable.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (availableUnitsTable.getSelectionModel().getSelectedItem() != null) {
                actionButton.setDisable(false);
            } else {
                actionButton.setDisable(true);
            }
        });
    }

    public void listSetup(Province start, Province end) {
        this.start = start;
        this.end = end;
        availableUnitsTable.getItems().clear();
        for (Unit u : start.getUnits()) {
            if (! u.getType().equals("tower"))
                availableUnitsTable.getItems().add(new UnitDetails(start, u));
        }
        if (start.getFactionName().equals(end.getFactionName())) {
            actionButton.setText("MOVE");
        } else {
            actionButton.setText("INVADE");
        }
    }
    
}
