package unsw.gloriaromanus.UI;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import unsw.gloriaromanus.Faction;
import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.buildings.Building;
import unsw.gloriaromanus.buildings.Farm;
import unsw.gloriaromanus.buildings.Market;
import unsw.gloriaromanus.buildings.Mine;
import unsw.gloriaromanus.buildings.Port;
import unsw.gloriaromanus.buildings.Smith;
import unsw.gloriaromanus.buildings.TroopBuilding;

public class GRMapViewBuildController extends GRMapViewMenuController{
   
    GRMapViewController parent;

    private Province currProvince;
    private double costReduction = 1;
    private boolean isLandlocked = false;

    private static Color red = Color.rgb(215, 69, 69);
    private static Color blue = Color.rgb(69, 144, 209);


    @FXML
    private Pane buildingPane;

    @FXML
    private Label title;

    @FXML
    private Label mBNameLabel;

    @FXML
    private Label mBWealthLabel;

    @FXML
    private Label mBWealthPTurn;

    @FXML
    private Label mBCurrBuilding;

    @FXML
    private Label mBCurrTroop;

    @FXML
    private Label mBCurrFarm;

    @FXML
    private Label mBCurrMarket;

    @FXML
    private Label mBCurrPort;

    @FXML
    private Label mBCurrMine;

    @FXML
    private Label mBCurrSmith;

    @FXML
    private ComboBox<String> mBBuildingSelector;

    @FXML
    private Label mBCost;

    @FXML
    private Label mBTurnCntLevel;

    @FXML
    private Button buildButton;

    @FXML
    void backButtonPress(ActionEvent event) {
        parent.changeToManageMenu();
    }

    @FXML
    void onBuildingSelect(ActionEvent event) {

        if (mBBuildingSelector.getValue().equals("Port") && isLandlocked) {
            buildButton.setDisable(true);
        } else {
            buildButton.setDisable(false);
        }

        int cost = 100;
        switch (mBBuildingSelector.getValue()) {
            case "Troop":
                if (currProvince.getTroopBuilding() != null) {
                    cost = currProvince.getTroopBuilding().getCost();
                    if (currProvince.getTroopBuilding().getLevel() == 5) buildButton.setDisable(true);
                }
            break;
            case "Farm":
                if (currProvince.getFarm() != null) {
                    cost = currProvince.getFarm().getCost();
                    if (currProvince.getFarm().getLevel() == 5) buildButton.setDisable(true);
                }
            break;
            case "Market":
                if (currProvince.getMarket() != null) {
                    cost = currProvince.getMarket().getCost();
                    if (currProvince.getMarket().getLevel() == 5) buildButton.setDisable(true);
                }
            break;
            case "Port":
                if (currProvince.getPort() != null) {
                    cost = currProvince.getPort().getCost();
                    if (currProvince.getPort().getLevel() == 5) buildButton.setDisable(true);
                }
            break;
            case "Mine":
                if (currProvince.getMine() != null) {
                    cost = currProvince.getMine().getCost();
                    if (currProvince.getMine().getLevel() == 5) buildButton.setDisable(true);
                }
            break;
            case "Smith":
                if (currProvince.getSmith() != null) {
                    cost = currProvince.getSmith().getCost();
                    if (currProvince.getSmith().getLevel() == 5) buildButton.setDisable(true);
                }
             break;

        }

        cost *= costReduction;
        mBCost.setText((int)cost + "G");

        if (cost > currProvince.getOccupant().getFaction_gold()) {
            buildButton.setDisable(true);
        }

        if (currProvince.getCurrBuilding() != null) {
            buildButton.setDisable(true);
        }

    }

    @FXML
    void buildButtonPress(ActionEvent event) {
        // System.out.println(mBBuildingSelector.getValue());
        String b = mBBuildingSelector.getValue().toLowerCase();
        currProvince.createBuilding(b);
        this.reInit();

        parent.updateTurnText();
    }

    @FXML
    void initialize() {

        ArrayList<String> buildingNames = new ArrayList<String>();
        buildingNames.add("Troop"); buildingNames.add("Farm");
        buildingNames.add("Market"); buildingNames.add("Port");
        buildingNames.add("Mine"); buildingNames.add("Smith");

        ObservableList<String> buildings = FXCollections.observableArrayList(buildingNames);
        mBBuildingSelector.setItems(buildings);

    }

    public void postInit() {
        
        parent = getParent();

    }
    
    public void reInit() {
        
        currProvince = parent.getSelectedProvince();
        mBNameLabel.setText(currProvince.getName());
        isLandlocked = currProvince.isLandLocked();

        mBBuildingSelector.setValue("");
        mBCost.setText("");

        Faction owner = currProvince.getOccupant();
        costReduction = owner.getMarketBonus();

        if (owner.equals(parent.getP1())) {
            mBNameLabel.setTextFill(red);
            title.setTextFill(red);
        } else {
            mBNameLabel.setTextFill(blue);
            title.setTextFill(blue);
        }

        mBWealthLabel.setText("" + currProvince.getWealth());
        mBWealthPTurn.setText("" + currProvince.getWealth());

        Building currBuilding = currProvince.getCurrBuilding();
        if (currBuilding == null) {
            mBCurrBuilding.setText("None");
            buildButton.setDisable(false);
            mBTurnCntLevel.setVisible(false);
        } else {
            String building = currBuilding.getClass().getSimpleName();
            mBCurrBuilding.setText(building);
            buildButton.setDisable(true);
            mBTurnCntLevel.setVisible(true);
            mBTurnCntLevel.setText(currBuilding.getTurnsLeft() + " turn(s) remaining.");
        }

        TroopBuilding pTroop = currProvince.getTroopBuilding();
        if (pTroop == null) {
            mBCurrTroop.setText("None");
        } else {
            mBCurrTroop.setText("Level " + pTroop.getLevel());
        }

        Farm pFarm = currProvince.getFarm();
        if (pFarm == null) {
            mBCurrFarm.setText("None");
        } else {
            mBCurrFarm.setText("Level " + pFarm.getLevel());
        }

        Market pMarket = currProvince.getMarket();
        if (pMarket == null) {
            mBCurrMarket.setText("None");
        } else {
            mBCurrMarket.setText("Level " + pMarket.getLevel());
        }

        if (!isLandlocked) {
            Port pPort = currProvince.getPort();
            if (pPort == null) {
                mBCurrPort.setText("None");
            } else {
                mBCurrPort.setText("Level " + pPort.getLevel());
            }    
        } else {
            mBCurrPort.setText("Unavailable");
        }

        Mine pMine = currProvince.getMine();
        if (pMine == null) {
            mBCurrMine.setText("None");
        } else {
            mBCurrMine.setText("Level " + pMine.getLevel());
        }

        Smith pSmith = currProvince.getSmith();
        if (pSmith == null) {
            mBCurrSmith.setText("None");
        } else {
            mBCurrSmith.setText("Level " + pSmith.getLevel());
        }
        

    }


}
