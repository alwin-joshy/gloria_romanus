package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GloriaRomanusScreen {
    private Stage stage;
    private String title;
    private GloriaRomanusController controller;
    private Scene scene;

    public GloriaRomanusScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Gloria Romanus";
        Font.loadFont(getClass().getResourceAsStream("Roman SD.ttf"), 10);
        controller = new GloriaRomanusController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        scene = new Scene(root, 800, 700);
    }
    
    public void start(Game game) {
        controller.setGame(game);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public GloriaRomanusController getController() {
        return controller;
    }

}
