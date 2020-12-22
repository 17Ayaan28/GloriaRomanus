package unsw.gloriaromanus.buildings;

import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.TurnSystem.TurnManager;
import java.io.Serializable;

public class Mine extends WealthGenerationBuilding implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private double soldierDiscount;

    public Mine(Province p, TurnManager tm) {
        super(p,tm);
        this.setSoldierDiscount(0.1);
        this.setBaseWealth(25);
        this.setWealthPerTurn(1);
    }

    @Override
    public void upgrade() {
        if (this.getLevel() < 5) {
            this.addLevel(1);
            this.addSoldierDiscount(0.1);
            // TODO Reduce the initial cost of all soldiers for the province.
            // TODO IF MAX RANK Reduce the turns to build for each building in the province.    
        }
    }

    // GETTERS AND SETTERS
    public double getSoldierDiscount() {
        return soldierDiscount;
    }

    public void setSoldierDiscount(double soldierDiscount) {
        this.soldierDiscount = soldierDiscount;
    }

    public void addSoldierDiscount(double soldierDiscount) {
        this.soldierDiscount += soldierDiscount;
    }

    public boolean isMaxLevel() {
        return (this.getLevel() == 5);
    }


}
