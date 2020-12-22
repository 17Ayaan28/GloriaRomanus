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

// public class LocalMultiplayerTest {
    
//     /**
//      * Test that the player in control is changed when the turn
//      * changes.
//      */
//     @Test
//     public void changeTurnTest() {
        
//         TurnManager tm = new TurnManager();

//         // Create provinces and list of provinces.
//         Province sicilia = new Province ("Sicilia", false, tm);
//         Province syria = new Province ("Syria", false, tm);
//         ArrayList<Province> romanProvinces = new ArrayList<Province>();
//         romanProvinces.add(sicilia); romanProvinces.add(syria);

//         Province asia = new Province ("Asia", false, tm);
//         Province britannia = new Province ("Britannia", false, tm);
//         ArrayList<Province> gaulianProvinces = new ArrayList<Province>();
//         gaulianProvinces.add(asia); gaulianProvinces.add(britannia);

//         // Create factions and assign them their respective provinces.
//         Faction rome = new Faction("Rome", romanProvinces);  
//         Faction gaul = new Faction("Gaul", gaulianProvinces);

//         // Create players and assign them their provinces.
//         Player p1 = new Player("Player 1", rome);
//         Player p2 = new Player("Player 2", gaul);

//         // Create game logic system and add it to the 
//         GloriaRomanusGameLogic gl = new GloriaRomanusGameLogic(p1, p2);
//         tm.subscribe(gl);

//         // Assert that the current game is not singleplayer, and is infact local multiplayer.
//         assertFalse(gl.isSingleplayer());

//         // Assert that the current player is player one.
//         assertEquals(gl.getCurrentPlayer(), gl.getPlayer1());

//         // Increment the turn and ensure it is turn 2.
//         tm.nextTurn();
//         assertTrue(tm.getCurrTurn() == 2);

//         // Assert that the current player is player two.
//         assertEquals(gl.getCurrentPlayer(), gl.getPlayer2());

//         // Increment the turn.
//         tm.nextTurn();

//         // Assert that the current player is player one.
//         assertEquals(gl.getCurrentPlayer(), gl.getPlayer1());

//     }


// }
