package unsw.gloriaromanus.UI;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GRMapView {
    
    private Stage stage;
    private String title;
    private GRMapViewController controller;
    private Scene scene;

    public GRMapView(Stage stage) throws IOException {

        this.stage = stage;
        title = "Gloria Romanus";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GRMapViewBody.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        scene = new Scene(root, 1024, 576);

    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public GRMapViewController getController() {
        return controller;
    }

}
