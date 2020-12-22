package unsw.gloriaromanus.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class GRMapViewMenuController {

    private GRMapViewController parent;

    public void setParent(GRMapViewController parent) {
        if (parent == null){
            System.out.println("GOT NULL");
        }
        this.parent = parent;
    }

    public GRMapViewController getParent(){
        return parent;
    }

    @FXML
    public void clickedSwitchMenu(ActionEvent e) throws Exception {
        // parent.switchMenu();
    }

	public void postInit() {
	}
    
}
