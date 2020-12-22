package unsw.gloriaromanus.UI;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import unsw.gloriaromanus.Faction;
import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.Unit;

public class GRMapViewManageController extends GRMapViewMenuController {

    GRMapViewController parent;

    private Province currProvince;

    private static Color red = Color.rgb(215, 69, 69);
    private static Color blue = Color.rgb(69, 144, 209);

    @FXML
    private Pane mainPane;

    @FXML
    private Label pNameLabel;

    @FXML
    private Label pOwnerLabel;

    @FXML
    private Label pMovePtsLabel;

    @FXML
    private Label pWealthLabel;

    @FXML
    private Label pWealthPTurn;

    @FXML
    private Label pTaxRate;

    @FXML
    private Slider pTaxSlider;

    @FXML
    private ListView<String> troopsListView;

    @FXML
    private Button trainButton;
    @FXML
    private Button moveButton;
    @FXML
    private Button invadeButton;

    @FXML
    void invadeOpponentButtonPress(ActionEvent event) {
        parent.changeToInvadeMenu();
    }

    @FXML
    void manageBuildingsButtonPress(ActionEvent event) {
        parent.changeToBuildMenu();
    }

    @FXML
    void moveTroopsButtonPress(ActionEvent event) {
        parent.changeToMoveMenu();
    }

    @FXML
    void returnButtonPress(ActionEvent event) {
        parent.changeToDetailsMenu();
    }

    @FXML
    void trainTroopsButtonPress(ActionEvent event) {
        parent.changeToTrainMenu();
    }

    @FXML
    void initialize() {

    }

    public void postInit() {
        
        parent = getParent();

    }

    public void reInit() {
        
        currProvince = parent.getSelectedProvince();
        pNameLabel.setText(currProvince.getName());

        Faction owner = currProvince.getOccupant();
        pOwnerLabel.setText(owner.getName());

        if (owner.equals(parent.getP1())) {
            pNameLabel.setTextFill(red);
            pOwnerLabel.setTextFill(red);
        } else {
            pNameLabel.setTextFill(blue);
            pOwnerLabel.setTextFill(blue);
        }

        pMovePtsLabel.setText("" + currProvince.getMovePoints());
        
        pWealthLabel.setText("" + currProvince.getWealth());
        pWealthPTurn.setText("" + currProvince.getWealthPerTurn());

        
        pTaxSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) { 
                    pTaxRate.setText("" + round((int) (newValue.doubleValue()*1)) + "%");
                    currProvince.setTaxRate(newValue.doubleValue()/100);
                    currProvince.recalculateWealth();
                    pWealthPTurn.setText("" + currProvince.getWealthPerTurn());
                }
            }
        );
            
        pTaxSlider.setValue(currProvince.getTaxRate() * 100);

        if (currProvince.getTroopBuilding() == null) {
            trainButton.setDisable(true);
        } else {
            trainButton.setDisable(false);
        }

        if (currProvince.getUnit_list().isEmpty()) {
            moveButton.setDisable(true);
            invadeButton.setDisable(true);
        } else {
            moveButton.setDisable(false);
            invadeButton.setDisable(false);
        }

        List<String> troop_list = new ArrayList<String>();
        for (Unit u: currProvince.getUnit_list()){
            troop_list.add(u.getType());
        }

        ObservableList<String> troops = FXCollections.observableArrayList(troop_list);
        
        troopsListView.setItems(troops);

    }


    private int round(int num) {
        int remainder = num % 5;
        return remainder >= 3 ? (num - remainder + 5) : (num - remainder);
    }


}
