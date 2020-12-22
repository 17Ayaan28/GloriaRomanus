package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.Serializable;
import org.json.JSONObject;

import unsw.gloriaromanus.TurnSystem.TurnListener;
import unsw.gloriaromanus.TurnSystem.TurnManager;
import unsw.gloriaromanus.buildings.*;

public class Province implements TurnListener, Serializable {

    private static final long serialVersionUID = 1L;
    private TurnManager tm;
    private String name;
    private boolean landLocked;
    private Faction occupant;
    //TODO
    private List<Unit> unit_list;

    private int troopsPerTurn = 1;

    private BuildingFactory bf;
    private Building currBuilding;                          // building currently being constructed
    private ArrayList<Building> buildings;                  // collection of buildings
    private TroopBuilding troopBuilding;                    // troop building in the province
    private Farm farm;                                      // farm in the province
    private Market market;                                  // market in the province
    private Port port;                                      // port in the province
    private Mine mine;                                      // mine in the province
    private Smith smith;                                    // smith in the province
    
    private int requiredMovementPoints = 4;                 // movement points used when moving across a province.

    private int wealth = 0;                                 // total wealth of province, starts at 0
    private int wealthPerTurn = 0;                          // wealth/turn of province, starts at 0
    private int totalWealthBonus = 0;
    private double taxRate = 0.15;

    public Province(String name, boolean isLandLocked, TurnManager tm){
        this.name = name;
        this.landLocked = isLandLocked;
        this.buildings = new ArrayList<Building>();
        this.unit_list = new ArrayList<Unit>();
        
        this.tm = tm;
        tm.subscribe(this);
        this.bf = new BuildingFactory(this);
    }

    // BUILDING CONSTRUCTION
    /**
     * Function to create or upgrade a building through a factory.
     * @param b String, representing which building to construct.
     * Can have the values "troop", "farm", "market", "port", "mine",
     * or "smith".
     */
    public void createBuilding(String b) {
        if (this.getCurrBuilding() == null) {
            Building product = bf.getBuilding(b);
            if (product != null) {
                this.setCurrBuilding(product);
            } else {
                recalculateWealth();
            }
        }
    }

    /**
     * This method recalculates the wealth and wealth per turn of a province.
     * It is called upon construction or upgrade of a building.
     */
    public void recalculateWealth() {

        int newWealth = 0;
        int newWealthPerTurn = 0;

        // Iterate through each building in this province, adding its base wealth to the sum.
        for (Building b : this.getBuildings()) {

            newWealth += b.getBaseWealth();

            // If the building is a wealth generation building, add its WPT value to the sum.
            if (b instanceof WealthGenerationBuilding) {
                WealthGenerationBuilding wgb = (WealthGenerationBuilding) b;
                newWealthPerTurn += wgb.getWealthPerTurn();
            }

        }

        // Add the wealth bonus of all the ports in the faction.
        newWealth += occupant.getPortBonus();

        // Reduce WPT if tax rate is too high.
        if (taxRate == 0.10) {
            newWealthPerTurn += 10;
        } else if (taxRate == 0.20) {
            newWealthPerTurn -= 10;
        } else if (taxRate == 0.25) {
            newWealthPerTurn -= 30;
        }
        
        newWealth += this.totalWealthBonus;

        // Set the new wealth and wealth per turn values.
        this.setWealth(newWealth);
        this.setWealthPerTurn(newWealthPerTurn);
    }

    // ADDERS (?)
    public void addFlatWealth(int amt) {
        this.wealth += amt;
    }

    public void addWealthPerTurn(int amt) {
        this.wealthPerTurn += amt;
    }

    public void addBuilding(Building b) {
        this.buildings.add(b);
    }


    // GETTERS AND SETTERS
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public int getTroopsPerTurn() {
        return troopsPerTurn;
    }
    
    public void addTroopsPerTurn(int i) {
        this.troopsPerTurn += i;
    }

    public void setTroopsPerTurn(int troopsPerTurn) {
        this.troopsPerTurn = troopsPerTurn;
    }

    public Building getCurrBuilding() {
        return currBuilding;
    }

