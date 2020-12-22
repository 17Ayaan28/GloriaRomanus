// package test;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.ArrayList;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;

// import unsw.gloriaromanus.*;
// import unsw.gloriaromanus.TurnSystem.TurnManager;
// import unsw.gloriaromanus.buildings.*;

// public class BuildingTest {
    
//     /**
//      * Test that the player can construct a building.
//      */
//     @Test
//     public void buildingTest() {
        
//         // Create a new turn manager and province, and subscribe the latter to the former.
//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province ("Sicilia", false, tm);
//         tm.subscribe(sicilia);

//         // Ensure that the province has no buildings.
//         assertTrue(sicilia.getNumBuildings() == 0);

//         // Construct a farm.
//         sicilia.createBuilding("farm");
//         tm.nextTurn();

//         // Ensure that the province has a farm, and that it only has one building.
//         assertTrue(sicilia.getFarm() != null);
//         assertTrue(sicilia.getNumBuildings() == 1);

//     }

//     /**
//      * Test that the player can only build one building at a time in a particular province.
//      */
//     @Test
//     public void onlyOneBuildingSlotTest() {
        
//         // Create a new turn manager and province, and subscribe the latter to the former.
//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province ("Sicilia", false, tm);
//         tm.subscribe(sicilia);

//         // Start construction of a farm.
//         sicilia.createBuilding("farm");
//         assertTrue(sicilia.getCurrBuilding() instanceof Farm);

//         // Try to construct a port. This should fail, and the current construction should remain
//         // a farm.
//         sicilia.createBuilding("port");
//         assertTrue(sicilia.getCurrBuilding() instanceof Farm);

//         // Increment the turn. This should conclude the building of the farm, and
//         // allow for the port to be constructed.
//         tm.nextTurn();
//         assertTrue(sicilia.getCurrBuilding() == null);

//         sicilia.createBuilding("port");
//         assertTrue(sicilia.getCurrBuilding() instanceof Port);

//     }

//     /**
//      * Test that the player can build buildings in the same turn in different provinces.
//      */
//     @Test
//     public void buildingBetweenProvincesTest() {
        
//         // Create a new turn manager.
//         TurnManager tm = new TurnManager();

//         // Create provinces and a list of provinces.
//         Province sicilia = new Province ("Sicilia", false, tm);
//         Province syria = new Province ("Syria", false, tm);
//         ArrayList<Province> romanProvinces = new ArrayList<Province>();
//         romanProvinces.add(sicilia); romanProvinces.add(syria);

//         // Create factions and assign them their respective provinces.
//         Faction rome = new Faction("Rome", romanProvinces);

//         // Create player and assign them their provinces.
//         Player p1 = new Player("Player 1", rome);

//         // Start construction of a farm in Syria.
//         syria.createBuilding("farm");
//         assertTrue(syria.getCurrBuilding() instanceof Farm);

//         // Try to construct a farm in Sicilia.
//         sicilia.createBuilding("farm");
//         assertTrue(sicilia.getCurrBuilding() instanceof Farm);

//     }

//     /**
//      * Test that the player can upgrade a building.
//      */
//     @Test
//     public void upgradeTest() {
        
//         // Create a new turn manager and province, and subscribe the latter to the former.
//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province ("Sicilia", false, tm);
//         tm.subscribe(sicilia);

//         // Construct a farm.
//         sicilia.createBuilding("farm");
//         tm.nextTurn();

//         // Ensure that the province has a farm, and that it is level 1 initially.
//         assertTrue(sicilia.getFarm() != null);
//         assertTrue(sicilia.getFarmLevel() == 1);

//         // Upgrade the farm and ensure that its level increases.
//         sicilia.createBuilding("farm");
//         assertTrue(sicilia.getFarmLevel() == 2);
        
//     }

//     /**
//      * Test that the ports can be upgraded and their wealth bonus increases.
//      */
//     @Test
//     public void portTest() {
        
//         // Create a new turn manager and province, and subscribe the latter to the former.
//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province ("Sicilia", false, tm);
//         tm.subscribe(sicilia);

//         // Also create a province that is landlocked, since they can't have ports.
//         Province noricum = new Province ("Noricum", true, tm);
//         tm.subscribe(noricum);

//         // Try to construct a port in both.
//         sicilia.createBuilding("port");
//         noricum.createBuilding("port");
//         tm.nextTurn();

//         // Ensure that the non-landlocked province has a port, and that it is level 1 initially.
//         assertTrue(sicilia.getPort() != null);
//         assertTrue(sicilia.getPortLevel() == 1);

//         // Ensure that the landlocked province does not have a port.
//         assertTrue(noricum.getPort() == null);
    
//         // Get the initial Port Wealth Bonus.
//         int initialPortWB = sicilia.getPort().getWealthBonus();

