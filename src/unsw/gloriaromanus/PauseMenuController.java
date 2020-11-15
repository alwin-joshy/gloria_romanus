package unsw.gloriaromanus;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PauseMenuController {
    private GloriaRomanusController gloriaRomanusController;
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
        gloriaRomanusController.closePauseMenu();
    }

    @FXML
    private void handleSaveButton() {
        gloriaRomanusController.openSaveMenu();
    }

    @FXML
    private void handleMainMenuButton() {
        gloriaRomanusController.closePauseMenu();
        mainMenuScreen.start();
    }

    @FXML
    public void handleExitGameButton() {
        gloriaRomanusController.terminate();
        Platform.exit();
        System.exit(1);
    }

    public PauseMenuController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }
    
    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }
}
