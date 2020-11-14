package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VictoryController {

    private MainMenuScreen mainMenuScreen;
    private GloriaRomanusScreen gloriaRomanusScreen;

    @FXML
    private Button continueButton;

    @FXML
    private Button backButton;

    @FXML
    private Label winningFaction;

    @FXML
    private void handleMainMenuButton() {
        mainMenuScreen.start();
    }
    
    @FXML
    private void handleContinueButton() {
        gloriaRomanusScreen.start();
    }

    public void updateWinner(String winner) {
        winningFaction.setText(winner);
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }
    
}
