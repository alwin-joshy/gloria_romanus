package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

public class NewGameController {
    private MainMenuScreen mainMenuScreen;
    private SelectFactionsScreen selectFactionsScreen;
    private AI ai;
    private BattleResolver br;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button selectFactionsButton;

    @FXML
    private SplitMenuButton AISplitMenu;

    @FXML
    private SplitMenuButton BRSplitMenu;

    @FXML
    private MenuItem simpleAIItem;

    @FXML
    private MenuItem standardAIItem;

    @FXML
    private MenuItem simpleBRItem;

    @FXML
    private MenuItem standardBRItem;

    @FXML
    public void handleMainMenuButton() {
        mainMenuScreen.start();
        AISplitMenu.setText("SELECT AI LEVEL");
        BRSplitMenu.setText("SELECT BATTLE RESOLVER");
    }

    @FXML
    public void handleSelectFactionsButton() {
        selectFactionsScreen.start();
        AISplitMenu.setText("SELECT AI LEVEL");
        BRSplitMenu.setText("SELECT BATTLE RESOLVER");
    }

    @FXML
    public void handleSimpleAIItem() {
        ai = new SimpleAI();
        AISplitMenu.setText("SIMPLE AI");
    }

    @FXML
    public void handleStandardAIItem() {
        ai = new StandardAI();
        AISplitMenu.setText("STANDARD AI");
    }

    @FXML
    public void handleSimpleBRItem() {
        br = new SimpleBattleResolver();
        BRSplitMenu.setText("SIMPLE BATTLE RESOLVER");
    }

    @FXML
    public void handleStandardBRItem() {
        br = new StandardBattleResolver(0);
        BRSplitMenu.setText("STANDARD BATTLE RESOLVER");
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setSelectFactionsScreen(SelectFactionsScreen selectFactionsScreen) {
        this.selectFactionsScreen = selectFactionsScreen;
    }

}
