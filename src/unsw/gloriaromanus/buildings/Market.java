package unsw.gloriaromanus.buildings;

import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.TurnSystem.TurnManager;
import java.io.Serializable;


public class Market extends WealthGenerationBuilding implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private double costReduction;

    public Market(Province p, TurnManager tm) {
        super(p,tm);
        this.setCostReduction(0.01);                         
        this.setBaseWealth(25);
        this.setWealthPerTurn(1);
    }

    @Override
    public void upgrade() {
        this.addLevel(1);
        this.addCostReduction(0.01);
    }

    // GETTERS AND SETTERS
    public double getCostReduction() {
        return costReduction;
    }

    public void setCostReduction(double costReduction) {
        this.costReduction = costReduction;
    }

    public void addCostReduction(double costReduction) {
        this.costReduction += costReduction;
    }

    
}
