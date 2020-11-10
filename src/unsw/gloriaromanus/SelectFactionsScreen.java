package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SelectFactionsScreen {
    private Stage stage;
    private String title;
    private SelectFactionsController controller;
    private Scene scene;

    public SelectFactionsScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "New Game";
        Font.loadFont(SelectFactionsScreen.class.getResource("Roman SD.ttf").toExternalForm(), 10);
        controller = new SelectFactionsController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("selectFactions.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        scene = new Scene(root, 800, 700);
    }

    public void start(BattleResolver br, AI ai) {
        controller.setBattleResolver(br);
        controller.setAI(ai);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public SelectFactionsController getController() {
        return controller;
    }
}
