// package test;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import java.io.IOException;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.ValueSource;

// import unsw.gloriaromanus.*;
// import unsw.gloriaromanus.TurnSystem.TurnManager;

// public class BattleResolverTest {
//     @Test
//     public void BattleResolverTest1() {

//         TurnManager tm = new TurnManager();

//         Province sicilia = new Province("Sicilia", false, tm);
//         Province III = new Province("III", false, tm);

//         Unit u1 = new Unit(1000, 5, 4, 3, 2, 1, 3, 3, sicilia, 1);
//         sicilia.addUnit_list(u1);

//         Unit u2 = new Unit(6, 5, 4, 3, 2, 1, 3, 3, sicilia, 1);
//         III.addUnit_list(u2);

//         try {
//             sicilia.BattleResolver(sicilia, III);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//         // Odds are Sicilia will win
//     }

//     @Test
//     public void BattleResolverTest2() {

//         TurnManager tm = new TurnManager();

//         Province sicilia = new Province("Sicilia", false, tm);
//         Province III = new Province("III", false, tm);

//         Unit u1 = new Unit(6, 5, 4, 3, 2, 1, 3, 3, sicilia, 1);
//         sicilia.addUnit_list(u1);

//         Unit u2 = new Unit(1000, 5, 4, 3, 2, 1, 3, 3, sicilia, 1);
//         III.addUnit_list(u2);

//         try {
//             sicilia.BattleResolver(sicilia, III);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//         // Odds are III will sustain
//     }
// }    