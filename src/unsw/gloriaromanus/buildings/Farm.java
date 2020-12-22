package unsw.gloriaromanus.buildings;

import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.TurnSystem.TurnManager;
import java.io.Serializable;


public class Farm extends WealthGenerationBuilding implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Farm(Province p, TurnManager tm) {
        super(p, tm);
        this.setBaseWealth(25);
        this.setWealthPerTurn(1);
    }

    @Override
    public void upgrade() {
        this.addLevel(1);
        this.getProvince().addTroopsPerTurn(1);
    }

    
}
