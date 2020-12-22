package unsw.gloriaromanus.UI;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenu {
    
    private Stage stage;
    private String title;
    private MainMenuController controller;
    private Scene scene;

    public MainMenu(Stage stage) throws IOException {

        this.stage = stage;
        title = "Gloria Romanus";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        controller = new MainMenuController();
        loader.setController(controller);
        Parent root = loader.load();
        scene = new Scene(root, 1024, 576);

    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public MainMenuController getController() {
        return controller;
    }

}
