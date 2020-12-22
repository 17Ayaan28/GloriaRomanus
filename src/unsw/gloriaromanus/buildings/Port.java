package unsw.gloriaromanus.buildings;

import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.TurnSystem.TurnManager;
import java.io.Serializable;

public class Port extends WealthGenerationBuilding implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int wealthBonus;

    public Port(Province p, TurnManager tm) {
        super(p,tm);
        this.setWealthBonus(10);
        this.setBaseWealth(25);
        this.setWealthPerTurn(1);
    }

    @Override
    public void upgrade() {
        this.addLevel(1);
        this.addWealthBonus(10);
    }

    // GETTERS AND SETTERS
    public int getWealthBonus() {
        return wealthBonus;
    }

    public void setWealthBonus(int wealthBonus) {
        this.wealthBonus = wealthBonus;
    }

    public void addWealthBonus(int wealthBonus) {
        this.wealthBonus += wealthBonus;
    }
    
}
