package unsw.gloriaromanus;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    private NewGameScreen newGameScreen;
    private LoadGameScreen loadGameScreen;
    private GloriaRomanusScreen gloriaRomanusScreen;

    @FXML
    private Button newGameButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button continueButton;

    @FXML
    private Button exitGameButton;

    @FXML
    public void handleLoadGameButton() {
        loadGameScreen.start();
    }

    @FXML
    public void handleNewGameButton() {
        newGameScreen.start();
    }

    @FXML
    public void handleContinueButton() {
        gloriaRomanusScreen.start();
    }

    @FXML
    public void handleExitGameButton() {
        gloriaRomanusScreen.getController().terminate();
        Platform.exit();
        System.exit(1);
    }

    public void setNewGameScreen(NewGameScreen newGameScreen) {
        this.newGameScreen = newGameScreen;
    }

    public void setLoadGameScreen(LoadGameScreen loadGameScreen) {
        this.loadGameScreen = loadGameScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }

}
