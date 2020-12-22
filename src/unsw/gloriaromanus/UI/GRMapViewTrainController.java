package unsw.gloriaromanus.UI;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import unsw.gloriaromanus.Faction;
import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.Unit;

public class GRMapViewTrainController extends GRMapViewMenuController {
    
    GRMapViewController parent;

    private Province currProvince;

    private static Color red = Color.rgb(215, 69, 69);
    private static Color blue = Color.rgb(69, 144, 209);

    @FXML
    private Pane trainPane;

    @FXML
    private Label title;

    @FXML
    private Label ttNameLabel;

    @FXML
    private Label ttTrainingSlots;

    @FXML
    private ListView<String> ttCurrTroops;

    @FXML
    private ComboBox<Integer> numTroops;

    @FXML
    private ListView<String> ttCurrentTraining;

    @FXML
    private ComboBox<String> ttTroopSelector;

    @FXML
    private Label ttCost;

    @FXML
    private Button ttTrainButton;

    @FXML
    private Label display;

    @FXML
    void backButtonPress(ActionEvent event) {
        parent.changeToManageMenu();
    }

    @FXML
    void ttTrainButtonPress(ActionEvent event) {

        //For gold Check
        //currProvince.getOccupant().setFaction_gold(1);
        String t = ttTroopSelector.getValue().toLowerCase();
        Integer i = numTroops.getValue();

        if(t.equals("heavy infantry")){
            
            if(currProvince.getOccupant().getFaction_gold() < 5*i){
                display.setText("You do not have enough money");
                return;
            }
        } else if(t.equals("berserker")){
            if(currProvince.getOccupant().getFaction_gold() < 10*i){
                display.setText("You do not have enough money");
                return;
            }

            if (currProvince.getTroopBuilding().getLevel() <= 1) {
                display.setText("Building Chain not complete");
                return;
            }

        } else if(t.equals("missile infantry")){
            if(currProvince.getOccupant().getFaction_gold() < 20*i){
                display.setText("You do not have enough money");
                return;
            }
        } else if(t.equals("melee cavalry")){
            if(currProvince.getOccupant().getFaction_gold() < 15*i){
                display.setText("You do not have enough money");
                return;
            }
        } else if(t.equals("horse archers")){
            if(currProvince.getOccupant().getFaction_gold() < 15*i){
                display.setText("You do not have enough money");
                return;
            }

            if (currProvince.getTroopBuilding().getLevel() <= 2) {
                display.setText("Building Chain not complete");
                return;
            }
        }

        if (currProvince.getTroopBuilding() != null) {
            try {
                currProvince.getTroopBuilding().add_units(t, i);

            } catch (IOException e) {
                
                e.printStackTrace();
            }
        } else {
            display.setText("The Province does not have a troop building");
        }
        parent.updateTurnText();
        this.reInit();
    }

    @FXML
    void initialize() {

        ArrayList<String> troopNames = new ArrayList<String>();
        troopNames.add("heavy infantry"); troopNames.add("missile infantry");
        troopNames.add("berserker"); troopNames.add("melee cavalry");
        troopNames.add("horse archers");

        ObservableList<String> troops = FXCollections.observableArrayList(troopNames);
        ttTroopSelector.setItems(troops);

        ArrayList<Integer> count_troops = new ArrayList<Integer>();
        count_troops.add(1); count_troops.add(3);
        count_troops.add(2); count_troops.add(4);
        count_troops.add(5); count_troops.add(6);
        count_troops.add(7); count_troops.add(8);
        count_troops.add(9); count_troops.add(10);


        ObservableList<Integer> num = FXCollections.observableArrayList(count_troops);
        numTroops.setItems(num);

        

    }

    @FXML
    void onTroopSelect(ActionEvent event){

        if(ttTroopSelector.getValue().toLowerCase().equals("heavy infantry")){
            ttCost.setText("5");
        } else if(ttTroopSelector.getValue().toLowerCase().equals("berserker")){
            ttCost.setText("10");
        } else if(ttTroopSelector.getValue().toLowerCase().equals("missile infantry")){
            ttCost.setText("20");
        } else if(ttTroopSelector.getValue().toLowerCase().equals("melee cavalry")){
            ttCost.setText("15");
        } else if(ttTroopSelector.getValue().toLowerCase().equals("horse_archers")){
            ttCost.setText("15");
        }
    }

    public void postInit() {
        
        parent = getParent();

    }

    public void reInit() {
        
        currProvince = parent.getSelectedProvince();
        ttNameLabel.setText(currProvince.getName());

        Faction owner = currProvince.getOccupant();

        if (owner.equals(parent.getP1())) {
            ttNameLabel.setTextFill(red);
            title.setTextFill(red);
        } else {
            ttNameLabel.setTextFill(blue);
            title.setTextFill(blue);
        }

        ttTrainingSlots.setText("" + currProvince.getTroopsPerTurn());

        ArrayList<String> hold_list = new ArrayList<String>();

        if (currProvince.getTroopBuilding() != null){
            for(Unit u: currProvince.getTroopBuilding().getTroop_holder()){
                hold_list.add(u.getType());
            }
        } else {
            ttTrainButton.setDisable(true);
        }

        ObservableList<String> items =FXCollections.observableArrayList(hold_list);
        ttCurrentTraining.setItems(items);
        //TODO
        ArrayList<String> troop_list = new ArrayList<String>();

        if (currProvince.getTroopBuilding() != null){
            for(Unit u: currProvince.getUnit_list()){
                troop_list.add(u.getType());
            }
        } else {
            ttTrainButton.setDisable(true);
        }

        ObservableList<String> items_1 =FXCollections.observableArrayList(troop_list);
        ttCurrTroops.setItems(items_1);

  
        ttTroopSelector.setValue("");
        ttCost.setText("");
        numTroops.setPromptText("Choose number of troops");
        display.setText("");

    }



}
