package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gluonhq.charm.glisten.control.Avatar;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class GloriaRomanusController {

  @FXML
  private MapView mapView;
  @FXML
  private TextField invading_province;
  @FXML
  private TextField targetProvince;
  @FXML
  private TextArea output_terminal;
  @FXML
  private Button pauseButton;
  @FXML
  private StackPane stack;
  @FXML
  private Button endTurnButton;
  @FXML
  private Circle factionAvatar;
  @FXML
  private Label currentFactionName;
  @FXML
  private Label currentYear;
  @FXML
  private Button manageProvinceButton;
  @FXML
  private Button infrastructureButton;
  @FXML
  private Button victoryProgressButton;
  @FXML
  private Button unitsButton;
  //@FXML
  //private ToggleButton moveToggle;
  @FXML
  private HBox moveBox;

  private ToggleSwitch moveToggle;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;

  private Feature currentlySelectedAlliedProvince;
  private Feature currentlySelectedTargetProvince;

  private FeatureLayer featureLayer_provinces;

  private StackPane transparentPane;

  private Pane pauseMenu;
  private Pane saveMenu;
  private Pane manageProvinceMenu;
  private Pane infrastructureMenu;
  private Pane victoryProgressMenu;
  private Pane unitsMenu;
  private Pane selectUnitsMenu;

  private VictoryScreen victoryScreen;
  private DefeatScreen defeatScreen;

  private PauseMenuController pauseMenuController;
  private SaveController saveController;
  private ManageProvinceController manageProvinceController;
  private InfrastructureController infrastructureController;
  private VictoryProgressController victoryProgressController;
  private UnitsController unitsController;
  private SelectUnitsController selectUnitsController;

  private Game game;
  private StringProperty winner;
  private BooleanProperty deselect;
  private StringProperty losingFaction;
  private boolean battleLost;

  @FXML
  private void handleVictoryProgressButton() {
    stack.getChildren().add(transparentPane);
    transparentPane.getChildren().add(victoryProgressMenu);
    victoryProgressController.updateProgress(game);
  }

  @FXML
  private void handleManageProvinceButton() {
    stack.getChildren().add(transparentPane);
    transparentPane.getChildren().add(manageProvinceMenu);
    manageProvinceController.setupScreen(getProvince(currentlySelectedAlliedProvince));
  }

  @FXML
  private void handleEndTurnButton() throws FileNotFoundException, JsonParseException, IOException {
    String provincesLost = "";
    for (Province p : game.endTurn()) {
      provinceToOwningFactionMap.put(p.getName(), p.getFactionName());
      provinceToNumberTroopsMap.put(p.getName(), p.getTotalTroops());
      provincesLost += p.getName() + ", ";
    }
    if (! provincesLost.equals("")) {
      provincesLost = provincesLost.substring(0, provincesLost.length() - 3);
      createAlert(provincesLost + " revolted", "Revolution!", AlertType.WARNING);
    }

    currentFactionName.setText(game.getCurrentFaction().getName());
    currentYear.setText(game.getCurrentYear());
    FileInputStream input = new FileInputStream("images/CS2511Sprites_No_Background/Flags/" + game.getCurrentFactionName() + "/" + game.getCurrentFactionName() + "Flag.png");
    Image image = new Image(input);
    factionAvatar.setFill(new ImagePattern(image));
    humanFaction = game.getCurrentFactionName();
    winner.setValue(game.getWinner());
    for (Province p : game.getCurrentFaction().getAlliedProvinces()) {
      provinceToNumberTroopsMap.put(p.getName(), p.getTotalTroops());
    }
    addAllPointGraphics(); // reset graphics
  }

  @FXML 
  private void handleInfrastructureButton() {
    stack.getChildren().add(transparentPane);
    transparentPane.getChildren().add(infrastructureMenu);
    infrastructureController.setupScreen(getProvince(currentlySelectedAlliedProvince));
  }

  public void moveUnits(ArrayList<Unit> toMove, Province start, Province end) throws IOException {
    if (currentlySelectedAlliedProvince != null && currentlySelectedTargetProvince != null) {
      String humanProvince = (String)currentlySelectedAlliedProvince.getAttributes().get("name");
      String targetProvince = (String)currentlySelectedTargetProvince.getAttributes().get("name");
      boolean invaded;
      if (game.getCurrentFaction().isAlliedProvince(targetProvince)) {
        invaded = false;
      } else {
        invaded = true;
      }
      int numTroopsToTransferBeforeMove = getNumTroopsToTransfer(toMove);
      if (game.moveUnits(toMove, game.getProvince(humanProvince), game.getProvince(targetProvince))) {
        if (invaded) {
          int numTroopsToTransferAfterMove = getNumTroopsToTransfer(toMove);
          provinceToNumberTroopsMap.put(targetProvince, numTroopsToTransferAfterMove);
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince) - numTroopsToTransferBeforeMove);
          provinceToOwningFactionMap.put(targetProvince, humanFaction);
        } else {
          provinceToNumberTroopsMap.put(targetProvince, provinceToNumberTroopsMap.get(targetProvince) + numTroopsToTransferBeforeMove);
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince) - numTroopsToTransferBeforeMove);
          printMessageToTerminal(numTroopsToTransferBeforeMove + " units moved from " + humanProvince + " to " + targetProvince);
        }
      } else {
        if (invaded && battleLost) {
          provinceToNumberTroopsMap.put(targetProvince, getNumTroopsToTransfer(end.getUnits()));
          provinceToNumberTroopsMap.put(humanProvince, getNumTroopsToTransfer(start.getUnits()));
          battleLost = false;
        } else {
          printMessageToTerminal("Selected units could not move from " + humanProvince + " to " + targetProvince);
        }
      }
      addAllPointGraphics(); // reset graphics
      deselect.setValue(true);
    }
  }

  public int getNumTroopsToTransfer(ArrayList<Unit> toMove) {
    int total = 0;
    for (Unit u : toMove) {
      total += u.getNumTroops();
    }
    return total;
  }

  public PauseMenuController getPauseMenuController() {
    return pauseMenuController;
  }

  public void setBattleLost(boolean battleLost) {
    this.battleLost = battleLost;
  }

  public void setLosingFaction(String factionName) {
    losingFaction.setValue(factionName);
  }

  public void closeManageProvinceMenu() {
    stack.getChildren().remove(transparentPane);
    transparentPane.getChildren().remove(manageProvinceMenu);
  }

  public void closePauseMenu() {
    stack.getChildren().remove(transparentPane);
    transparentPane.getChildren().remove(pauseMenu);
  }

  public void openSaveMenu() {
    saveController.populateSaveList();
    transparentPane.getChildren().remove(pauseMenu);
    transparentPane.getChildren().add(saveMenu);
  }

  public void closeSaveMenu() {
    transparentPane.getChildren().remove(saveMenu);
    transparentPane.getChildren().add(pauseMenu);
  }

  public void closeInfrastructureMenu() {
    stack.getChildren().remove(transparentPane);
    transparentPane.getChildren().remove(infrastructureMenu);
  }

  public void closeVictoryProgressMenu() {
    stack.getChildren().remove(transparentPane);
    transparentPane.getChildren().remove(victoryProgressMenu);
  }

  public void closeUnitsMenu() {
    stack.getChildren().remove(transparentPane);
    transparentPane.getChildren().remove(unitsMenu);
  }

  public void closeSelectUnitsMenu() {
    stack.getChildren().remove(transparentPane);
    transparentPane.getChildren().remove(selectUnitsMenu);
  }

  public void setVictoryScreen(VictoryScreen victoryScreen) {
    this.victoryScreen = victoryScreen;
  }

  public void setDefeatScreen(DefeatScreen defeatScreen) {
    this.defeatScreen = defeatScreen;
  }

  public void createAlert(String content, String header, AlertType type) {
    Stage stage = (Stage) stack.getScene().getWindow();

    Alert alert = new Alert(type, "");

    alert.initModality(Modality.APPLICATION_MODAL);
    alert.initOwner(stage);

    alert.getDialogPane().setContentText(content);
    alert.getDialogPane().setHeaderText(header);

    alert.showAndWait();

  }

  @FXML
  public void handleUnitsButton() throws IOException {
    transparentPane.getChildren().add(unitsMenu);
    stack.getChildren().add(transparentPane);
    unitsController.setupUnitDetails(getProvince(currentlySelectedAlliedProvince));
  }

  private Province getProvince(Feature fl) {
    return game.getProvince((String) fl.getAttributes().get("name"));
  }

  @FXML
  private void initialize() throws IOException {
    this.pauseMenuController = new PauseMenuController();
    this.saveController = new SaveController(this);
    this.manageProvinceController = new ManageProvinceController(this);
    this.infrastructureController = new InfrastructureController(this);
    this.victoryProgressController = new VictoryProgressController(this);
    this.unitsController = new UnitsController(this);
    this.selectUnitsController = new SelectUnitsController(this);

    deselect = new SimpleBooleanProperty(false);
    losingFaction = new SimpleStringProperty("");

    moveToggle = new ToggleSwitch();
    output_terminal.setWrapText(true);
    endTurnButton.disableProperty().bind(moveToggle.selectedProperty());

    manageProvinceButton.setDisable(true);
    unitsButton.setDisable(true);
    infrastructureButton.setDisable(true);

    moveBox.getChildren().add(moveToggle);

    transparentPane = new StackPane();

    losingFaction.addListener((ov, oldValue, newValue) -> {
      if (! losingFaction.getValue().equals("")) {
        defeatScreen.getController().updateLoser(losingFaction.getValue());
        defeatScreen.start();
        losingFaction.setValue("");
      }
    });

    FXMLLoader loader = new FXMLLoader(getClass().getResource("pauseMenu.fxml"));
    loader.setController(pauseMenuController);
    pauseMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("save.fxml"));
    loader.setController(saveController);
    saveMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("manageProvince.fxml"));
    loader.setController(manageProvinceController);
    manageProvinceMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("infrastructure.fxml"));
    loader.setController(infrastructureController);
    infrastructureMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("victoryProgress.fxml"));
    loader.setController(victoryProgressController);
    victoryProgressMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("units.fxml"));
    loader.setController(unitsController);
    unitsMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("SelectUnits.fxml"));
    loader.setController(selectUnitsController);
    selectUnitsMenu = loader.load();

  }

  public void initialiseMap() throws JsonParseException, JsonMappingException, IOException {
    battleLost = false;
    game.setEngagementObserver(new EngagementObserver(this));
    game.addDefeatObserver(new DefeatObserver(this, game));
    deselect.setValue(false);
    winner = new SimpleStringProperty("");
    winner.addListener((ov, oldValue, newValue) -> {
      if (! winner.getValue().equals("")) {
        victoryScreen.getController().updateWinner(game.getWinner());
        game.saveGame();
        victoryScreen.start();
      }
    });
    losingFaction.setValue("");
    int i = 0;
    ArrayList<Province> toTransfer = new ArrayList<Province>();

    // TODO get rid of this -- used to to test defeat screen
    for (Province p : game.getFaction("Rome").getAlliedProvinces()) {
      toTransfer.add(p);
    }

    System.out.println(game.getCurrentFactionName());
    for (Province p : toTransfer) {
        Game.transferProvinceOwnership(game.getFaction("Rome"), game.getFaction("Gaul"), p);
    }

    Game.transferProvinceOwnership(game.getFaction("Gaul"), game.getFaction("Rome"), game.getProvince("Germania Inferior"));

    currentFactionName.setText(game.getCurrentFaction().getName());
    currentYear.setText(game.getCurrentYear());
    FileInputStream input = new FileInputStream("images/CS2511Sprites_No_Background/Flags/" + game.getCurrentFactionName() + "/" + game.getCurrentFactionName() + "Flag.png");
    Image image = new Image(input);
    factionAvatar.setFill(new ImagePattern(image));
    // TODO = you should rely on an object oriented design to determine ownership
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, 0);
    }

    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)
    humanFaction = game.getCurrentFactionName();

    currentlySelectedAlliedProvince = null;
    currentlySelectedTargetProvince = null;

    initializeProvinceLayers();
  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) {
    /*if (currentlySelectedAlliedProvince != null && currentlySelectedTargetProvince != null) {
      String humanProvince = (String)currentlySelectedAlliedProvince.getAttributes().get("name");
      String targetProvince = (String)currentlySelectedTargetProvince.getAttributes().get("name");
      if (confirmIfProvincesConnected(humanProvince, enemyProvince)) {
        if (game.moveUnits(toMove, game.getProvince(humanProvince), game.getProvince(enemyProvince))) {
          factionCount.setValue(game.getFactions().size());
          int numTroopsToTransfer = getNumTroopsToTransfer();
          provinceToNumberTroopsMap.put(enemyProvince, numTroopsToTransfer);
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsToTransfer);
          provinceToOwningFactionMap.put(enemyProvince, humanFaction);
          resetSelections();  // reset selections in UI
          addAllPointGraphics(); // reset graphics
        } else {
          printMessageToTerminal("Can't make this move idk why!");
        }
      }
    }*/
  }

  @FXML
  private void handlePauseButton() {
    stack.getChildren().add(transparentPane);
    transparentPane.getChildren().add(pauseMenu);
  }

  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {
    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        // PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        // switch (faction) {
        //   case "Gaul":
        //     // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
        //     // construct it with custom-produced BufferedImages stored in Ram
        //     // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
        //     // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

        //     // you can pass in a filename to create a PictureMarkerSymbol...
        //     s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
        //     break;
        //   case "Rome":
        //     // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
        //     s = new PictureMarkerSymbol("images/legionary.png");
        //     break;
        //   // TODO = handle all faction names, and find a better structure...
        // }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        // System.out.println(curPoint);
        // System.out.println(s);
        // Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        // graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);
    endTurnButton.setOnMouseClicked(e -> {
      if (currentlySelectedAlliedProvince != null) {
        flp.unselectFeature(currentlySelectedAlliedProvince);
        currentlySelectedAlliedProvince = null;
      }
      if (currentlySelectedTargetProvince != null) {
        flp.unselectFeature(currentlySelectedTargetProvince);
        currentlySelectedTargetProvince = null;
      }
      invading_province.clear();
      targetProvince.clear();
      manageProvinceButton.setDisable(true);
      unitsButton.setDisable(true);
      infrastructureButton.setDisable(true);
    });

    moveToggle.setOnMouseClicked(e -> {
      if (! moveToggle.isSelected() && currentlySelectedTargetProvince != null) {
        flp.unselectFeature(currentlySelectedTargetProvince);
        currentlySelectedTargetProvince = null;
        targetProvince.clear();
      }
    });

    deselect.addListener((ov, oldVal, newVal) -> {
      if (deselect.getValue()) {
        flp.unselectFeature(currentlySelectedTargetProvince);
        currentlySelectedTargetProvince = null;
        targetProvince.clear();
        deselect.setValue(false);
      }
    });

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");

                if (provinceToOwningFactionMap.get(province).equals(humanFaction) && ! moveToggle.isSelected()) {
                  // province owned by human
                  if (currentlySelectedAlliedProvince != null){
                    featureLayer.unselectFeature(currentlySelectedAlliedProvince);
                  }
                  currentlySelectedAlliedProvince = f;
                  invading_province.setText(province);
                  manageProvinceButton.setDisable(false);
                  unitsButton.setDisable(false);
                  infrastructureButton.setDisable(false);
                  featureLayer.selectFeature(f); 
                } else if (moveToggle.isSelected() && currentlySelectedAlliedProvince != null && ! f.getAttributes().get("name").equals(getProvince(currentlySelectedAlliedProvince).getName())) {
                  if (currentlySelectedTargetProvince != null) {
                    featureLayer.unselectFeature(currentlySelectedTargetProvince);
                  }
                  currentlySelectedTargetProvince = f;
                  targetProvince.setText(province);
                  featureLayer.selectFeature(f);

                  transparentPane.getChildren().add(selectUnitsMenu);
                  stack.getChildren().add(transparentPane);
                  selectUnitsController.listSetup(getProvince(currentlySelectedAlliedProvince), getProvince(currentlySelectedTargetProvince));
                }              
              }

              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(humanFaction));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections(){
    featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedTargetProvince, currentlySelectedAlliedProvince));
    currentlySelectedTargetProvince = null;
    currentlySelectedAlliedProvince = null;
    invading_province.setText("");
    targetProvince.setText("");
  }

  public void resetTargetSelection() {
    featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedTargetProvince));
    currentlySelectedTargetProvince = null;
    targetProvince.setText("");
  }

  public void printMessageToTerminal(String message){
    output_terminal.appendText(message+"\n");
  }

  public void setGame(Game game) {
    this.game = game;
    saveController.setGame(game);
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }
}