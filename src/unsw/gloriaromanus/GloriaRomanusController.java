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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

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

public class GloriaRomanusController {

  @FXML
  private MapView mapView;
  @FXML
  private TextField invading_province;
  @FXML
  private TextField opponent_province;
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
  private Button victoryProgressButton;
  @FXML
  private Button unitsButton;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;

  private Feature currentlySelectedAlliedProvince;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;

  private StackPane transparentPane;

  private Pane pauseMenu;
  private Pane saveMenu;
  private Pane manageProvinceMenu;
  private Pane victoryProgressMenu;
  private Pane unitsMenu;

  private MainMenuScreen mainMenuScreen;
  private GloriaRomanusScreen gloriaRomanusScreen;

  private PauseMenuController pauseMenuController;
  private SaveController saveController;
  private ManageProvinceController manageProvinceController;
  private VictoryProgressController victoryProgressController;
  private UnitsController unitsController;

  private Game game;

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
    manageProvinceController.setupScreen(game.getProvince((String) currentlySelectedAlliedProvince.getAttributes().get("name")));
  }

  @FXML
  private void handleEndTurnButton() throws FileNotFoundException {
    game.endTurn();
    currentFactionName.setText(game.getCurrentFaction().getName());
    currentYear.setText(game.getCurrentYear());
    FileInputStream input = new FileInputStream("images/CS2511Sprites_No_Background/Flags/" + game.getCurrentFactionName() + "/" + game.getCurrentFactionName() + "Flag.png");
    Image image = new Image(input);
    factionAvatar.setFill(new ImagePattern(image));
    humanFaction = game.getCurrentFactionName();
  }

  public void setGloriaRomanusScreen(GloriaRomanusScreen gloriaRomanusScreen) {
    this.gloriaRomanusScreen = gloriaRomanusScreen;
  }

  public PauseMenuController getPauseMenuController() {
    return pauseMenuController;
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

  public void closeVictoryProgressMenu() {
    stack.getChildren().remove(transparentPane);
    transparentPane.getChildren().remove(victoryProgressMenu);
  }

  public void closeUnitsMenu() {
    stack.getChildren().remove(transparentPane);
    transparentPane.getChildren().remove(unitsMenu);
  }

  @FXML
  public void handleUnitsButton() {
    transparentPane.getChildren().add(unitsMenu);
    stack.getChildren().add(transparentPane);
    unitsController.setupUnitDetails(game.getProvince((String) currentlySelectedAlliedProvince.getAttributes().get("name")));
  }

  @FXML
  private void initialize() throws IOException {
    this.pauseMenuController = new PauseMenuController();
    this.saveController = new SaveController(this);
    this.manageProvinceController = new ManageProvinceController(this);
    this.victoryProgressController = new VictoryProgressController(this);
    this.unitsController = new UnitsController(this);

    manageProvinceButton.setDisable(true);
    unitsButton.setDisable(true);
    transparentPane = new StackPane();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("pauseMenu.fxml"));
    loader.setController(pauseMenuController);
    pauseMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("save.fxml"));
    loader.setController(saveController);
    saveMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("manageProvince.fxml"));
    loader.setController(manageProvinceController);
    manageProvinceMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("victoryProgress.fxml"));
    loader.setController(victoryProgressController);
    victoryProgressMenu = loader.load();

    loader = new FXMLLoader(getClass().getResource("units.fxml"));
    loader.setController(unitsController);
    unitsMenu = loader.load();
  }

  public void initialiseMap() throws JsonParseException, JsonMappingException, IOException {
    currentFactionName.setText(game.getCurrentFaction().getName());
    currentYear.setText(game.getCurrentYear());
    FileInputStream input = new FileInputStream("images/CS2511Sprites_No_Background/Flags/" + game.getCurrentFactionName() + "/" + game.getCurrentFactionName() + "Flag.png");
    Image image = new Image(input);
    factionAvatar.setFill(new ImagePattern(image));
    // TODO = you should rely on an object oriented design to determine ownership
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    Random r = new Random();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, r.nextInt(500));
    }

    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)
    humanFaction = game.getCurrentFactionName();

    currentlySelectedAlliedProvince = null;
    currentlySelectedEnemyProvince = null;

    initializeProvinceLayers();
  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedAlliedProvince != null && currentlySelectedEnemyProvince != null){
      String humanProvince = (String)currentlySelectedAlliedProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      if (confirmIfProvincesConnected(humanProvince, enemyProvince)){
        // TODO = have better battle resolution than 50% chance of winning
        Random r = new Random();
        int choice = r.nextInt(2);
        if (choice == 0) {
          // human won. Transfer 40% of troops of human over. No casualties by human, but enemy loses all troops
          int numTroopsToTransfer = provinceToNumberTroopsMap.get(humanProvince)*2/5;
          provinceToNumberTroopsMap.put(enemyProvince, numTroopsToTransfer);
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsToTransfer);
          provinceToOwningFactionMap.put(enemyProvince, humanFaction);
          printMessageToTerminal("Won battle!");
        }
        else{
          // enemy won. Human loses 60% of soldiers in the province
          int numTroopsLost = provinceToNumberTroopsMap.get(humanProvince)*3/5;
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsLost);
          printMessageToTerminal("Lost battle!");
        }
        resetSelections();  // reset selections in UI
        addAllPointGraphics(); // reset graphics
      }
      else{
        printMessageToTerminal("Provinces not adjacent, cannot invade!");
      }

    }
  }

  @FXML
  private void handlePauseButton() {
    stack.getChildren().add(transparentPane);
    transparentPane.getChildren().add(pauseMenu);

    //pauseMenuScreen.start();
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
      if (currentlySelectedEnemyProvince != null) {
        flp.unselectFeature(currentlySelectedEnemyProvince);
        currentlySelectedEnemyProvince = null;
      }
      invading_province.clear();
      opponent_province.clear();
      manageProvinceButton.setDisable(true);
      unitsButton.setDisable(true);
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

                if (provinceToOwningFactionMap.get(province).equals(humanFaction)){
                  // province owned by human
                  if (currentlySelectedAlliedProvince != null){
                    featureLayer.unselectFeature(currentlySelectedAlliedProvince);
                  }
                  currentlySelectedAlliedProvince = f;
                  invading_province.setText(province);
                  manageProvinceButton.setDisable(false);
                  unitsButton.setDisable(false);
                }
                else{
                  if (currentlySelectedEnemyProvince != null){
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  opponent_province.setText(province);
                }

                featureLayer.selectFeature(f);                
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
    featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedAlliedProvince));
    currentlySelectedEnemyProvince = null;
    currentlySelectedAlliedProvince = null;
    invading_province.setText("");
    opponent_province.setText("");
  }

  private void printMessageToTerminal(String message){
    output_terminal.appendText(message+"\n");
  }

  public void setMainMenuScreen(MainMenuScreen mainMenuScreen) {
    this.mainMenuScreen = mainMenuScreen;
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