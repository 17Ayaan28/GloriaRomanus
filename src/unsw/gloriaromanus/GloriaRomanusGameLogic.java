package unsw.gloriaromanus;

import unsw.gloriaromanus.TurnSystem.TurnListener;
import unsw.gloriaromanus.TurnSystem.TurnManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONObject;

public class GloriaRomanusGameLogic implements TurnListener, Serializable {
    
    private static final long serialVersionUID = 1L;
    private boolean isSingleplayer;
    private Faction player1;
    private Faction player2;

    private int n_turn;
    private Operator goal;

    private Faction currentPlayer;

    private Integer continue_after_victory = 0;

    /**
     * Basic constructor for a local multiplayer Game Logic object
     * @param p1 Faction, Player One
     * @param p2 Faction, Player Two
     * @param tm
     */
    public GloriaRomanusGameLogic(Faction p1, Faction p2, TurnManager tm) {

        this.setSingleplayer(false);
        this.setPlayer1(p1);
        this.setPlayer2(p2);
        this.n_turn = 1;

        tm.subscribe(this);
        
        this.setCurrentPlayer(p1);

    }

    /**
     * Function called when turn is incremented.
     * Responsible for changing which player is in control.
     * @throws IOException
     */
    @Override
    public void update() {

        if (this.n_turn == 1){
            try {
                this.goal = getGoal();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } 

        if (!isSingleplayer) {
                      
            //int i = this.goal.show(this.currentPlayer);            
            
            //if (i != 0){
            //    System.out.println(this.currentPlayer + "WINS!!");
            //}

            if (this.getCurrentPlayer() == this.getPlayer1()) {
                this.setCurrentPlayer(this.getPlayer2());
            } else {
                this.setCurrentPlayer(this.getPlayer1());
            }

        }

        this.n_turn = n_turn + 1;

    }

    public int checkVictory(){

        int i = this.goal.show(this.currentPlayer); 
        return i;

    }

    public Operator getGoal() throws IOException {

        Random rand = new Random(); 
        int rand_int = rand.nextInt(2); 
        rand_int  = 1;

        String victory = get_victory();
        JSONArray victory_array = new JSONArray(victory);
        Operator o = new Operator();

        for (int i = 0; i < victory_array.length(); i++){  

            JSONObject victory_category = victory_array.getJSONObject(i);
 
            if(rand_int == victory_category.getInt("id")){

                String goal = victory_category.getString("goal");
                //System.out.println(goal);
                o.setOperator(goal);
                //System.out.println("Location - 1");
                o.setConditions_list(getSubgoals(victory_category.getJSONArray("subgoals")));
                //System.out.println("Location - 2");
            }
        } 
        //System.out.println(o.getConditions_list().size());
        //System.out.println(o.getOperator());
        //System.out.println(o.getConditions_list().size());

        return o;

    }

    public ArrayList<v_condition> getSubgoals(JSONArray subgoal_array) {

        ArrayList<v_condition> v_list = new ArrayList<v_condition>();

        for(int j = 0; j < subgoal_array.length(); j++){
            
            //System.out.println("Location - 2");

            JSONObject subgoals = subgoal_array.getJSONObject(j);
            //System.out.println(subgoals);
            if(subgoals.getString("goal").equals("TREASURY") ||subgoals.getString("goal").equals("WEALTH") || subgoals.getString("goal").equals("INFRASTRUCTURE") || subgoals.getString("goal").equals("CONQUEST")){
                //System.out.println(("Location - 1 " + subgoals.getString("goal")));
                Condition c = new Condition(subgoals.getString("goal"));
                v_list.add(c);
            }
            else {
                Operator o2 = new Operator();
                o2.setOperator(subgoals.getString("goal"));
                ArrayList<v_condition> l = getSubgoals(subgoals.getJSONArray("subgoals"));
                o2.setConditions_list(l);
                v_list.add(o2);

            }
        }

        return v_list;

    }


    public String get_victory() throws IOException {
        String victory = Files.readString(Paths.get("src/unsw/gloriaromanus/victory_conditions.json"));
        return victory;
    }



    // GETTERS AND SETTERS
    public boolean isSingleplayer() {
        return isSingleplayer;
    }

    public void setSingleplayer(boolean isSingleplayer) {
        this.isSingleplayer = isSingleplayer;
    }

    public Faction getPlayer1() {
        return player1;
    }

    public void setPlayer1(Faction player1) {
        this.player1 = player1;
    }

    public Faction getPlayer2() {
        return player2;
    }

    public void setPlayer2(Faction player2) {
        this.player2 = player2;
    }

    public Faction getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Faction currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getN_turn() {
        return n_turn;
    }

    public void setN_turn(int n_turn) {
        this.n_turn = n_turn;
    }

    public void setGoal(Operator goal) {
        this.goal = goal;
    }

    public Operator get_goal() {
        return this.goal;
    }

    public Integer getContinue_after_victory() {
        return continue_after_victory;
    }

    public void setContinue_after_victory(Integer continue_after_victory) {
        this.continue_after_victory = continue_after_victory;
    }
    /*
    public static void main(String[] args) throws IOException {
        Faction Rome = new Faction("Rome", null);
        Faction India = new Faction("India", null);

        TurnManager tm = new TurnManager();
        GloriaRomanusGameLogic gl = new GloriaRomanusGameLogic(Rome, India, tm);

        tm.nextTurn();
        //System.out.println(gl.getGoal().getConditions_list().size());
        gl.getPlayer1().setFaction_gold(1000000);
        gl.getPlayer1().setFaction_wealth(4000000);

        tm.nextTurn();
        System.out.println(gl.checkVictory());
        //tm.nextTurn();

        //System.out.println(gl.checkVictory());


    }
    */
    
}
