package unsw.gloriaromanus;

import com.gluonhq.charm.glisten.control.Avatar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class SelectFactionsController {
    private MainMenuScreen mainMenuScreen;
    private GloriaRomanusScreen gloriaRomanusScreen;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button startGameButton;

    @FXML
    public void handleMainMenuButton() {
        mainMenuScreen.start();
    }

    @FXML
    public void handleStartGameButton() {
        gloriaRomanusScreen.start();
    }

    /*public void initialize() {
        GridPane gp = new GridPane();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Avatar a = new Avatar();
                a.setOnMouseClicked();
                gp.add(a, i, j);
            }
        }
    }*/

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }
}
