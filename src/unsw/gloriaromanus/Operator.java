package unsw.gloriaromanus;

import java.util.ArrayList;
import java.io.Serializable;
public class Operator implements v_condition, Serializable {

    private String operator;
    private ArrayList<v_condition> conditions_list;

    public Operator(){
        this.operator = null;
        this.conditions_list = new ArrayList<v_condition>();
    }


    @Override
    public int show(Faction p1){

        int count = 0;

        if(operator.equals("OR")){
            //System.out.println("hello");
            for (v_condition v: conditions_list){
                if (v.show(p1) == 1){
                    count++;
                }
            }

            if (count >= 1){
                return 1;
            }
    
            return 0;

        } else {
            //System.out.println("hi");
            for (v_condition v: conditions_list){
                
                if (v.show(p1) == 1){
                    count++;
                }
            }
            //System.out.println(count);
            if (count >= 2){
                
                return 1;
            }
    
            return 0;

        }
       
    }

    public void addConditions_list(v_condition v){
        conditions_list.add(v);
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ArrayList<v_condition> getConditions_list() {
        return conditions_list;
    }

    public void setConditions_list(ArrayList<v_condition> conditions_list) {
        this.conditions_list = conditions_list;
    }

    
}