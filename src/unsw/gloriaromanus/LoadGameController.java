package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoadGameController {
    private MainMenuScreen mainMenuScreen;
    private GloriaRomanusScreen gloriaRomanusScreen;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button loadButton;

    @FXML
    private void handleMainMenuButton() {
        mainMenuScreen.start();
    }

    @FXML
    private void handleLoadButton() {
        
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }
    
}
