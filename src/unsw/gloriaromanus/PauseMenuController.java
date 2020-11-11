package unsw.gloriaromanus;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PauseMenuController {
    private GloriaRomanusScreen gloriaRomanusScreen;
    private MainMenuScreen mainMenuScreen;

    @FXML
    private Button resumeButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button exitGameButton;

    @FXML
    private void handleResumeButton() {
        gloriaRomanusScreen.start();
    }

    @FXML
    private void handleSaveButton() {

    }

    @FXML
    private void handleMainMenuButton() {
        mainMenuScreen.start();
    }

    @FXML
    public void handleExitGameButton() {
        gloriaRomanusScreen.getController().terminate();
        Platform.exit();
        System.exit(1);
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }
}
