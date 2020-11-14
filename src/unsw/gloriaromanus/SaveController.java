package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class SaveController {
    @FXML
    private Label title;

    @FXML
    private TextField saveName;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    private Label status;

    @FXML
    private Label instruction;

    @FXML
    private ListView<ListCell<String>> saveListView;

    private Game game;

    private GloriaRomanusController gloriaRomanusController;

    private boolean listHasCurrentName() {
        for (ListCell<String> lc : saveListView.getItems()) {
            if (lc.getText().equals(saveName.getText()))
                return true;
        }
        return false;
    }

    @FXML
    private void handleSaveButton() {
        if (game.saveGame(saveName.getText())) {
            status.setText("SAVE SUCCESS");
            status.setTextFill(Paint.valueOf("#12b22b"));
            if (!listHasCurrentName())
                saveListView.getItems().add(newCell());
        } else {
            status.setText("SAVE FAILED");
            status.setTextFill(Paint.valueOf("#ff2d00"));
        }
    }

    @FXML
    private void handleBackButton() {
        gloriaRomanusController.closeSaveMenu();
        status.setText("");
    }

    @FXML
    private void initialize() {
        instruction.setWrapText(true);

        saveListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ListCell<String>> ov, ListCell<String> old_val, ListCell<String> new_val) -> {
            //System.out.println(saveName);
            if (saveListView.getSelectionModel().getSelectedItem() != null){
                saveName.setText(saveListView.getSelectionModel().getSelectedItem().getText());
            }
        });
    }

    public SaveController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    public void populateSaveList() {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu HHmm");
        saveName.setText(LocalDateTime.now().format(formatters));
        saveListView.getItems().clear();
        File savesDirectory = new File("saves");
        File[] savesList = savesDirectory.listFiles();
        if (savesList != null) {
            for (File save : savesList) {
                saveListView.getItems().add(newCell(save.getName()));
            }
        }
        // NEW INSTANCE (OF SELECTED ITEM) CREATED EACH TIME SAVE BUTTON IS PRESSED
        // maybe have to clear selection?
        // whjen we press save game it thinks the prev selected thing is being selected?
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private ListCell<String> newCell() {
        ListCell<String> cell = new ListCell<String>();
        cell.setText(saveName.getText());
        return cellSetup(cell);
    }

    private ListCell<String> newCell(String name) {
        ListCell<String> cell = new ListCell<String>();
        cell.setText(name);
        return cellSetup(cell);
    }

    private ListCell<String> cellSetup(ListCell<String> cell) {
        cell.setFont(new Font("Roman SD", 12));
        cell.setPrefWidth(170);
        cell.setStyle("-fx-background-color: transparent");
        return cell;
    }
}
