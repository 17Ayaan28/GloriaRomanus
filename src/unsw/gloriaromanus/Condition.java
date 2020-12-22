package unsw.gloriaromanus;

public class Condition implements v_condition {
    private String type;

    public Condition(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int show(Faction p1) {
        
        if (type.equals("TREASURY")) {
            
            if (p1.getFaction_gold() >= 100000) {
                //System.out.println("Location - 3");
                return 1;
            }
        } else if (type.equals("WEALTH")) {

            if (p1.calculate_wealth() >= 400000) {
                return 1;
            }

        } else if (type.equals("INFRASTRUCTURE")) {

            System.out.println("Incomplete");
            return 0;

        } else if (type.equals("CONQUEST")) {
            if(p1.getProvince_list().size() == 52){
                return 1;
            }
        }
        return 0;

    }


}