    public void setCurrBuilding(Building currBuilding) {
        this.currBuilding = currBuilding;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(ArrayList<Building> buildings) {
        this.buildings = buildings;
    }

	public int getNumBuildings() {
        return this.buildings.size();
    }
    
    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public int getWealthPerTurn() {
        return wealthPerTurn;
    }

    public void setWealthPerTurn(int wealthPerTurn) {
        this.wealthPerTurn = wealthPerTurn;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public TroopBuilding getTroopBuilding() {
        return troopBuilding;
    }

    public void setTroopBuilding(TroopBuilding tb) {
        this.troopBuilding = tb;
    }

    public int getTroopBuildingLevel() {
		return this.getTroopBuilding().getLevel();
	}

    public Farm getFarm() {
        return farm;
    }

    public int getFarmLevel() {
		return farm.getLevel();
	}

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public boolean isLandLocked() {
        return landLocked;
    }

    public void setLandLocked(boolean landLocked) {
        this.landLocked = landLocked;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public int getMarketLevel() {
		return this.getMarket().getLevel();
	}

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public int getPortLevel() {
		return this.port.getLevel();
	}

    public Mine getMine() {
        return mine;
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }

	public int getMineLevel() {
		return this.getMine().getLevel();
	}

    public Smith getSmith() {
        return smith;
    }

    public void setSmith(Smith smith) {
        this.smith = smith;
    }

    public int getSmithLevel() {
		return this.getSmith().getLevel();
	}

    public int getRequiredMovementPoints() {
        return requiredMovementPoints;
    }

    public void setRequiredMovementPoints(int requiredMovementPoints) {
        this.requiredMovementPoints = requiredMovementPoints;
    }

    public void addUnit_list(Unit u){
        unit_list.add(u);
    }

    public void removeUnit_list (Unit u){
        unit_list.remove(u);
    }
    
    public Faction getOccupant() {
        return occupant;
    }
    
    public void setOccupant(Faction occupant) {
        this.occupant = occupant;
    }
    //TODO
    public List<Unit> getUnit_list() {
        return unit_list;
    }
    //TODO
    public void setUnit_list(List<Unit> unit_list) {
        this.unit_list = unit_list;
    }
    
    // implement BFS for shortest path
    public static boolean confirmIfProvincesConnected(Province province1, Province province2) throws IOException {
        String content = Files
                .readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        JSONObject provinceAdjacencyMatrix = new JSONObject(content);
        return provinceAdjacencyMatrix.getJSONObject(province1.getName()).getBoolean(province2.getName());
    }

    /**
     * Function called every time a turn is incremented.
     * Currently finishes the building of the current building.
     */
    @Override
    public void update() {

        // If there is a building being construted, finish the building.
        Building cb = this.getCurrBuilding();
        if (cb != null  && cb.isReady()) {
            if (cb instanceof TroopBuilding) {
                TroopBuilding tb = (TroopBuilding) cb;
                this.setTroopBuilding(tb); this.addBuilding(tb);
            } else if (cb instanceof Farm) {
                Farm f = (Farm) cb;
                this.setFarm(f); this.addBuilding(f);
            } else if (cb instanceof Market) {
                Market m = (Market) cb;
                this.setMarket(m); this.addBuilding(m);
            } else if (cb instanceof Port) {
                Port p = (Port) cb;
                this.setPort(p); this.addBuilding(p);
            } else if (cb instanceof Mine) {
                Mine m = (Mine) cb;
                this.setMine(m); this.addBuilding(m);
            } else if (cb instanceof Mine) {
                Mine m = (Mine) cb;
                this.setMine(m); this.addBuilding(m);
            } else if (cb instanceof Smith) {
                Smith s = (Smith) cb;
                this.setSmith(s); this.addBuilding(s);
            }

            // Free up the building slot.
            this.setCurrBuilding(null);

        }

        // Recalculate the wealth at the end of the turn.
        this.totalWealthBonus += this.wealthPerTurn;
        recalculateWealth();

        if (this.wealth < 0) {
            System.out.println("Got here");
            setWealth(0);
        }
        
        // Give this province's faction the gold.
        Faction f = this.getOccupant();
        int amt = (int) (this.getTaxRate() * this.getWealth());
        f.addGold(amt);

    }

    public static Province BattleResolver(Province p1, Province p2) throws IOException {

        if (confirmIfProvincesConnected(p1, p2)) {

            int army_strength1 = 0;
            for (Unit u: p1.getUnit_list()){
                army_strength1 = army_strength1 + (u.getAttack()*u.getNumTroops()*u.getDefenseSkill());
            }

            int army_strength2 = 0;
            for (Unit u: p2.getUnit_list()){
                army_strength2 = army_strength2 + (u.getAttack()*u.getNumTroops()*u.getDefenseSkill());
            }     

            // implement damage to troops

            Random r = new Random();

            int choice = r.nextInt(army_strength1+army_strength2+1);

            if(choice <= army_strength1){
                ArrayList<Unit> n = (ArrayList<Unit>) p1.getUnit_list();
                p2.setOccupant(p1.getOccupant());
                System.out.println(p2.getOccupant().getName());
                return p1;
            } else {
                System.out.println(p2.getOccupant());
                System.out.println("Army 1 lost the invasion");
                return p2;
            }

        } else {
            System.out.println("The Provinces are not adjacent");
            return null;
        }
        
    }


	public int getMovePoints() {
		return requiredMovementPoints;
	}

	public TurnManager getTurnManager() {
		return tm;
    }
    //TODO
    /*
    public static void main(String[] args) {
        TurnManager tm = new TurnManager();
        Province idk = new Province("idk",false,tm);

        for (Unit u: idk.getUnit_list()){
            System.out.println(u.getType());
        }
        //System.out.println(idk.getUnit_list().size());
    }
    */

}

