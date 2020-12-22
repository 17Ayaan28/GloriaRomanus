package unsw.gloriaromanus.UI;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import unsw.gloriaromanus.Faction;
import unsw.gloriaromanus.GloriaRomanusGameLogic;
import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.ResourceManager;
import unsw.gloriaromanus.TurnSystem.TurnManager;

public class GRMapViewDetailsController extends GRMapViewMenuController {

    GRMapViewController parent;
    TurnManager parentTM;

    Faction p1;
    Faction p2;
    Faction currentPlayer;

    private static Color red = Color.rgb(215, 69, 69);
    private static Color blue = Color.rgb(69, 144, 209);

    ObservableList<String> province_names;

    @FXML
    private Label cpLabel;
    private StringProperty cp_Value;

    @FXML
    private Label cpFactionLabel;
    private StringProperty cpFaction_Value;

    @FXML
    private Label cpGoldLabel;
    private StringProperty cpGold_Value;

    @FXML
    private ListView<String> provinceListView;

    @Override
    public void postInit() {

        provinceListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String old_selection, String new_selection) {
                if (new_selection != null) {
                    parent.changeSelectedProvince(new_selection);
                }
            }
        });
        
        parent = getParent();
        parentTM = parent.getTm();

        p1 = parent.getP1();
        p2 = parent.getP2();

        currentPlayer = parent.getCP();

        String currentPlayer_string = (currentPlayer.equals(p1)) ? "Player 1" : "Player 2";

        cp_Value = new SimpleStringProperty(currentPlayer_string);
        cpLabel.textProperty().bind(cp_Value);
        if (currentPlayer.equals(p1)) {
            cpLabel.setTextFill(red);
        } else {
            cpLabel.setTextFill(blue);
        }

        cpFaction_Value = new SimpleStringProperty(currentPlayer.getName());
        cpFactionLabel.textProperty().bind(cpFaction_Value);

        cpGold_Value = new SimpleStringProperty("" + currentPlayer.getFaction_gold());
        cpGoldLabel.textProperty().bind(cpGold_Value);
 
        List<Province> currentPlayer_provinces = currentPlayer.getProvince_list();
        ArrayList<String> currentPlayer_PNames = new ArrayList<String>();
        for (Province p : currentPlayer_provinces) {
            currentPlayer_PNames.add(p.getName());
        }

        ObservableList<String> provinces = FXCollections.observableArrayList(currentPlayer_PNames);
        provinceListView.setItems(provinces);

    }

    @FXML
    void endTurnButtonPress(ActionEvent event) {
        
        parentTM.nextTurn();

        parent.updateTurnText();

        // For victory condition check
        if(parent.getGrGameLogic().getN_turn() == 2){

            parent.getGrGameLogic().getPlayer1().setFaction_gold(1000000);
            parent.getGrGameLogic().getPlayer1().setFaction_wealth(50000000);
        }       

        if(parent.getGrGameLogic().checkVictory() == 1 && parent.getGrGameLogic().getContinue_after_victory() == 0){
            parent.changeToVictoryMenu();
        }

        parent.resetZoom();

        parent.deselectSelectedProvince();
        parent.setColorBasedOnPlayer();

        

        reInit();

    }

    @FXML
    void saveButtonPress(ActionEvent event){
        try {
            ResourceManager.save(parent.getGrGameLogic(), "1.save");
            System.out.println("Save Successful");

        }
        catch (Exception e) {
            System.out.println("Couldnt save" + e.getMessage());
        }
    }

    public void reInit() {

        currentPlayer = parent.getCP();

        String currentPlayer_string =  (currentPlayer.equals(p1)) ? "Player 1" : "Player 2";
        cp_Value.setValue(currentPlayer_string);
        if (currentPlayer.equals(p1)) {
            cpLabel.setTextFill(red);
        } else {
            cpLabel.setTextFill(blue);
        }

        cpFaction_Value.setValue(currentPlayer.getName());
        cpGold_Value.setValue("" + currentPlayer.getFaction_gold());

        List<Province> currentPlayer_provinces = currentPlayer.getProvince_list();
        ArrayList<String> currentPlayer_PNames = new ArrayList<String>();
        for (Province p : currentPlayer_provinces) {
            currentPlayer_PNames.add(p.getName());
        }

        ObservableList<String> provinces = FXCollections.observableArrayList(currentPlayer_PNames);
        provinceListView.setItems(provinces);

    }

    @FXML
    void manageProvinceButtonPress(ActionEvent event) {

        if (parent.getSelectedProvinceFeature() != null) {
            parent.changeToManageMenu();
        }

    }

}
