package unsw.gloriaromanus.UI;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import unsw.gloriaromanus.GloriaRomanusGameLogic;
import unsw.gloriaromanus.ResourceManager;

public class MainMenuController {

    @FXML
    private Pane titlePane;
    @FXML
    private Pane newGamePane;

    @FXML
    private ImageView topBar;
    @FXML
    private ImageView topBarBackup;

    @FXML
    private ImageView botBar;
    @FXML
    private ImageView botBarBackup;

    @FXML
    private Button loadGameButton;
    @FXML
    private Button newGameButton;

    @FXML
    private Group p1Indicator;
    @FXML
    private Group p2Indicator;

    private String p1Faction = "Rome";
    private String p2Faction = "Gaul";

    @FXML
    private Text swapButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button backButton;

    private GRMapView grMapView;

    @FXML
    void loadGameButtonPress(ActionEvent event) {
        try {
            GloriaRomanusGameLogic g = (GloriaRomanusGameLogic)ResourceManager.load("1.save");
            System.out.println(g.getN_turn());
            System.out.println(g.getCurrentPlayer().getName());
        } catch (Exception e){
            System.out.println("Couldnt load" + e.getMessage());
        }
        
    }

    @FXML
    void newGameButtonPress(ActionEvent event) {
        titlePane.setVisible(false);
        newGamePane.setVisible(true);
    }

    @FXML
    void backButtonPress(ActionEvent event) {
        newGamePane.setVisible(false);
        titlePane.setVisible(true);
    }

    @FXML
    void swapButtonPress(MouseEvent event) {

        double tempX = p1Indicator.getLayoutX();
        p1Indicator.setLayoutX(p2Indicator.getLayoutX());
        p2Indicator.setLayoutX(tempX);

        String temp = p1Faction;
        p1Faction = p2Faction;
        p2Faction = temp;

    }

    @FXML
    void continueButtonPress(ActionEvent event) throws IOException {
        System.out.println("Player 1 faction: " + p1Faction);
        System.out.println("Player 2 faction: " + p2Faction);

        GRMapViewController grMapViewController = grMapView.getController();
        grMapViewController.createP1Faction(p1Faction);
        grMapViewController.createP2Faction(p2Faction);

        grMapViewController.preShow();
        grMapView.start();   
    }

    @FXML
    void initialize() {

        titlePane.setVisible(true);
        newGamePane.setVisible(false);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyValue tB_kv = new KeyValue(topBar.xProperty(), 1024);
        KeyValue tBB_kv = new KeyValue(topBarBackup.xProperty(), 1024);

        KeyValue bB_kv = new KeyValue(botBar.xProperty(), -1024);
        KeyValue bBB_kv = new KeyValue(botBarBackup.xProperty(), -1024);

        KeyFrame tB_kf = new KeyFrame(Duration.seconds(120), tB_kv);
        timeline.getKeyFrames().add(tB_kf);
        KeyFrame tBB_kf = new KeyFrame(Duration.seconds(120), tBB_kv);
        timeline.getKeyFrames().add(tBB_kf);

        KeyFrame bB_kf = new KeyFrame(Duration.seconds(120), bB_kv);
        timeline.getKeyFrames().add(bB_kf);
        KeyFrame bBB_kf = new KeyFrame(Duration.seconds(120), bBB_kv);
        timeline.getKeyFrames().add(bBB_kf);


        timeline.play();

    }

    public void setGRMapView(GRMapView grMapView) {
        this.grMapView = grMapView;
    }

}
