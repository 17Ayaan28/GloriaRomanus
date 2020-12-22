package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.Serializable;
import org.json.JSONObject;

//import unsw.gloriaromanus.TurnSystem.TurnListener;
//import unsw.gloriaromanus.TurnSystem.TurnManager;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers, heavy cavalry, elephants, chariots, archers, slingers, horse-archers, onagers, ballista, etc...
 * higher classes include ranged infantry, cavalry, infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent armour and morale)
 */

public class Unit implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int numTroops; // the number of troops in this unit (should reduce based on depletion)
    private int range;  // range of the unit
    private int armour;  // armour defense
    private int morale;  // resistance to fleeing
    private int speed;  // ability to disengage from disadvantageous battle
    private int attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    private int defenseSkill;  // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield
    private int turns_to_train; // Turns required to train the troop

    private Province province; //add province to json
    private int cost;
    private int movement_points; // Movement Points associated with unit

    private String type;


    // Constructor for Unit
    public Unit(int numTroops, int range, int armour, int morale, int speed, int attack, int defenseSkill, int shieldDefense, Province province, int turns_to_trains, int movement_points, int cost, String type){

        this.type = type;
        this.numTroops = numTroops;
        this.range = range;
        this.armour = armour;
        this.morale = morale;
        this.speed = speed;
        this.attack = attack;
        this.defenseSkill = defenseSkill;
        this.shieldDefense = shieldDefense;
        this.province = province;
        this.turns_to_train = turns_to_trains;
        this.movement_points = movement_points;
        this.cost = cost;
    }


    // Getters and Setters
    public int getNumTroops(){
        return numTroops;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefenseSkill() {
        return defenseSkill;
    }

    public void setDefenseSkill(int defenseSkill) {
        this.defenseSkill = defenseSkill;
    }

    public int getShieldDefense() {
        return shieldDefense;
    }

    public void setShieldDefense(int shieldDefense) {
        this.shieldDefense = shieldDefense;
    }


    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public int getTurns_to_train() {
        return turns_to_train;
    }

    public void setTurns_to_train(int turns_to_train) {
        this.turns_to_train = turns_to_train;
    }

    public int getMovement_points() {
        return movement_points;
    }

    public void setMovement_points(int movement_points) {
        this.movement_points = movement_points;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    


    // Will have to make something called unitid to get this to work
    /**
     * The function takes the following parameters to move the unit.
     * @param unit
     * @param start_province
     * @param destination_province
     * @throws IOException
     */
    public static void move_unit (ArrayList<Unit> UnitList, Province start_province, Province destination_province) throws IOException{


        // Checks if the provinces are connected
        if(Province.confirmIfProvincesConnected(start_province, destination_province)) {

            if (!start_province.getOccupant().equals(destination_province.getOccupant())){
                System.out.println("The province belongs to another Faction"); 
                return;
            }

            int total_unit_movement_points = 0;

            for (Unit a: UnitList){
                total_unit_movement_points = total_unit_movement_points + a.getMovement_points();
            }

            if(total_unit_movement_points < start_province.getRequiredMovementPoints()){
                System.out.println("The unit does not have enough movement points"); 
                return;
            }
            //TODO
            //System.out.println("Location - 2");
            for (Unit a: UnitList){
                a.setProvince(destination_province);
                start_province.removeUnit_list(a);
                destination_province.addUnit_list(a);
                a.setMovement_points(a.getMovement_points() - start_province.getRequiredMovementPoints());
            }

            

        } else {
            
            HashMap<String, String> Path = find_shortest_route (start_province, destination_province);

            if(Path != null){
                
                String rand = destination_province.getName();

                int total_movement_points = 0;
                while (!Path.get(rand).equals("invalid")) {

                    for(Province p: start_province.getOccupant().getProvince_list()){
                        if(p.getName().equals(rand)){
                           total_movement_points = total_movement_points + p.getRequiredMovementPoints();
                        }
                    }
                    //System.out.println(rand);
                    rand = Path.get(rand);
                }

                int total_unit_movement_points = 0;

                for (Unit a: UnitList){
                    total_unit_movement_points = total_unit_movement_points + a.getMovement_points();
                    //System.out.println(a.getType() + "Location - 1");
                }

                if(total_unit_movement_points >= total_movement_points){
                    //TODO DEBUG pls help
                    //System.out.println(UnitList.size() + " idk");

                    for (Unit a: UnitList){
                        System.out.println();
                        a.setProvince(destination_province);                      
                        start_province.removeUnit_list(a);                    
                        destination_province.addUnit_list(a);                     
                        a.setMovement_points(a.getMovement_points() - total_movement_points);
                        
                    }

                    System.out.println("The units Have been moved");

                } else {
                    System.out.println("The Unit does not have enough movement points to move to the given province");
                }

            } else {
                System.out.println("The unit is not able to move to the designated province as there are enemy provinces in the path");
            }
            
        }    
        
    }



    public static HashMap<String, String> find_shortest_route (Province start_province, Province destination_province) throws IOException {


        // Variable Declerations
        ArrayList<String> visited = new ArrayList<String>();
        LinkedList<String> queue = new LinkedList<String>();
        HashMap<String, Integer> distances = new HashMap<String, Integer>();
        HashMap<String, String> predecessor = new HashMap<String, String>();


        // Get connected provinces
        String content_1 = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        JSONObject provinceAdjacencyMatrix_1 = new JSONObject(content_1);
        Iterator<String> keys_1 = provinceAdjacencyMatrix_1.keys();


        // Set predecessors to invalid
        while (keys_1.hasNext()){             

            String key = keys_1.next();
            predecessor.put(key, "invalid");

        }

        // Add first province to queue
        visited.add(start_province.getName());
        queue.add(start_province.getName());
        distances.put(start_province.getName(), 1);    
        //predecessor.put(start_province.getName(), start_province.getName());

        while (queue.size() != 0) { 

            String s = queue.poll(); 
            ArrayList<String> adjacent = new ArrayList<String>();    
            Iterator<String> keys = provinceAdjacencyMatrix_1.keys();

            while (keys.hasNext()){             

                String key = keys.next();
                if (provinceAdjacencyMatrix_1.getJSONObject(s).getBoolean(key)){

                    for (Province p: start_province.getOccupant().getProvince_list()){
                        if (p.getName().equals(key)){

                            adjacent.add(key);

                        }
                    }
                   
                }

            }


            for (int i = 0; i< adjacent.size(); i++){

                if(!visited.contains(adjacent.get(i))){

                    visited.add(adjacent.get(i));
                    distances.put(adjacent.get(i), distances.get(s) + 1);
                    predecessor.put(adjacent.get(i), s);
                    queue.add(adjacent.get(i));

                    if (adjacent.get(i).equals(destination_province.getName())){
                    
                        return predecessor;
                                              
                    }
                    
                }
                
            }

        }
        return null;
    
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    
    // public static void main(String[] args) throws IOException {

    //     ArrayList<Province> p = new ArrayList<Province>();    

    //     Faction Rome = new Faction("Rome");
    //     TurnManager tm = new TurnManager();

    //     Province VIII = new Province("VIII", false, tm, Rome);     
    //     Province Britannia = new Province("Britannia", false, tm, Rome);
    //     Province IX = new Province("IX", false, tm, Rome);
    //     Province Alpes_Maritimae = new Province("Alpes Maritimae", false, tm, Rome);
    //     Province Narbonensis = new Province("Narbonensis", false, tm, Rome);
    //     Province Lugdunensis = new Province("Lugdunensis", false, tm, Rome);
    //     Province Alpes_Cottiae = new Province("Alpes Cottiae", false, tm, Rome);
        

    //     p.add(Lugdunensis);
    //     p.add(VIII);
    //     p.add(Britannia);
    //     p.add(IX);
    //     p.add(Alpes_Cottiae);
    //     p.add(Narbonensis);

    //     Rome.setProvince_list(p);

    //     Unit u = new Unit(10, 6, 5, 6, 6, 6, 7, 9, Britannia, 1, 6, 6, "berserker");
    //     u.setMovement_points(1000);

    //     Unit a = new Unit(20, 6, 5, 6, 6, 6, 7, 9, Britannia, 1, 6, 6, "artillery");
    //     a.setMovement_points(1000);


    //     Britannia.addUnit_list(u);
    //     Britannia.addUnit_list(a);

    //     System.out.println(VIII.getUnit_list().size());

    //     ArrayList<Unit> n = (ArrayList<Unit>) Britannia.getUnit_list();

    //     //System.out.println(n.size());
    //     move_unit(n, Britannia, VIII); 
        
    //     System.out.println(VIII.getUnit_list().size());
    // }
    
}

