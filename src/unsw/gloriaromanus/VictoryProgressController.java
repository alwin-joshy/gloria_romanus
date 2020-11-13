package unsw.gloriaromanus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


public class VictoryProgressController {
    @FXML
    private Label currentVictoryCondition;

    @FXML
    private GridPane victoryGrid;

    @FXML
    private Label provincesConquered;

    @FXML
    private Label totalWealth;

    @FXML
    private Label treasuryWealth;

    @FXML
    private Label fullyUpgradedProvinces;

    @FXML
    private Label conqueredPercent;

    @FXML
    private Label provinceWealthPercent;

    @FXML
    private Label treasuryPercent;

    @FXML
    private Label upgradedProvincesPercent;

    @FXML
    private Button backButton;

    private GloriaRomanusController gloriaRomanusController;

    @FXML
    private void handleBackButton() {
        gloriaRomanusController.closeVictoryProgressMenu();
    }

    public VictoryProgressController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    private void setVictoryCondition(Game game) {
        currentVictoryCondition.setText(game.getVictoryCondition().showGoal());
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void updateProgress(Game game) {
        currentVictoryCondition.setText(game.getVictoryCondition().showGoal());
        Faction f = game.getCurrentFaction();
        provincesConquered.setText(Integer.toString(f.getNumProvinces()) + " / 53");
        conqueredPercent.setText(Double.toString(round((double) f.getNumProvinces() / 53 * 100, 2)));
        treasuryWealth.setText(Integer.toString(f.getTreasury()) + " / 100000");
        treasuryPercent.setText(Double.toString(round((double) f.getTreasury() / 1000, 2)));
        totalWealth.setText(Integer.toString(f.getWealth()) + " / 400000");
        provinceWealthPercent.setText(Double.toString(round((double) f.getWealth() / 4000, 2)));
        fullyUpgradedProvinces.setText(Integer.toString(f.getFullyUpgradedProvinces()) + " / " + Integer.toString(f.getNumProvinces()));
        upgradedProvincesPercent.setText(Double.toString(round((double) f.getFullyUpgradedProvinces() / f.getNumProvinces() * 100, 2)));

    }

}