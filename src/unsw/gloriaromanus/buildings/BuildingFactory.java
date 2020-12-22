package unsw.gloriaromanus.buildings;

import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.TurnSystem.TurnManager;
import java.io.Serializable;

public class BuildingFactory implements Serializable {
    
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Province owningProvince;
    TurnManager tm;

    public BuildingFactory(Province p) {
        this.owningProvince = p;
        this.tm = p.getTurnManager();
    }

    public Building getBuilding (String b) {

        double costReduction = owningProvince.getOccupant().getMarketBonus();
        switch (b) {
            case "troop":
                if (owningProvince.getTroopBuilding() == null) {
                    TroopBuilding tb = new TroopBuilding(owningProvince, tm);
                    int cost = (int) (tb.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    return tb;
                } else {
                    TroopBuilding tb = owningProvince.getTroopBuilding();
                    int cost = (int) (tb.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    tb.upgrade();
                }
                break;
            case "farm":
                if (owningProvince.getFarm() == null) {
                    Farm f = new Farm(owningProvince, tm);
                    int cost = (int) (f.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    return f;
                } else {
                    Farm f = owningProvince.getFarm();
                    int cost = (int) (f.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    f.upgrade();
                }
                break;
            case "market":
                if (owningProvince.getMarket() == null) {
                    Market m = new Market(owningProvince, tm);
                    int cost = (int) (m.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    return m;
                } else {
                    Market m = owningProvince.getMarket();
                    int cost = (int) (m.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    m.upgrade();
                }
                break;
            case "port":
                if (!owningProvince.isLandLocked()) {
                    if (owningProvince.getPort() == null) {
                        Port p = new Port(owningProvince, tm);
                        int cost = (int) (p.getCost() * costReduction);
                        owningProvince.getOccupant().subtractGold(cost);
                        return p;
                    } else {
                        Port p = owningProvince.getPort();
                        int cost = (int) (p.getCost() * costReduction);
                        owningProvince.getOccupant().subtractGold(cost);
                        p.upgrade();
                    }    
                }
                break;
            case "mine":
                if (owningProvince.getMine() == null) {
                    Mine m = new Mine(owningProvince, tm);
                    int cost = (int) (m.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    return m;
                } else {
                    Mine m = owningProvince.getMine();
                    int cost = (int) (m.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    m.upgrade();
                }
                break;
            case "smith":
                if (owningProvince.getSmith() == null) {
                    Smith s = new Smith(owningProvince, tm);
                    int cost = (int) (s.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    return s;
                } else {
                    Smith s = owningProvince.getSmith();
                    int cost = (int) (s.getCost() * costReduction);
                    owningProvince.getOccupant().subtractGold(cost);
                    s.upgrade();
                }
                break;
        }    

        return null;
    }
 
}
