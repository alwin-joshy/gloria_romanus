package unsw.gloriaromanus;

import javax.swing.text.TabableView;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InfrastructureController {

    @FXML
    private TableView<Infrastructure> builtInfrastructure;

    @FXML
    private TableColumn<Infrastructure, String> buildingNameColumn;

    @FXML
    private TableColumn<Infrastructure, Integer> builtLevelColumn;

    private Province province;
    private GloriaRomanusController gloriaRomanusController;

    public InfrastructureController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    @FXML
    private void initialize() {
        buildingNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        builtLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        //builtInfrastructure.getItems().add(new Market());
    }

    public void setupScreen(Province p)  {
        builtInfrastructure.getItems().clear();
        builtInfrastructure.getItems().addAll(p.getInfrastructure());
    }
    
}
