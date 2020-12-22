package unsw.gloriaromanus.UI;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import unsw.gloriaromanus.Faction;
import unsw.gloriaromanus.Province;

public class GRMapViewInvadeController extends GRMapViewMenuController{

    GRMapViewController parent;

    private Province invadingProvince;
    private Province targetProvince;

    private static Color red = Color.rgb(215, 69, 69);
    private static Color blue = Color.rgb(69, 144, 209);


    @FXML
    private Pane invadePane;

    @FXML
    private Label title;

    @FXML
    private Label invProvince;

    @FXML
    private Label invTarget;

    @FXML
    private Button invadeButton;

    @FXML
    private ListView<String> battleOutput;

    @FXML
    void backButtonPress(ActionEvent event) {
        parent.changeToManageMenu();
        parent.deselectTargetProvince();
        parent.setColorBasedOnPlayer();
    }

    @FXML
    void invButtonPress(ActionEvent event) {
        try {
            Province p = Province.BattleResolver(invadingProvince, targetProvince);
            if (p.getName().equals(invadingProvince.getName())){
                battleOutput.getItems().add(p.getOccupant().getName() + " won the battle");

            } else {
                battleOutput.getItems().add(p.getOccupant().getName() + " won the battle");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    @FXML
    void initialize() {

        // Probably dont need anything in here?

    }

    public void postInit() {
        
        parent = getParent();

    }

    public void reInit() {
        
        invadingProvince = parent.getSelectedProvince();
        invProvince.setText(invadingProvince.getName());
        invTarget.setText("Select Target");

        Faction cp = invadingProvince.getOccupant();

        if (cp.equals(parent.getP1())) {
            invProvince.setTextFill(red);
            invTarget.setTextFill(blue);
            title.setTextFill(red);
        } else {
            invProvince.setTextFill(blue);
            invTarget.setTextFill(red);
            title.setTextFill(blue);
        }

    }

    public void updateTarget() {
        targetProvince = parent.getTargetProvince();
        invTarget.setText(targetProvince.getName());
    }


}
