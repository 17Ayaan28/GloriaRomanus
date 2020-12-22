package unsw.gloriaromanus.buildings;

import java.io.Serializable;

import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.TurnSystem.TurnListener;
import unsw.gloriaromanus.TurnSystem.TurnManager;

// TODO ADD BONUSES TO TROOPS RECRUITED IN FACTION

public class Smith implements Building, TurnListener, Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int level;
    private int baseWealth;

    private int cost = 100;
    private int turnsToComplete = 2;

    private Province province;
    
    private boolean helmetsUpgraded = false;
    private boolean armourUpgraded = false;
    private boolean weaponUpgraded = false;
    private boolean fireArrows = false;


    public Smith(Province p, TurnManager tm) {
        tm.subscribe(this);
        this.setLevel(1);
        this.setBaseWealth(25);
        this.setProvince(p);
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

    public boolean isHelmetsUpgraded() {
        return helmetsUpgraded;
    }

    public void upgradeHelmets() {
        this.helmetsUpgraded = true;
    }

    public boolean isArmourUpgraded() {
        return armourUpgraded;
    }

    public void upgradeArmour() {
        this.armourUpgraded = true;
    }

    public boolean isWeaponUpgraded() {
        return weaponUpgraded;
    }

    public void upgradeWeapons() {
        this.weaponUpgraded = true;
    }

    public boolean hasFireArrows() {
        return fireArrows;
    }

    public void upgradeFireArrows() {
        this.fireArrows = true;
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
    public void update() {

        if (turnsToComplete > 0) {
            turnsToComplete -= 1;
        }

    }

    @Override
    public int getTurnsLeft() {
        return turnsToComplete;
    }

    public Province getProvince() {
        return province;
    }


}
