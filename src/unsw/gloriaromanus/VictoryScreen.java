package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class VictoryScreen {
    private Stage stage;
    private String title;
    private VictoryController controller;
    private Scene scene;

    public VictoryScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Victory";
        Font.loadFont(getClass().getResourceAsStream("Roman SD.ttf"), 10);
        controller = new VictoryController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VictoryScreen.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        scene = new Scene(root, 800, 700);
    }
    
    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public VictoryController getController() {
        return controller;
    }
}
