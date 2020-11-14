package unsw.gloriaromanus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class SelectFactionsController {
    private MainMenuScreen mainMenuScreen;
    private GloriaRomanusScreen gloriaRomanusScreen;
    private BattleResolver br;
    private AI ai;
    private Game game;
    private ArrayList<String> factions;
    private int selectedFactionCounter;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button startGameButton;

    @FXML
    public void handleMainMenuButton() {
        cleanUp();
        mainMenuScreen.start();
    }

    @FXML
    public void handleStartGameButton() throws IOException {
        createNewGame();
        game.startGame(factions);
        cleanUp();
        gloriaRomanusScreen.getController().setGame(game);
        gloriaRomanusScreen.getController().initialiseMap();
        gloriaRomanusScreen.start();
    }

    public void cleanUp() {
        selectedFactionCounter = 0;
        startGameButton.setDisable(true);
        for (Node n : gridPane.getChildren()) {
            ToggleButton b = (ToggleButton) n;
            b.setSelected(false);
        }
        factions.clear();
    }

    public void initialize() throws FileNotFoundException, IOException {
        selectedFactionCounter = 0;
        startGameButton.setDisable(true);
        // create a list of all playable factions
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/factions.json"));
        JSONArray availableFactions = new JSONArray(content);

        factions = new ArrayList<String>();
        
        Font.loadFont(getClass().getResourceAsStream("Roman SD.ttf"), 10);
        
        // populate the 4x4 grid with toggle buttons
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                // create toggle button and add it to the grid pane
                String currentFactionName = availableFactions.getString(4 * i + j);
                ToggleButton b = new ToggleButton(currentFactionName);
                b.setStyle("-fx-font-family: \'Roman SD\'; -fx-font-size: 15;");
                b.getStyleClass().add(getClass().getResource("stylesheet.css").toExternalForm());
                b.setPrefSize(140, 50);
                // designate a faction to each button so that it can be used to toggle
                // a faction to be a player
                b.setOnAction((e) -> {
                    if (b.isSelected()) {
                        factions.add(b.getText());
                        selectedFactionCounter++;
                    } else {
                        factions.remove(b.getText());
                        selectedFactionCounter--;
                    }
                    if (selectedFactionCounter < 2)
                        startGameButton.setDisable(true);
                    else
                        startGameButton.setDisable(false);
                });
                gridPane.add(b, j, i);
            }
        }
    }

    public void createNewGame() throws IOException {
        game = new Game();
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/provinces.json"));
        JSONArray provinces = new JSONArray(content);
        content = Files.readString(Paths.get("src/unsw/gloriaromanus/landlocked_provinces.json"));
        JSONArray landlocked = new JSONArray(content);
        content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        JSONObject adjacencyMap = new JSONObject(content);
        game.initialiseGame(provinces, landlocked, adjacencyMap);
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }

}
