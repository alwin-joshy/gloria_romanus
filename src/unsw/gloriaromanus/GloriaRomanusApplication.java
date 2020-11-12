package unsw.gloriaromanus;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class GloriaRomanusApplication extends Application {

  private MainMenuController mainMenuController;
  private GloriaRomanusController controller;

  @Override
  public void start(Stage stage) throws IOException {
    MainMenuScreen mainMenuScreen = new MainMenuScreen(stage);
    NewGameScreen newGameScreen = new NewGameScreen(stage);
    LoadGameScreen loadGameScreen = new LoadGameScreen(stage);
    SelectFactionsScreen selectFactionsScreen = new SelectFactionsScreen(stage);
    GloriaRomanusScreen gloriaRomanusScreen = new GloriaRomanusScreen(stage);

    mainMenuController = mainMenuScreen.getController();
    controller = gloriaRomanusScreen.getController();

    mainMenuController.setLoadGameScreen(loadGameScreen);
    mainMenuController.setNewGameScreen(newGameScreen);
    mainMenuController.setGloriaRomanusScreen(gloriaRomanusScreen);

    newGameScreen.getController().setMainMenuScreen(mainMenuScreen);
    newGameScreen.getController().setSelectFactionsScreen(selectFactionsScreen);

    loadGameScreen.getController().setMainMenuScreen(mainMenuScreen);
    loadGameScreen.getController().setGloriaRomanusScreen(gloriaRomanusScreen);

    selectFactionsScreen.getController().setMainMenuScreen(mainMenuScreen);
    selectFactionsScreen.getController().setGloriaRomanusScreen(gloriaRomanusScreen);

    gloriaRomanusScreen.getController().getPauseMenuController().setMainMenuScreen(mainMenuScreen);
    gloriaRomanusScreen.getController().getPauseMenuController().setGloriaRomanusScreen(gloriaRomanusScreen);

    mainMenuScreen.start();
  }

  /**
   * Stops and releases all resources used in application.
   */
  @Override
  public void stop() {
    controller.terminate();
  }

  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {

    Application.launch(args);
  }
}