package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    private SelectFactionsScreen selectFactionsScreen;
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
        loadGameScreen.getController().populateSaveList();
        loadGameScreen.start();
    }

    @FXML
    public void handleNewGameButton() {
        selectFactionsScreen.start();
    }

    @FXML
    public void handleContinueButton() throws IOException { 
        // load the most recent save into an instance of game and pass it to start
        File savesDirectory = new File("saves");
        File[] files = savesDirectory.listFiles();
        if (files.length != 0) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
            File mostRecent = files[0];
            System.out.println(mostRecent.getName());
            Game g = new Game();
            g.loadGame(mostRecent.getName());
            gloriaRomanusScreen.getController().setGame(g);
            gloriaRomanusScreen.getController().initialiseMap();
            gloriaRomanusScreen.start();
        }
    }

    @FXML
    public void handleExitGameButton() {
        gloriaRomanusScreen.getController().terminate();
        Platform.exit();
        System.exit(1);
    }

    public void setSelectFactionsScreen(SelectFactionsScreen selectFactionsScreen) {
        this.selectFactionsScreen = selectFactionsScreen;
    }

    public void setLoadGameScreen(LoadGameScreen loadGameScreen) {
        this.loadGameScreen = loadGameScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }

}
