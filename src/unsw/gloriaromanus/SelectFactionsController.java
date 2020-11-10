package unsw.gloriaromanus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class SelectFactionsController {
    private MainMenuScreen mainMenuScreen;
    private GloriaRomanusScreen gloriaRomanusScreen;
    private BattleResolver br;
    private AI ai;
    private Game game;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button startGameButton;

    @FXML
    public void handleMainMenuButton() {
        mainMenuScreen.start();
    }

    @FXML
    public void handleStartGameButton() {
        game.startGame();
        gloriaRomanusScreen.start(game);
    }

    public void initialize() throws FileNotFoundException {
        String initialOwnership = "{\r\n  \"Rome\": [\r\n    \"Lugdunensis\",\r\n    \"Lusitania\",\r\n    \"Lycia et Pamphylia\"\r\n  ],\r\n  \"Carthage\": [\r\n    \"Macedonia\",\r\n    \"Mauretania Caesariensis\",\r\n    \"Mauretania Tingitana\"\r\n  ],\r\n  \"Gaul\": [\r\n    \"Moesia Inferior\",\r\n    \"Moesia Superior\",\r\n    \"Narbonensis\"\r\n  ],\r\n  \"Britons\": [\r\n    \"Noricum\",\r\n    \"Numidia\",\r\n    \"Pannonia Inferior\"\r\n  ],\r\n  \"Spain\": [\r\n    \"Pannonia Superior\",\r\n    \"Raetia\",\r\n    \"Sardinia et Corsica\"\r\n  ],\r\n  \"Numidia\": [\r\n    \"Sicilia\",\r\n    \"Syria\",\r\n    \"Tarraconensis\"\r\n  ],\r\n  \"Egypt\": [\r\n    \"Thracia\",\r\n    \"V\",\r\n    \"VI\"\r\n  ],\r\n  \"Seleucids\": [\r\n    \"VII\",\r\n    \"VIII\",\r\n    \"X\"\r\n  ],\r\n  \"Pontus\": [\r\n    \"XI\",\r\n    \"Achaia\",\r\n    \"Aegyptus\"\r\n  ],\r\n  \"Armenia\": [\r\n    \"Africa Proconsularis\",\r\n    \"Alpes Cottiae\",\r\n    \"Alpes Graiae et Poeninae\"\r\n  ],\r\n  \"Parthia\": [\r\n    \"Alpes Maritimae\",\r\n    \"Aquitania\",\r\n    \"Arabia\"\r\n  ],\r\n  \"Germanics\": [\r\n    \"Armenia Mesopotamia\",\r\n    \"Asia\",\r\n    \"Baetica\"\r\n  ],\r\n  \"Greece\": [\r\n    \"Belgica\",\r\n    \"Bithynia et Pontus\",\r\n    \"Britannia\"\r\n  ],\r\n  \"Macedonia\": [\r\n    \"Cilicia\",\r\n    \"Creta et Cyrene\",\r\n    \"Cyprus\"\r\n  ],\r\n  \"Thracia\": [\r\n    \"Dacia\",\r\n    \"Dalmatia\",\r\n    \"Galatia et Cappadocia\"\r\n  ],\r\n  \"Dacia\": [\r\n    \"Germania Inferior\",\r\n    \"Germania Superior\",\r\n    \"I\",\r\n    \"II\",\r\n    \"III\",\r\n    \"IV\",\r\n    \"IX\",\r\n    \"Iudaea\"\r\n  ]\r\n}";
        String landlockedString = "[\r\n \"Germania Superior\",\r\n \"Raetia\",\r\n \"Alpes Graiae et Poeninae\",\r\n \"Alpes Cottiae\",\r\n \"XI\",\r\n \"Noricum\",\r\n \"Pannonia Superior\",\r\n \"Pannonia Inferior\",\r\n \"Moesia Superior\",\r\n \"Dacia\"\r\n]\r\n";
        String adjacencyString = "{\r\n  \"Britannia\": {\r\n    \"Britannia\": false,\r\n    \"Lugdunensis\": true,\r\n    \"Belgica\": false,\r\n    \"Germania Inferior\": false,\r\n    \"Aquitania\": false,\r\n    \"Germania Superior\": false,\r\n    \"Alpes Graiae et Poeninae\": false,\r\n    \"XI\": false,\r\n    \"Alpes Cottiae\": false,\r\n    \"Alpes Maritimae\": false,\r\n    \"IX\": false,\r\n    \"Narbonensis\": false,\r\n    \"Tarraconensis\": false,\r\n    \"Baetica\": false,\r\n    \"Lusitania\": false,\r\n    \"Raetia\": false,\r\n    \"Noricum\": false,\r\n    \"X\": false,\r\n    \"VIII\": false,\r\n    \"VII\": false,\r\n    \"VI\": false,\r\n    \"IV\": false,\r\n    \"V\": false,\r\n    \"I\": false,\r\n    \"III\": false,\r\n    \"Sicilia\": false,\r\n    \"Pannonia Superior\": false,\r\n    \"Pannonia Inferior\": false,\r\n    \"Dalmatia\": false,\r\n    \"II\": false,\r\n    \"Sardinia et Corsica\": false,\r\n    \"Moesia Superior\": false,\r\n    \"Dacia\": false,\r\n    \"Moesia Inferior\": false,\r\n    \"Thracia\": false,\r\n    \"Macedonia\": false,\r\n    \"Achaia\": false,\r\n    \"Bithynia et Pontus\": false,\r\n    \"Cilicia\": false,\r\n    \"Creta et Cyrene\": false,\r\n    \"Cyprus\": false,\r\n    \"Aegyptus\": false,\r\n    \"Arabia\": false,\r\n    \"Iudaea\": false,\r\n    \"Syria\": false,\r\n    \"Africa Proconsularis\": false,\r\n    \"Numidia\": false,\r\n    \"Mauretania Caesariensis\": false,\r\n    \"Mauretania Tingitana\": false,\r\n    \"Galatia et Cappadocia\": false,\r\n    \"Lycia et Pamphylia\": false,\r\n    \"Asia\": false,\r\n    \"Armenia Mesopotamia\": false\r\n  }\r\n}";
        JSONObject ownership = new JSONObject(initialOwnership);
        JSONArray landlocked = new JSONArray(landlockedString);
        JSONObject adjacencyMap = new JSONObject(adjacencyString);
        game = new Game();
        game.initialiseGame(ownership, landlocked, adjacencyMap);
        ArrayList<Faction> factions = game.getFactions();
        Font.loadFont(getClass().getResourceAsStream("Roman SD.ttf"), 10);
        
        // populate the 4x4 grid with toggle buttons
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                FileInputStream input = new FileInputStream(
                "images/CS2511Sprites_No_Background/Flags/Celtic/CelticFlag.png");
                Image image = new Image(input, 30, 30, true, true);
                ImageView iv = new ImageView(image);
                String currentFactionName = factions.get(4 * i + j).getName();
                ToggleButton b = new ToggleButton(currentFactionName, iv);
                b.setStyle("-fx-font-family: \'Roman SD\'; -fx-font-size: 15;");
                b.setPrefSize(140, 50);
                // designate a faction to each button so that it can be used to toggle
                // a faction to be a player
                b.setOnMouseClicked((e) -> {
                    Faction f = game.getFaction(b.getText());
                    if (e.getClickCount() % 2 == 1)
                        f.setPlayer();
                    else
                        f.setAI();
                });
                gridPane.add(b, j, i);
            }
        }
    }

    public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
        this.mainMenuScreen = mainMenuScreen;
    }

    public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
        this.gloriaRomanusScreen = gloriaRomanusScreen;
    }

    public void setBattleResolver(BattleResolver br) {
        this.br = br;
    }

    public void setAI(AI ai) {
        this.ai = ai;
    }
}
