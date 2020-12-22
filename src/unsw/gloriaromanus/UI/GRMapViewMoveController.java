package unsw.gloriaromanus.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

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

public class GRMapViewMoveController extends GRMapViewMenuController {
    
    GRMapViewController parent;

    private Province sourceProvince;
    private Province destProvince;

    private static Color red = Color.rgb(215, 69, 69);
    private static Color blue = Color.rgb(69, 144, 209);
    
    @FXML
    private Pane trainPane;

    @FXML
    private Label title;

    @FXML
    private Label mtSource;

    @FXML
    private Label mtDestination;

    @FXML
    private ListView<String> mtAvailTroops;

    @FXML
    private ComboBox<String> mtTroopSelector;

    @FXML
    private Label mtMPts;

    @FXML
    private Button mtMoveButton;

    @FXML
    void backButtonPress(ActionEvent event) {
        parent.changeToManageMenu();
        parent.deselectDestinationProvince();
    }

    @FXML
    void mtMoveButtonPress(ActionEvent event) {

        ArrayList<Unit> u_list = new ArrayList<Unit>();

        for(String s: mtAvailTroops.getItems()){
            for(Unit u: sourceProvince.getUnit_list()){
                if(u.getType().equals(s)){
                    
                    u_list.add(u);
                }
            }
        }
        
        try {
            Unit.move_unit(u_list, sourceProvince, destProvince);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void initialize() {
        
    }

    @FXML
    void onSelect(ActionEvent event){
        String t = mtTroopSelector.getValue().toLowerCase();
        List<String> move_list = new ArrayList<String>();
        move_list.add(t);
        
        ObservableList<String> move = FXCollections.observableArrayList(move_list);
        
        mtAvailTroops.setItems(move);
        //mtTroopSelector.setItems(move);
        //mtAvailTroops.getItems().add(t);
        //mtTroopSelector.setValue("");
        
    }    

    public void postInit() {
        
        parent = getParent();

    }

    public void reInit() {
        
        sourceProvince = parent.getSelectedProvince();
        mtSource.setText(sourceProvince.getName());
        mtDestination.setText("Select Destination");

        Faction owner = sourceProvince.getOccupant();

        if (owner.equals(parent.getP1())) {
            mtSource.setTextFill(red);
            mtDestination.setTextFill(red);
            title.setTextFill(red);
        } else {
            mtSource.setTextFill(blue);
            mtDestination.setTextFill(blue);
            title.setTextFill(blue);
        }

        List<String> troop_list = new ArrayList<String>();
        for (Unit u: sourceProvince.getUnit_list()){
            troop_list.add(u.getType());
        }
        
        ObservableList<String> troops = FXCollections.observableArrayList(troop_list);
        
        mtTroopSelector.setItems(troops);

    }

    public void updateDestination() {
        destProvince = parent.getDestinationProvince();
        mtDestination.setText(destProvince.getName());
    }


}
