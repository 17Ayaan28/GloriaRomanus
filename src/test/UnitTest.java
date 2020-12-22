// package test;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import java.io.IOException;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;

// import unsw.gloriaromanus.*;
// import unsw.gloriaromanus.TurnSystem.TurnManager;

// public class UnitTest {
//     @Test
//     public void blahTest() {
//         assertEquals("a", "a");
//     }

//     @Test
//     public void unitTest2() {

//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province("Sicilia", false, tm);
//         Unit u = new Unit(6, 5, 4, 3, 2, 1, 3, 3, sicilia, 1);
//         assertEquals(u.getNumTroops(), 6);

//         assertEquals(u.getRange(), 5);
//     }

//     @Test

//     public void unitTest3() {

//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province("Sicilia", false, tm);
//         Province III = new Province("III", false, tm);

//         Unit u = new Unit(6, 5, 4, 3, 2, 1, 3, 3, sicilia, 1);

//         try {
//             u.move_unit(u, sicilia, III);
//         } catch (IOException e) {

//             e.printStackTrace();
//         }
//         // Changes
//         assertEquals(u.getProvince().getName(), "III");
//     }

//     // Troops dont move
//     public void unitTest4() {

//         TurnManager tm = new TurnManager();
//         Province sicilia = new Province("Sicilia", false, tm);
//         Province Britannia = new Province("Britannia", false, tm);

//         Unit u = new Unit(6, 5, 4, 3, 2, 1, 3, 3, sicilia, 1);

//         try {
//             u.move_unit(u, sicilia, Britannia);
//         } catch (IOException e) {
            
//             e.printStackTrace();
//         }
//         //Does not change
//         assertEquals(u.getProvince().getName(), "Sicilia");
//     }
// }

