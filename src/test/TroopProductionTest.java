// package test;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import java.io.IOException;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;

// import unsw.gloriaromanus.*;
// import unsw.gloriaromanus.TurnSystem.TurnManager;
// import unsw.gloriaromanus.buildings.TroopBuilding;

// public class TroopProductionTest {

//     @Test
//     public void TroopProductionTest1() {

//         // Shows Troop production 
//         TurnManager tm = new TurnManager();
 
//         Province sicilia = new Province("Sicilia", false, tm);
//         sicilia.setTroopsPerTurn(20);

//         TroopBuilding tb = new TroopBuilding(sicilia);
//         sicilia.setTroopBuilding(tb);
//         tm.subscribe(sicilia.getTroopBuilding());

//         tm.nextTurn();
//         tm.nextTurn();
        
//         // Command to create unit
//         try {
//             sicilia.getTroopBuilding().add_units("infantry", 10);
//         } catch (IOException e) {

//             e.printStackTrace();
//         }
        
//         //units not added till after appropriate number of turns
//         assertEquals(sicilia.getUnit_list().size(), 0);

//         tm.nextTurn();
//         tm.nextTurn();

//         for(Unit u: sicilia.getUnit_list()){
            
//             assertEquals(u.getNumTroops(), 10);
//         }
//     }

//     @Test

//     public void TroopProductionTest2() {

//         TurnManager tm = new TurnManager();
 
//         Province sicilia = new Province("Sicilia", false, tm);
//         sicilia.setTroopsPerTurn(20);

//         TroopBuilding tb = new TroopBuilding(sicilia);
//         sicilia.setTroopBuilding(tb);
//         sicilia.getFarm().setLevel(-2);
//         tm.subscribe(sicilia.getTroopBuilding());

//         tm.nextTurn();
//         tm.nextTurn();
        
//         // Command to create unit
//         try {
//             sicilia.getTroopBuilding().add_units("infantry", 10);
//         } catch (IOException e) {

//             e.printStackTrace();
//         }
        
//         //units not added till after appropriate number of turns
//         assertEquals(sicilia.getUnit_list().size(), 0);

//         tm.nextTurn();
//         tm.nextTurn();

//         // Units not added beacuse farm level not enough
//         assertEquals(sicilia.getUnit_list().size(), 0);
//     }

//     // Troops dont move
//     public void TroopProductionTest3() {

//         TurnManager tm = new TurnManager();
 
//         Province sicilia = new Province("Sicilia", false, tm);
//         sicilia.setTroopsPerTurn(5);

//         TroopBuilding tb = new TroopBuilding(sicilia);
//         sicilia.setTroopBuilding(tb);
//         sicilia.getFarm().setLevel(-2);
//         tm.subscribe(sicilia.getTroopBuilding());

//         tm.nextTurn();
//         tm.nextTurn();
        
//         // Command to create unit
//         try {
//             sicilia.getTroopBuilding().add_units("infantry", 10);
//         } catch (IOException e) {

//             e.printStackTrace();
//         }
        
//         //units not added till after appropriate number of turns
//         assertEquals(sicilia.getUnit_list().size(), 0);

//         tm.nextTurn();
//         tm.nextTurn();

//         // Units not added beacuse troops max limit reached
//         assertEquals(sicilia.getUnit_list().size(), 0);
//     }
// }