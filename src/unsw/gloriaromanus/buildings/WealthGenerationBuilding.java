package unsw.gloriaromanus.buildings;

import java.io.Serializable;

import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.TurnSystem.TurnListener;
import unsw.gloriaromanus.TurnSystem.TurnManager;

public abstract class WealthGenerationBuilding implements Building, TurnListener, Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int level;
    private int baseWealth;
    private int WealthPerTurn;

    private int cost = 100;
    private int turnsToComplete = 3;

    private Province province;

    public WealthGenerationBuilding(Province p, TurnManager tm) {
        tm.subscribe(this);
        this.province = p;
        this.level = 1;
    }

    // GETTERS AND SETTERS
    public int getLevel() {
        return level;
    }

    public void addLevel(int i) {
        if (level < 5) {
            this.level += 1;
        }
    }

    public int getBaseWealth() {
        return baseWealth * level;
    }

    public void setBaseWealth(int baseWealth) {
        this.baseWealth = baseWealth;
    }

    public int getWealthPerTurn() {
        return WealthPerTurn;
    }

    public void setWealthPerTurn(int wealthPerTurn) {
        WealthPerTurn = wealthPerTurn;
    }

    public Province getProvince() {
        return province;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCost() {
        return cost*level;
    }

    public void update() {
        if (turnsToComplete > 0) {
            turnsToComplete -= 1;
            System.out.println("reducing turns, turns left = " + turnsToComplete);
        }
    }

    @Override
    public boolean isReady() {
        return (turnsToComplete==0);
    }

    public int getTurnsLeft() {
        return turnsToComplete;
    }

}
