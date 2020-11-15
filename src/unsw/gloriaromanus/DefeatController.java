package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DefeatController {

    private MainMenuScreen mainMenuScreen;
    private GloriaRomanusScreen gloriaRomanusScreen;

    @FXML
    private Button continueButton;

    @FXML
    private Label losingFaction;

    
    @FXML
    private void handleContinueButton() {
        gloriaRomanusScreen.start();
    }

    public void updateLoser(String loser) {
        losingFaction.setText(loser);
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }
    
}
