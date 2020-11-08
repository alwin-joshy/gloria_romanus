package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NewGameController {
    private MainMenuScreen mainMenuScreen;
    private SelectFactionsScreen selectFactionsScreen;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button selectFactionsButton;

    @FXML
    public void handleMainMenuButton() {
        mainMenuScreen.start();
    }

    @FXML
    public void handleSelectFactionsButton() {
        selectFactionsScreen.start();
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setSelectFactionsScreen(SelectFactionsScreen selectFactionsScreen) {
        this.selectFactionsScreen = selectFactionsScreen;
    }
}
