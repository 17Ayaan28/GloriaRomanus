package unsw.gloriaromanus.UI;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class GRMainApp extends Application {

  private static GRMapViewController grMapViewController;

  @Override
  public void start(Stage stage) throws IOException {

    stage.setResizable(false);

    MainMenu mainMenu = new MainMenu(stage);
    MainMenuController mainMenuController = mainMenu.getController();

    GRMapView grMapView = new GRMapView(stage);
    grMapViewController = grMapView.getController();

    mainMenuController.setGRMapView(grMapView);

    mainMenu.start();
    
    // // set up the scene
    // FXMLLoader loader = new FXMLLoader(getClass().getResource("GRMapViewBody.fxml"));
    // Parent root = loader.load();
    // grMapViewController = loader.getController();
    // Scene scene = new Scene(root);

    // // set up the stage
    // stage.setTitle("Gloria Romanus");
    // stage.setWidth(800);
    // stage.setHeight(700);
    // stage.setScene(scene);
    // stage.show();

  }

  /**
   * Stops and releases all resources used in application.
   */
  @Override
  public void stop() {
    grMapViewController.terminate();
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