import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.TreeMap;

/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class BattleShipTest {
    
    private Battleship game;
    
    @BeforeEach
    public void setup() {
        game = new Battleship();
    }
    
    @Test
    public void testCreateShips() {
        TreeMap<String, Ship> ships = game.createShips();
        assertTrue(ships.containsKey("battleship"));
        assertTrue(ships.containsKey("cruiser"));
        assertTrue(ships.containsKey("destroyer"));
        assertTrue(ships.containsKey("submarine"));
        assertTrue(ships.containsKey("carrier"));
    }
    
    @Test 
    public void testPlaceShips1() {
        game.createShips();
        Tile[][] board = game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        int[][] battleCoords = {{1, 0}, {1,1}, {1,2}, {1, 3}};
        int[][] subCoords = {{2, 0}, {2,1}, {2,2}};
        int[][] cruiserCoords = {{3, 0}, {3,1}, {3,2}};
        int[][] destroyerCoords = {{4, 0}, {4,1}};
        
        game.placeShips(1, carrierCoords, battleCoords, 
                cruiserCoords, subCoords, destroyerCoords);
        
        for (int a = 0; a < 5; a++) {
            Tile t = board[carrierCoords[a][0]][carrierCoords[a][1]];
            t.getShip();
            assertFalse(t.getEmpty());
        }
        
        for (int b = 0; b < 4; b++) {
            Tile t2 = board[battleCoords[b][0]][battleCoords[b][1]];
            t2.getShip();
            assertFalse(t2.getEmpty());
        }
        
        for (int c = 0; c < 3; c++) {
            Tile t3 = board[subCoords[c][0]][subCoords[c][1]];
            t3.getShip();
            assertFalse(t3.getEmpty());
        }
        
        for (int d = 0; d < d; d++) {
            Tile t4 = board[cruiserCoords[d][0]][cruiserCoords[d][1]];
            t4.getShip();
            assertFalse(t4.getEmpty());
        }
        
        for (int e = 0; e < 2; e++) {
            Tile t5 = board[destroyerCoords[e][0]][destroyerCoords[e][1]];
            t5.getShip();
            assertFalse(t5.getEmpty());
        }
        
    }
    
    public void testFireOnShip() {
        game.createShips();
        Tile[][] board1 = game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        int[][] battleCoords = {{1, 0}, {1,1}, {1,2}, {1, 3}};
        int[][] subCoords = {{2, 0}, {2,1}, {2,2}};
        int[][] cruiserCoords = {{3, 0}, {3,1}, {3,2}};
        int[][] destroyerCoords = {{4, 0}, {4,1}};
        
        game.placeShips(1, carrierCoords, battleCoords, 
                cruiserCoords, subCoords, destroyerCoords);
        
        //User2 firing on user1's board at position (0, 0)
        int[] p = {0, 0};
        game.fire(p, board1);
        
        assertTrue(board1[0][0].getHit());
        
        Ship s = board1[0][0].getShip();
        assertEquals(1, s.getHits());
        
    }
    
    @Test
    public void testFireSinkShip() {
        game.createShips();
        Tile[][] board1 = game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        int[][] battleCoords = {{1, 0}, {1,1}, {1,2}, {1, 3}};
        int[][] subCoords = {{2, 0}, {2,1}, {2,2}};
        int[][] cruiserCoords = {{3, 0}, {3,1}, {3,2}};
        int[][] destroyerCoords = {{4, 0}, {4,1}};
        
        game.placeShips(1, carrierCoords, battleCoords, 
                cruiserCoords, subCoords, destroyerCoords);
        
        int[] p1 = {4, 0};
        int[] p2 = {4, 1};
        game.fire(p1, board1);
        game.fire(p2, board1);
        
        Ship s = board1[4][0].getShip();
        assertEquals(2, s.getHits());
        assertTrue(s.getSunk());
        assertTrue(board1[4][0].getHit());
        assertTrue(board1[4][1].getHit());
    }
    
    @Test
    public void testFireEmptyTile() {
        game.createShips();
        Tile[][] board1 = game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        int[][] battleCoords = {{1, 0}, {1,1}, {1,2}, {1, 3}};
        int[][] subCoords = {{2, 0}, {2,1}, {2,2}};
        int[][] cruiserCoords = {{3, 0}, {3,1}, {3,2}};
        int[][] destroyerCoords = {{4, 0}, {4,1}};
        
        game.placeShips(1, carrierCoords, battleCoords, 
                cruiserCoords, subCoords, destroyerCoords);
        
        int[] p = {9, 9};
        game.fire(p, board1);
        
        assertTrue(board1[9][9].getEmpty());
        assertTrue(board1[9][9].getHit());
        
    }

    
    @Test
    public void testCheckWinBeginningOfGame() {
        assertEquals(0, game.checkWin());
    }
    
    @Test
    public void testCheckWinUser1Wins() {
        game.getUser1Ships();
        Tile[][] board1 = game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        int[][] battleCoords = {{1, 0}, {1,1}, {1,2}, {1, 3}};
        int[][] subCoords = {{2, 0}, {2,1}, {2,2}};
        int[][] cruiserCoords = {{3, 0}, {3,1}, {3,2}};
        int[][] destroyerCoords = {{4, 0}, {4,1}};
        
        game.placeShips(1, carrierCoords, battleCoords, 
                cruiserCoords, subCoords, destroyerCoords);
        
        game.fire(carrierCoords[0], board1);
        game.fire(carrierCoords[1], board1);
        game.fire(carrierCoords[2], board1);
        game.fire(carrierCoords[3], board1);
        game.fire(carrierCoords[4], board1);
        
        game.fire(battleCoords[0], board1);
        game.fire(battleCoords[1], board1);
        game.fire(battleCoords[2], board1);
        game.fire(battleCoords[3], board1);
        
        game.fire(subCoords[0], board1);
        game.fire(subCoords[1], board1);
        game.fire(subCoords[2], board1);
        
        game.fire(cruiserCoords[0], board1);
        game.fire(cruiserCoords[1], board1);
        game.fire(cruiserCoords[2], board1);
        
        game.fire(destroyerCoords[0], board1);
        game.fire(destroyerCoords[1], board1);
        
        assertEquals(1, game.checkWin());
        
    }
    
    @Test
    public void testCheckWinUser1DoesntWin() {
        game.getUser1Ships();
        Tile[][] board1 = game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        int[][] battleCoords = {{1, 0}, {1,1}, {1,2}, {1, 3}};
        int[][] subCoords = {{2, 0}, {2,1}, {2,2}};
        int[][] cruiserCoords = {{3, 0}, {3,1}, {3,2}};
        int[][] destroyerCoords = {{4, 0}, {4,1}};
        
        game.placeShips(1, carrierCoords, battleCoords, 
                cruiserCoords, subCoords, destroyerCoords);
        
        game.fire(carrierCoords[0], board1);
        game.fire(carrierCoords[1], board1);
        game.fire(carrierCoords[2], board1);
        game.fire(carrierCoords[3], board1);
        game.fire(carrierCoords[4], board1);
        
        game.fire(battleCoords[0], board1);
        game.fire(battleCoords[1], board1);
        game.fire(battleCoords[2], board1);
        game.fire(battleCoords[3], board1);
        
        game.fire(subCoords[0], board1);
        game.fire(subCoords[1], board1);
        game.fire(subCoords[2], board1);
        
        game.fire(cruiserCoords[0], board1);
        game.fire(cruiserCoords[1], board1);
        game.fire(cruiserCoords[2], board1);
        
        assertEquals(0, game.checkWin());
        
    }
    
    @Test
    public void testCheckWinUser2Wins() {
        game.getUser2Ships();
        Tile[][] board2 = game.getUser2Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        int[][] battleCoords = {{1, 0}, {1,1}, {1,2}, {1, 3}};
        int[][] subCoords = {{2, 0}, {2,1}, {2,2}};
        int[][] cruiserCoords = {{3, 0}, {3,1}, {3,2}};
        int[][] destroyerCoords = {{4, 0}, {4,1}};
        
        game.placeShips(2, carrierCoords, battleCoords, 
                cruiserCoords, subCoords, destroyerCoords);
        
        game.fire(carrierCoords[0], board2);
        game.fire(carrierCoords[1], board2);
        game.fire(carrierCoords[2], board2);
        game.fire(carrierCoords[3], board2);
        game.fire(carrierCoords[4], board2);
        
        game.fire(battleCoords[0], board2);
        game.fire(battleCoords[1], board2);
        game.fire(battleCoords[2], board2);
        game.fire(battleCoords[3], board2);
        
        game.fire(subCoords[0], board2);
        game.fire(subCoords[1], board2);
        game.fire(subCoords[2], board2);
        
        game.fire(cruiserCoords[0], board2);
        game.fire(cruiserCoords[1], board2);
        game.fire(cruiserCoords[2], board2);
        
        game.fire(destroyerCoords[0], board2);
        game.fire(destroyerCoords[1], board2);
        
        assertEquals(2, game.checkWin());
        
    }
    
    @Test
    public void testCheckSelectedCoordsValid() {
        game.getUser1Board();
        
        int[][] destroyerCoords = {{4, 0}, {4,1}};
        
        game.select(destroyerCoords[0]);
        game.select(destroyerCoords[1]);
        
        assertTrue(game.checkSelectedCoords(2));
    }
    
    @Test
    public void testCheckSelectedCoordsNotIncreasing() {
        game.getUser1Board();
        
        int[][] destroyerCoords = {{4, 4}, {4,1}};
        
        game.select(destroyerCoords[0]);
        game.select(destroyerCoords[1]);
        
        assertFalse(game.checkSelectedCoords(2));
    }
    
    @Test
    public void testCheckSelectedCoordsWrongSize() {
        game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        
        game.select(carrierCoords[0]);
        game.select(carrierCoords[1]);
        game.select(carrierCoords[2]);
        game.select(carrierCoords[3]);
        
        assertFalse(game.checkSelectedCoords(5));
    }
    
    @Test
    public void testCheckSelectedCoordsDiagonal()  {
        game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {1,1}, {2,2}, {3, 3}, {4, 4}};
        
        game.select(carrierCoords[0]);
        game.select(carrierCoords[1]);
        game.select(carrierCoords[2]);
        game.select(carrierCoords[3]);
        game.select(carrierCoords[4]);
        
        assertFalse(game.checkSelectedCoords(5));
    }
    
    @Test
    public void testCheckSelectedCoordsValidCarrierCoords()  {
        game.getUser1Board();
        
        int[][] carrierCoords = {{0, 0}, {0,1}, {0,2}, {0, 3}, {0, 4}};
        
        game.select(carrierCoords[0]);
        game.select(carrierCoords[1]);
        game.select(carrierCoords[2]);
        game.select(carrierCoords[3]);
        game.select(carrierCoords[4]);
        
        assertTrue(game.checkSelectedCoords(5));
    }
    
    //From GameBoard class
    @Test 
    public void testTranslateCoords() {
        int[] expected = {0, 0};
        
        GameBoard g = new GameBoard(null, null);
        int[] actual = g.translateCoords(new Point(20, 30));
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }
    
    
}
