package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoadGameScreen {
    private Stage stage;
    private String title;
    private LoadGameController controller;
    private Scene scene;

    public LoadGameScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Load Game";
        controller = new LoadGameController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loadGame.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        scene = new Scene(root, 800, 700);
    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public LoadGameController getController() {
        return controller;
    }
}
