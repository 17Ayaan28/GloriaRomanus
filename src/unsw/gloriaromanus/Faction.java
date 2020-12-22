package unsw.gloriaromanus;

import java.util.List;
import java.io.Serializable;

public class Faction implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Province> province_list;
    private int faction_gold;
    private int faction_wealth;

    public Faction(String name, List<Province> province_list){
        this.name = name;
        this.province_list = province_list;
        this.faction_gold = 1000;
        this.faction_wealth = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Province> getProvince_list() {
        return province_list;
    }

    public void setProvince_list(List<Province> province_list) {
        this.province_list = province_list;
    }

    public void addProvince_List(Province province){
        province_list.add(province);
    }

    public int getFaction_gold() {
        return faction_gold;
    }

    public void setFaction_gold(int faction_gold) {
        this.faction_gold = faction_gold;
    }

	public void addGold(int amt) {
        this.faction_gold += amt;
	}

	public void subtractGold(int amt) {
        this.faction_gold -= amt;
	}

	public int getPortBonus() {

        int bonus = 0;
        for (Province p : province_list) {
            if (p.getPort()!=null) {
                bonus += p.getPort().getWealthBonus();
            }
        }

        return bonus;

    }

    public double getMarketBonus() {

        double bonus = 1;
        for (Province p : province_list) {
            if (p.getMarket()!=null) {
                bonus *= (1 - p.getMarket().getCostReduction());
            }
        }

        // Max bonus is 75% off.
        return (bonus>=0.75) ? bonus : 0.75;

    }

    public int getFaction_wealth() {
        return faction_wealth;
    }

    public void setFaction_wealth(int faction_wealth) {
        this.faction_wealth = faction_wealth;
    }

    public int calculate_wealth(){
        /*
        int total = 0;
        for (Province p: this.province_list){
            total = total + p.getWealth();
        }

        this.setFaction_wealth(total);
        */
        return this.getFaction_wealth();
    }

}
