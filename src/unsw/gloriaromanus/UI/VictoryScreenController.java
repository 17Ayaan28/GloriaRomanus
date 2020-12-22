package unsw.gloriaromanus.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import unsw.gloriaromanus.Faction;

public class VictoryScreenController extends GRMapViewMenuController {
    
    GRMapViewController parent;
    Faction currentPlayer;

    private static Color red = Color.rgb(215, 69, 69);
    private static Color blue = Color.rgb(69, 144, 209);

    @FXML
    private Label playerLabel;

    public void postInit() {
        
        parent = getParent();

    }

    public void reInit() {

        currentPlayer = parent.getCP();
        if (currentPlayer.equals(parent.getP1())) {
            playerLabel.setText("Good Job, Player 1!");
            playerLabel.setTextFill(red);
        } else {
            playerLabel.setText("Good Job, Player 2!");
            playerLabel.setTextFill(blue);
        }

    }

    @FXML
    void continueButtonPress(ActionEvent event) {
        parent.getGrGameLogic().setContinue_after_victory(1);
        parent.changeToDetailsMenu();
        parent.setColorBasedOnPlayer();
    }
}
