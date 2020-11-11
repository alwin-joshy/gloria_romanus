package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PauseMenuScreen {
    private Stage stage;
    private String title;
    private PauseMenuController controller;
    private Scene scene;

    public PauseMenuScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Pause Menu";
        controller = new PauseMenuController();
        Font.loadFont(getClass().getResourceAsStream("Roman SD.ttf"), 10);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pauseMenu.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        scene = new Scene(root, 800, 700);
    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public PauseMenuController getController() {
        return controller;
    }
}