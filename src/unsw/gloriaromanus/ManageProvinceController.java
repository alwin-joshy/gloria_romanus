package unsw.gloriaromanus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

public class ManageProvinceController {
    @FXML
    private Label provinceName;
    
    @FXML
    private Label provinceWealth;
    
    @FXML
    private Label provinceWealthGrowth;
    
    @FXML
    private Label provincePublicOrder;

    @FXML   
    private MenuItem lowTax;

    @FXML   
    private MenuItem normalTax;

    @FXML   
    private MenuItem highTax;

    @FXML   
    private MenuItem veryHighTax;

    @FXML
    private SplitMenuButton currentTax;

    @FXML
    private Button backButton;

    private Province province;
    private GloriaRomanusController gloriaRomanusController;

    private void updateTax(String level) {
        province.setTax(level);
        currentTax.setText(province.getTax().toString());
        provinceWealthGrowth.setText(Integer.toString(province.getWealthGrowth() + province.getTax().getWealthGrowthDelta()));
        provincePublicOrder.setText(Integer.toString((int) Math.round(province.getPublicOrder() * 100)) + "%");
    }

    @FXML
    private void setLowTax() {
        updateTax("low");
    }

    @FXML
    private void setNormalTax() {
        updateTax("normal");
    }

    @FXML
    private void setHighTax() {
        updateTax("high");
    }

    @FXML
    private void setVeryHighTax() {
        updateTax("very high");
    }

    @FXML
    private void handleBackButton() {
        gloriaRomanusController.closeManageProvinceMenu();
    }

    public ManageProvinceController(GloriaRomanusController gloriaRomanusController) {
        this.gloriaRomanusController = gloriaRomanusController;
    }

    public void setupScreen(Province province) {
        this.province = province;
        provinceName.setText(province.getName());
        provinceName.setWrapText(true);
        provinceWealth.setText(Integer.toString(province.getWealth()));
        provinceWealthGrowth.setText(Integer.toString(province.getWealthGrowth() + province.getTax().getWealthGrowthDelta()));
        provincePublicOrder.setText(Integer.toString((int) Math.round(province.getPublicOrder() * 100)) + "%");
        currentTax.setText(province.getTax().toString());
    }
    
}
