package unsw.gloriaromanus.buildings;

import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.Unit;
import unsw.gloriaromanus.TurnSystem.TurnListener;
import unsw.gloriaromanus.TurnSystem.TurnManager;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


// TODO ADD BONUSES TO TROOPS RECRUITED IN FACTION
/**
 * @author Ayaan
 * Troop Production Building adds troops to the Faction's Army.
 */
public class TroopBuilding implements Building, TurnListener, Serializable {
    
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int level;
    private int baseWealth;
    
    private int cost = 100;
    private int turnsToComplete = 5;

    private Province province;
    
    //private ArrayList<Pair<Unit, Integer>> troop_holder;
    private List<Unit> troop_holder;
    private List<Integer> troop_training_finish;

    private int n_turn;

    public TroopBuilding(Province p, TurnManager tm) {
        tm.subscribe(this);
        this.level = 1;
        this.baseWealth = 25;
        this.province = p;
        this.troop_holder = new ArrayList<Unit>();
        this.troop_training_finish = new ArrayList<Integer>();
        this.n_turn = 1;
    }

	public void upgrade() {
        if (level < 5) {
            this.level += 1;
        }
    }

    // GETTERS AND SETTERS
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBaseWealth() {
        return baseWealth * level;
    }

    public void setBaseWealth(int baseWealth) {
        this.baseWealth = baseWealth;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Province getProvince() {
        return this.province;
    }

    /*
    public void addTroop_holder (Pair<Unit, Integer> troop){
        this.troop_holder.add(troop);
    }

    public void removeTroop_holder (Pair<Unit, Integer> troop){
        this.troop_holder.remove(troop);
    }
    */

    public void addTroop_holder(Unit u){
        this.troop_holder.add(u);
    }

    public void removeTroop_holder(Unit u){
        this.troop_holder.remove(u);
    }

    public void addTroop_training_finish(Integer i){
        this.troop_training_finish.add(i);
    }

    public void removeTroop_training_finish(Integer i){
        this.troop_training_finish.remove(i);
    }

    @Override
    public void update() {

        if (turnsToComplete > 0) {
            turnsToComplete -= 1;
        }

        this.n_turn = this.n_turn + 1;

        for (int i = 0; i < troop_training_finish.size(); i++){

            if (troop_training_finish.get(i) <= this.n_turn){

                this.province.addUnit_list(this.troop_holder.get(i));

                removeTroop_holder(this.troop_holder.get(i));

                removeTroop_training_finish(troop_training_finish.get(i)); // Might not work, modifying while iterating

                int increased_troop_slots = this.province.getTroopsPerTurn() + 1;
                this.province.setTroopsPerTurn(increased_troop_slots);

            }
        }

    }


    public void add_units(String unit_type, int num_troops) throws IOException {


        if (this.province.getTroopsPerTurn() == 0){

            System.out.println("The province cannot train more troops!");
            return;
        }

        if (this.getProvince().getFarm() != null){

            // Decide Building chains required for various units
            if (unit_type.equals("heavy infantry")){

                if (this.province.getTroopBuilding().getLevel() < 0) {
                    System.out.println("Building Chain not complete for specified unit");
                    return;
                }
            } else if (unit_type.equals("berserker")){
    
                    if (this.province.getTroopBuilding().getLevel() <= 1) {
                        System.out.println("Building Chain not complete for specified unit");
                        return;
                    }
    
            } else if (unit_type.equals("horse archers")){
    
                if (this.province.getTroopBuilding().getLevel() <= 2) {
                    System.out.println("Building Chain not complete for specified unit");
                    return;
                }
            }

        } else {
            System.out.println("The Province does not have a farm");
        }


        String troops_string = Files.readString(Paths.get("src/unsw/gloriaromanus/troops.json"));
        JSONArray troops_array = new JSONArray(troops_string);


        for (int i = 0; i < troops_array.length(); i++){  

            JSONObject troop_category = troops_array.getJSONObject(i);

            String type = troop_category.getString("name");
            
            // Create the troop and add it to the army
            if (unit_type.equals(type)){

                int range = troop_category.getInt("range");
                int armour = troop_category.getInt("armour");
                int morale = troop_category.getInt("morale");
                int speed = troop_category.getInt("speed");
                int attack = troop_category.getInt("attack");
                int defenseSkill = troop_category.getInt("defenseSkill");
                int shieldDefense = troop_category.getInt("shieldDefense");

                int turns_to_train = troop_category.getInt("turns_to_train");
                int movement_points = troop_category.getInt("movement_points");
                int cost = troop_category.getInt("cost");

                if (this.getProvince().getOccupant().getFaction_gold() < cost*num_troops){
                    System.out.println("The faction does not have enough money");
                    return;
                } else {
                    Integer amount = this.getProvince().getOccupant().getFaction_gold() - cost*num_troops;
                    this.getProvince().getOccupant().setFaction_gold(amount);
                }

                Unit u = new Unit(num_troops, range, armour, morale, speed, attack, defenseSkill, shieldDefense, this.province, turns_to_train, movement_points, cost, type);
       
                // Holds troops till turn to create
                addTroop_holder(u);
                addTroop_training_finish(n_turn+turns_to_train + 1);
                int reduced_troop_slots = this.province.getTroopsPerTurn() - 1;
                this.province.setTroopsPerTurn(reduced_troop_slots);

                System.out.println("Unit will be added to the army after training");
                return;
            }else {
                continue;
            }

        }

        System.out.println("The specified troop does not exist");
    }

    public List<Unit> getTroop_holder() {
        return troop_holder;
    }

    public void setTroop_holder(List<Unit> troop_holder) {
        this.troop_holder = troop_holder;
    }

    @Override
    public boolean isReady() {
        return (turnsToComplete==0);
    }

    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public int getTurnsLeft() {
        return turnsToComplete;
    }

}
