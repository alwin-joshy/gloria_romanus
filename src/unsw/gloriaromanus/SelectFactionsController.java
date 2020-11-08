package unsw.gloriaromanus;

import com.gluonhq.charm.glisten.control.Avatar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

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

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }
}
