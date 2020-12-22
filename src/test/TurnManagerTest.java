// package test;

// import static org.junit.jupiter.api.Assertions.assertTrue;

// import org.junit.jupiter.api.Test;

// import unsw.gloriaromanus.*;
// import unsw.gloriaromanus.TurnSystem.TurnManager;

// public class TurnManagerTest{

//     /**
//      * Test that subscription and unsubscription from a TurnManager works.
//      */
//     @Test
//     public void subUnsubTest(){

//         // Create a new province (turn listener) and link it with a new turn manager.
//         TurnManager tm = new TurnManager();
//         Province testProvince = new Province("Test", false, tm);
        
//         // Obviously we should only have 1 listener right now.
//         assertTrue(tm.getListeners().size() == 1);

//         // Unsubscribe this test province from the turn manager, then ensure we have 0 listeners.
//         tm.unsubscribe(testProvince);
//         assertTrue(tm.getListeners().size() == 0);

//     }

// }

