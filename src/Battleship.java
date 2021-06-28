import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

// Model for battleship game

public class Battleship {
    private Tile[][] board1;
    private Tile[][] board2;
    private TreeMap<String, Ship> user1Ships;
    private TreeMap<String, Ship> user2Ships;
    private boolean playingGame;
    private int turn;
    private String user1Name;
    private String user2Name;
    private ArrayList<int[]> selected;
    private ArrayList<int[]> shipPositions;
    
    public Battleship() {
        board1 = newBoard();
        board2 = newBoard();
        playingGame = false;
        turn = 1;
        
        user1Ships = createShips();
        user2Ships = createShips();
        selected = new ArrayList<int[]>();
        shipPositions = new ArrayList<int[]>();
    }
    
    private Tile[][] newBoard() {
        Tile[][] b = new Tile[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                b[i][j] = new Tile(null, i, j);
            }
        }
        
        return b;
    }
    
    public TreeMap<String, Ship> createShips() {
        TreeMap<String, Ship> shipMap = new TreeMap<String, Ship>();
        shipMap.put("carrier", new Ship(5));
        shipMap.put("battleship", new Ship(4));
        shipMap.put("cruiser", new Ship(3));
        shipMap.put("submarine", new Ship(3));
        shipMap.put("destroyer", new Ship(2));
        
        return shipMap;
    }
    
    public void select(int[] p) {
        
        if (!playingGame) {
            int[] elem = containsElement(p);
            if (elem != null) {
                selected.remove(elem);
            } else {
                selected.add(p);
            }
        } 
        
    }
    
    private int[] containsElement(int[] p) {
        for (int i = 0; i < selected.size(); i++) {
            int x = selected.get(i)[0];
            int y = selected.get(i)[1];
            if (x == p[0] && y == p[1]) {
                return selected.get(i);
            } 
        }
        return null;
    }
    
    public boolean checkSelectedCoords(int size) {
        if (size == selected.size()) {
            int[] first = selected.get(0);
            int x = first[0];
            int y = first[1];
            boolean horizontal = (y == selected.get(1)[1]);
            int[] changingCoordVals = new int[size];
            
            for (int i = 0; i < size; i++) {
                int[] next = selected.get(i);
                if (horizontal) {
                    if (y != next[1]) {
                        return false;
                    }
                    changingCoordVals[i] = next[0];
                } else {
                    if (x != next[0]) {
                        return false;
                    }
                    changingCoordVals[i] = next[1];
                }
            }
            
            Arrays.sort(changingCoordVals);
            
            for (int j = 0; j < size - 1; j++) {
                if (changingCoordVals[j] + 1 != changingCoordVals[j + 1]) {
                    return false;
                }
            }
          
        } else {
            return false;
        }
        
        //Checking that ship isn't being placed where another one already is or 
        //within one space of another ship
        if (shipPositions != null) {
            for (int k = 0; k < selected.size(); k++) {
                int[] p = selected.get(k);
                if (shipPositions.contains(p)) {
                    return false;
                } 
                
                for (int l = 0; l < shipPositions.size(); l++) {
                    int[] m = shipPositions.get(l);
                    
                    boolean withinOneX = (Math.abs(p[0] - m[0]) <= 1);
                    boolean withinOneY = (Math.abs(p[1] - m[1]) <= 1);
                    
                    if (withinOneX && withinOneY) {
                        return false;
                    }
                    
                }
                
            }
        }
        
        shipPositions.addAll(selected);        
        return true;

    }
    
    public void setTurn(int n) {
        if (n == 1) {
            turn = 1;
        } else {
            turn = 2;
        }
    }
    
    //Make sure to pass in the right board that corresponds with the ships
    public void placeShips(int n, int[][] carrierPos, int[][] battlePos, int[][] cruiserPos, 
            int[][] subPos, int[][] destroyerPos) {

        Tile[][] board = board1;
        TreeMap<String, Ship> ships = user1Ships;
        if (n == 2) {
            board = board2;
            ships = user2Ships;
        } 
        
        Ship carrier = ships.get("carrier");
        Ship battle = ships.get("battleship");
        Ship cruiser = ships.get("cruiser");
        Ship sub = ships.get("submarine");
        Ship destroyer = ships.get("destroyer");
        
        //Putting ship objects into corresponding tiles
        placeHelper(5, carrierPos, carrier, board);
        placeHelper(4, battlePos, battle, board);
        placeHelper(3, cruiserPos, cruiser, board);
        placeHelper(3, subPos, sub, board);
        placeHelper(2, destroyerPos, destroyer, board);
        
        shipPositions.clear();
        
    }
    
    private void placeHelper(int size, int[][] coords, Ship ship, Tile[][] board) {
        for (int i = 0; i < size; i++) {
            int x = coords[i][0];
            int y = coords[i][1];
            Tile t = board[x][y];
            t.placeShip(ship);
            ship.placeShip(coords);
        }
    }
    
    public String fire(int[] p, Tile[][] board) {
        int x = p[0];
        int y = p[1];
        Tile t = board[x][y];

        boolean isEmpty = t.getEmpty();
        String outcome = new String();
        String user = new String();
        
        
        if (t.getHit()) {
            return "invalid";
        }
        
        t.hitTile();

        if (!isEmpty) {
            Ship s = t.getShip();
            s.hitShip();
            outcome = "hit";
        } else {
            outcome = "missed";
        }
        
        if (board == board1) {
            user = user1Name;
        } else {
            user = user2Name;
        }
        
        String[] coords = translateCoords(x, y);
        return (user + " fired on tile [" + coords[1] + ", " + coords[0] + "] and " 
                + outcome + ".");
        
    }
    
    private String[] translateCoords(int x, int y) {
        String[] coords = new String[2];
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        coords[0] = Integer.toString(x + 1);
        coords[1] = letters[y];
        
        return coords;

        
    }
    
    //Returns 0 if no winner, 1 if user1 wins, 2 if user2 wins
    public int checkWin() {
        boolean user1Wins = true;
        for (Entry<String, Ship> e : user2Ships.entrySet()) {
            Ship s = e.getValue();
            if (!s.getSunk()) {
                user1Wins = false;
                break;
            }
        }
        
        boolean user2Wins = true;
        for (Entry<String, Ship> e : user1Ships.entrySet()) {
            Ship s2 = e.getValue();
            if (!s2.getSunk()) {
                user2Wins = false;
                break;
            }
        }
        
        if (user1Wins) {
            playingGame = false;
            return 1;
        } else if (user2Wins) {
            playingGame = false;
            return 2;
        } else {
            return 0;
        }
    }
    
    public void nextTurn() {
        if (turn == 1) {
            turn = 2;
        } else {
            turn = 1;
        }
    }
    
    public void reset() {
        board1 = newBoard();
        board2 = newBoard();
        playingGame = false;
        
        user1Ships = createShips();
        user2Ships = createShips();
       
    }
    
    public void setUser1Name(String n) {
        user1Name = n;
    }
    
    public void setUser2Name(String n) {
        user2Name = n;
    }
    
    public void deselect() {
        selected.clear();
    }
    
    public void startGame() {
        playingGame = true;
    }
    
    public void resetPlayingGame() {
        playingGame = false;
    }
    
    
    //Accessors
    public TreeMap<String, Ship> getUser1Ships() {
        return user1Ships;
    }
    
    public TreeMap<String, Ship> getUser2Ships() {
        return user2Ships;
    }
    
    public Tile[][] getUser1Board() {
        return board1;
    }
    
    public Tile[][] getUser2Board() {
        return board2;
    }
    
    public boolean getPlayingGame() {
        return playingGame;
    }
    
    public String getTurn() {
        if (turn == 1) {
            return user1Name;
        } else {
            return user2Name;
        }
    }
    
    public String getNotTurn() {
        if (turn == 2) {
            return user1Name;
        } else {
            return user2Name;
        }
    }
    
    public Tile[][] getCurrentTiles() {
        if (turn == 1) {
            return board1;
        }
        
        return board2;
    }
    
    public TreeMap<String, Ship> getCurrentShips() {
        if (turn == 1) {
            return user1Ships;
        } else {
            return user2Ships;
        }
    }
    
    public Tile[][] getEnemyTiles() {
        if (turn == 1) {
            return board2;
        } else {
            return board1;
        }
    }
    
    public String getUser1Name() {
        return user1Name;
    }
    
    public String getUser2Name() {
        return user2Name;
    }
    
    public int[][] getSelected() {
        Iterator<int[]> iter = selected.iterator();
        int[][] s = new int[selected.size()][2];
        int i = 0; 
        
        while (iter.hasNext()) {
            s[i] = iter.next();
            i += 1;
        }
        
        return s;
    }
    

    
}