//         // Upgrade the port and ensure that its level increases.
//         sicilia.createBuilding("port");
//         assertTrue(sicilia.getPortLevel() == 2);
//         assertTrue(sicilia.getPort().getWealthBonus() > initialPortWB);
        
//     }

//     /**
//      * Test that the markets can be upgraded and cost reduction bonus increases.
//      */
//     @Test
//     public void marketTest() {
        
//         // Create a new turn manager and province, and subscribe the latter to the former.
//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province ("Sicilia", false, tm);
//         tm.subscribe(sicilia);

//         // Construct a market.
//         sicilia.createBuilding("market");
//         tm.nextTurn();

//         // Ensure that the province has a market, and that it is level 1 initially.
//         assertTrue(sicilia.getMarket() != null);
//         assertTrue(sicilia.getMarketLevel() == 1);

//         // Get the initial market bonus.
//         double initialMarketBonus = sicilia.getMarket().getCostReduction();

//         // Upgrade the market and ensure that its level increases.
//         sicilia.createBuilding("market");
//         assertTrue(sicilia.getMarketLevel() == 2);
//         assertTrue(sicilia.getMarket().getCostReduction() > initialMarketBonus);
        
//     }

//     /**
//      * Test that the mines can be upgraded and soldier cost reduction bonus increases.
//      */
//     @Test
//     public void mineTest() {
        
//         // Create a new turn manager and province, and subscribe the latter to the former.
//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province ("Sicilia", false, tm);
//         tm.subscribe(sicilia);

//         // Construct a mine.
//         sicilia.createBuilding("mine");
//         tm.nextTurn();

//         // Ensure that the province has a mine, and that it is level 1 initially.
//         assertTrue(sicilia.getMine() != null);
//         assertTrue(sicilia.getMineLevel() == 1);

//         // Get the initial mine bonus.
//         double initialMineBonus = sicilia.getMine().getSoldierDiscount();

//         // Upgrade the mine and ensure that its level increases and, that it is not yet max level.
//         sicilia.createBuilding("mine");
//         assertTrue(sicilia.getMineLevel() == 2);
//         assertTrue(sicilia.getMine().getSoldierDiscount() > initialMineBonus);
//         assertFalse(sicilia.getMine().isMaxLevel());

//         // Upgrade the mine to level 5 (MAX) and see if the bonus gets activated.
//         sicilia.createBuilding("mine");
//         sicilia.createBuilding("mine");
//         sicilia.createBuilding("mine");
//         assertTrue(sicilia.getMine().isMaxLevel());

//         // Also ensure it cant be upgraded any further.
//         sicilia.createBuilding("mine");
//         assertTrue(sicilia.getMineLevel() == 5);
//         assertTrue(sicilia.getMine().isMaxLevel());

//     }

//     /**
//      * Test that the player can create and upgrade a troop building.
//      */
//     @Test
//     public void troopBuildingTest() {
        
//         // Create a new turn manager and province, and subscribe the latter to the former.
//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province ("Sicilia", false, tm);
//         tm.subscribe(sicilia);

//         // Construct a troop building.
//         sicilia.createBuilding("troop");
//         tm.nextTurn();

//         // Ensure that the province has a troop building, and that it is level 1 initially.
//         assertTrue(sicilia.getTroopBuilding() != null);
//         assertTrue(sicilia.getTroopBuildingLevel() == 1);

//         // Upgrade the troop building and ensure that its level increases.
//         sicilia.createBuilding("troop");
//         assertTrue(sicilia.getTroopBuildingLevel() == 2);
        
//     }

//     /**
//      * Test that the player can create and upgrade a smith, and also purchase upgrades for their troops.
//      */
//     @Test
//     public void smithTest() {
        
//         // Create a new turn manager and province, and subscribe the latter to the former.
//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province ("Sicilia", false, tm);
//         tm.subscribe(sicilia);

//         // Construct a smith.
//         sicilia.createBuilding("smith");
//         tm.nextTurn();

//         Smith smith = sicilia.getSmith();
//         // Ensure that the province has a smith, and that it is level 1 initially.
//         assertTrue(smith != null);
//         assertTrue(smith.getLevel() == 1);

//         // Upgrade the smith and ensure that its level increases.
//         sicilia.createBuilding("smith");
//         assertTrue(smith.getLevel() == 2);
        
//         // Purchase each individual upgrade and ensure that it becomes active.
//         smith.upgradeHelmets();
//         smith.upgradeArmour();
//         smith.upgradeWeapons();
//         smith.upgradeFireArrows();

//         assertTrue(smith.isHelmetsUpgraded());
//         assertTrue(smith.isArmourUpgraded());
//         assertTrue(smith.isWeaponUpgraded());
//         assertTrue(smith.hasFireArrows());

//     }


// }
