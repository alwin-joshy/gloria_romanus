package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class LoadGameController {
    private MainMenuScreen mainMenuScreen;
    private GloriaRomanusScreen gloriaRomanusScreen;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button loadButton;

    @FXML
    private ListView<ListCell<String>> saveList;

    @FXML
    public void initialize() {
        saveList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ListCell<String>> ov, ListCell<String> old_val, ListCell<String> new_val) -> {
            if (saveList.getSelectionModel().getSelectedItem() != null){
                if (old_val != null)
                    old_val.setTextFill(Paint.valueOf("black"));
                saveList.getSelectionModel().getSelectedItem().setTextFill(Paint.valueOf("white"));
                loadButton.setDisable(false);
            }
        });
    }

    public void populateSaveList() {
        saveList.getItems().clear();
        File savesDirectory = new File("saves");
        File[] savesList = savesDirectory.listFiles();
        if (savesList != null) {
            for (File save : savesList) {
                saveList.getItems().add(newCell(save.getName()));
            }
        }
        loadButton.setDisable(true);
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

    @FXML
    private void handleMainMenuButton() {
        mainMenuScreen.start();
    }

    @FXML
    private void handleLoadButton() throws IOException {
        Game g = new Game();
        g.loadGame(saveList.getSelectionModel().getSelectedItem().getText());
        gloriaRomanusScreen.getController().setGame(g);
        gloriaRomanusScreen.getController().initialiseMap();
        gloriaRomanusScreen.start();
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }
    
}
