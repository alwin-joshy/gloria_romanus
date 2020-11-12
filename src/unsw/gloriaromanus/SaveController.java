package unsw.gloriaromanus;

import java.awt.Color;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class SaveController {
    @FXML
    private Label title;

    @FXML
    private TextField saveName;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    private Label status;

    @FXML
    private Label instruction;

    private Game game;

    private GloriaRomanusController gloriaRomanusController;

    @FXML
    private void handleSaveButton() {
        try {
            game.saveGame(saveName.getText());
            status.setText("SAVE SUCCESS");
            status.setTextFill(Paint.valueOf("#12b22b"));
        } catch (IOException e) {
            status.setText("SAVE FAILED");
            status.setTextFill(Paint.valueOf("#ff2d00"));
        }
        title.setTextFill(Paint.valueOf("#cd9636"));
    }

    @FXML
    private void handleBackButton() {
        gloriaRomanusController.closeSaveMenu();
        status.setText("");
    }

    @FXML
    private void initialize() {
        instruction.setWrapText(true);
    }

    public SaveController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    public void setGame(Game game) {
        this.game = game;
        title.setTextFill(Paint.valueOf("#cd9636"));
        System.out.println("set game");
    }
}
