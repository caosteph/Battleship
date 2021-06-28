import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.*;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated.  Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  We STRONGLY 
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:  
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with 
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Battleship battleship; // model for the game
    private BattleShipViewer bsViewer; //model for showing user's hits
    private JLabel status; // current status text
    private TreeMap<String, Ship> ships;
    private StatusPanel spanel;
    private boolean drawBoard;
    
    private boolean carrierPlaced;
    private boolean bsPlaced;
    private boolean cruiserPlaced;
    private boolean subPlaced;
    private boolean desPlaced;
    private boolean user1Placed;
    private boolean user2Placed;
    
    private int[][] carrierCoords;
    private int[][] bsCoords;
    private int[][] cruiserCoords;
    private int[][] subCoords;
    private int[][] desCoords;
    private boolean gameover;
    
    
    // Game constants
    public static final int BOARD_WIDTH = 860;
    public static final int BOARD_HEIGHT = 420;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit, StatusPanel p) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        status = statusInit;
        spanel = p;
        drawBoard = true;

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        
        battleship = new Battleship();
        bsViewer = new BattleShipViewer();
        battleship.getCurrentTiles();
        ships = battleship.getCurrentShips();
        setPlacedBools(false);
        user1Placed = false;
        user2Placed = false;
        gameover = false;
        

        /*
         * Listens for mouseclicks.  Updates the model, then updates the game board
         * based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int[] pt = translateCoords(p);
                
                if (pt[0] < 10 && pt[1] < 10 && !battleship.getPlayingGame()) {
                    battleship.select(pt);
                    
                } else if (pt[0] < 21 && pt[0] > 10 && pt[1] < 10 && battleship.getPlayingGame()) {
                    bsViewer.select(pt);
                }
                
                repaint(); // repaints the game board
            }
        });
    }
    
    /**
     * Translates point coordinates to tile coordinates.
     * @param p is the point where the user clicked on the window
     * @return ordered pair that corresponds to tile on the board with [0,0] at 
     * the upper left corner.
     */
    public int[] translateCoords(Point p) {
        double x = p.getX();
        double y = p.getY();
        
        int boardX = (int) ((x - 20) / 40);
        int boardY = (int) (y / 40);
  
        int[] r = {boardX, boardY};
        return r;
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        battleship.reset();
        battleship.deselect();
        battleship.resetPlayingGame();
        setPlacedBools(false);
        user1Placed = false;
        user2Placed = false;
        gameover = false;
        resetCoords();
        updateStatus();
        spanel.reset();
        battleship.setTurn(1);
        repaint();

        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    public void updateStatus() {
        
        status.setText("It is " + battleship.getTurn() + "'s turn.");
        
        int winner = battleship.checkWin();
        
        if (winner == 1) {
            status.setText(battleship.getUser1Name() + " wins!!");
        } else if (winner == 2) {
            status.setText(battleship.getUser2Name() + " wins!!");
        } 
        
    }
    
    /**
     * Checks that the points selected are valid for the ship being placed
     * and once the user has picked valid tiles for all ships, placing them all at once
     */
    public void placeShip() {        
        if (!carrierPlaced) {
            carrierPlaced = battleship.checkSelectedCoords(5);
            if (carrierPlaced) {
                carrierCoords = battleship.getSelected();
                status.setText("Carrier ship successfully placed! "
                        + "Please pick 4 spots for your battleship.");
            } else {
                status.setText("Invalid tiles for carrier ship, please try again.");
            }
        } else if (!bsPlaced) {
            bsPlaced = battleship.checkSelectedCoords(4);

            if (bsPlaced) {
                bsCoords = battleship.getSelected();
                status.setText("Battleship successfully placed! "
                        + "Please pick 3 spots for your cruiser ship.");

            } else {
                status.setText("Invalid tiles for battleship, please try again.");
            }
        } else if (!cruiserPlaced) {
            cruiserPlaced = battleship.checkSelectedCoords(3);
            
            if (cruiserPlaced) {
                cruiserCoords = battleship.getSelected();
                status.setText("Cruiser ship successfully placed! "
                        + "Please pick 3 spots for your submarine.");

            } else {
                status.setText("Invalid tiles for cruiser ship, please try again.");
            }
        } else if (!subPlaced) {
            subPlaced = battleship.checkSelectedCoords(3);
            
            if (subPlaced) {
                subCoords = battleship.getSelected();
                status.setText("Submarine successfully placed! "
                        + "Please pick 2 spots for your destroyer ship.");

            } else {
                status.setText("Invalid tiles for submarine ship, please try again.");
            }
        } else if (!desPlaced) {
            desPlaced = battleship.checkSelectedCoords(2);
            
            if (desPlaced) {
                desCoords = battleship.getSelected();
                status.setText("Destroyer ship successfully placed! ");

                
                if (!user1Placed) {
                    battleship.placeShips(1, carrierCoords, bsCoords, 
                            cruiserCoords, subCoords, desCoords);
                    user1Placed = true;
                    switchTurn();
                    resetCoords();
                } else if (user1Placed && !user2Placed) {
                    battleship.placeShips(2, carrierCoords, bsCoords, 
                            cruiserCoords, subCoords, desCoords);
                    user2Placed = true;
                    switchTurn();
                } else {
                    status.setText("Done placing ships. It is now " + battleship.getUser1Name() + 
                            "'s turn.");
                    startGame();
                }
            } else {
                status.setText("Invalid tiles for destroyer ship, please try again.");
            }
        }
        
        battleship.deselect();
        repaint();
    }
    
    
    
    /**
     * Resetting booleans used in placing ships
     */
    public void setPlacedBools(boolean b) {
        carrierPlaced = b;
        bsPlaced = b;
        cruiserPlaced = b;
        subPlaced = b;
        desPlaced = b;
    }
 
    
    /**
     * Gives the user nicknames to model
     * @param n1 nickname of user 1
     * @param n2 nickname of user 2
     */
    public void setNames(String n1, String n2) {
        battleship.setUser1Name(n1);
        battleship.setUser2Name(n2);
        spanel.setNames(n1, n2);
    }
    
    public void startGame() {
        battleship.startGame();
        battleship.deselect();
        repaint();
    }
    
    public boolean fire() {
        int[] p = bsViewer.getSelected();
        
        
        p[0] = p[0] - 11;
        if (p[0] < 0) {
            status.setText("Please select the tile that you would like to fire on.");
            return false;
        }
        
        String n = battleship.fire(p, battleship.getEnemyTiles());
        
        if (n.equals("invalid")) {
            status.setText("Please select a tile that you have not fired on already! Try again.");
            return false;
        } else {
            status.setText(n);
            bsViewer.deselect();
            
            if (battleship.checkWin() == 1) {
                status.setText(battleship.getUser1Name() + " has won! Game over.");
                gameover = true;
                writeWinner(battleship.getUser1Name());
            } else if (battleship.checkWin() == 2) {
                status.setText(battleship.getUser2Name() + " has won! Game over.");
                gameover = true;
                writeWinner(battleship.getUser2Name());
            } else {
                switchTurn();
            }
            
            updateSPanel(battleship.getUser1Ships(), battleship.getUser2Ships());
            repaint(); 
            return true;
        }

    }
    
    public String saveState() {
        String[] shipNames = {"carrier", "battleship", "cruiser", "submarine", "destroyer"};
        String line1 = battleship.getUser1Name();
        String line2 = battleship.getUser2Name();
        String line3 = "";
        String line4 = "";
        TreeMap<String, Ship> ships1 = battleship.getUser1Ships();
        TreeMap<String, Ship> ships2 = battleship.getUser2Ships();
        
        for (int i = 0; i < 5; i++) {
            int[][] coords = (ships1.get(shipNames[i])).getPos();
            line3 += toString(coords);
            
            int[][] coords2 = (ships2.get(shipNames[i])).getPos();
            line4 += toString(coords2);
        }
        
        String line5 = "";
        String line6 = "";
        Tile[][] tiles1 = battleship.getUser1Board();
        Tile[][] tiles2 = battleship.getUser2Board();
        
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Tile t = tiles1[x][y];
                if (t.getHit()) {
                    line5 += Integer.toString(x) + Integer.toString(y) + " ";
                }
                
                Tile t2 = tiles2[x][y];
                if (t2.getHit()) {
                    line6 += Integer.toString(x) + Integer.toString(y) + " ";
                }
                
            }
        }
        
        return (line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5 + "\n" + line6);
    }
    
    private String toString(int[][] n) {
        String s = "";
        if (n != null) {
            for (int i = 0; i < n.length; i++) {
                s += n[i][0];
                s += n[i][1];
            }
            s += " ";
        } else {
            status.setText("An error occured");
        }
        return s;
    }
    
    /**
     * 
     * @param user1Name
     * @param user2Name
     * @param shipCoords1
     * @param shipCoords2
     * @param firedCoords1
     * @param firedCoords2
     */
    public void restoreState(String user1Name, String user2Name, ArrayList<String> shipCoords1, 
            ArrayList<String> shipCoords2, ArrayList<String> firedCoords1, 
            ArrayList<String> firedCoords2) {
        
        battleship.reset();
        
        battleship.setUser1Name(user1Name);
        battleship.setUser2Name(user2Name);
        
        if (shipCoords1.size() >= 5) {
            setCoords(carrierCoords, shipCoords1.get(0));
            setCoords(bsCoords, shipCoords1.get(1));
            setCoords(cruiserCoords, shipCoords1.get(2));
            setCoords(subCoords, shipCoords1.get(3));
            setCoords(desCoords, shipCoords1.get(4));
        } else {
            status.setText("An error occured");
            return;
        }
        
        battleship.placeShips(1, carrierCoords, bsCoords, cruiserCoords, subCoords, desCoords);
        user1Placed = true;
        setPlacedBools(true);
        resetCoords();
        
        if (shipCoords2.size() >= 5) {
            setCoords(carrierCoords, shipCoords2.get(0));
            setCoords(bsCoords, shipCoords2.get(1));
            setCoords(cruiserCoords, shipCoords2.get(2));
            setCoords(subCoords, shipCoords2.get(3));
            setCoords(desCoords, shipCoords2.get(4));
        } else {
            status.setText("An error occured");
            return;
        }
        
        battleship.placeShips(2, carrierCoords, bsCoords, cruiserCoords, subCoords, desCoords);
        user2Placed = true;
        setPlacedBools(true);
        resetCoords();
        startGame();
        
        Tile[][] user2Board = battleship.getUser2Board();
        Tile[][] user1Board = battleship.getUser1Board();
        
        for (int i = 0; i < firedCoords1.size(); i++) {
            if (firedCoords1.get(i) == "") {
                break;
            }
            String[] coords = firedCoords1.get(i).split("");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            int[] p = {x, y};
            battleship.fire(p, user1Board);
        }
        
        for (int j = 0; j < firedCoords2.size(); j++) {
            if (firedCoords2.get(j) == "") {
                break;
            }
            String[] coords = firedCoords2.get(j).split("");
            int x = Integer.valueOf(coords[0]);
            int y = Integer.valueOf(coords[1]);
            int[] p = {x, y};
            battleship.fire(p, user2Board);
        }
        
        updateSPanel(battleship.getUser1Ships(), battleship.getUser2Ships());
        
        if (firedCoords1.size() < firedCoords2.size()) {
            setTurn(2);
        }
        
    }
    
    /**
     * Manually set it so that it's the second player's turn
     */
    private void setTurn(int n) {
        if (n == 2) {
            battleship.getUser2Board();
            ships = battleship.getUser2Ships();
            spanel.setTurn(battleship.getUser2Name());
            battleship.setTurn(2);
        }
        
    }
    
    private void setCoords(int[][] coords, String numbers) {
        String[] s = numbers.split("");        
        for (int i = 0; i < s.length; i++) {
            if (i % 2 == 0) {
                coords[i / 2][0] = Integer.valueOf(s[i]);
            } else {
                coords[i / 2][1] = Integer.valueOf(s[i]);
            }
            
        }
    }
    
    /**
     * Switching to the next turn, updating board and ships
     */
    private void switchTurn() {
        drawBoard = false;
        repaint();
        
        JFrame f = new JFrame();
        JOptionPane.showMessageDialog(f,"Switch users!"); 
        
        drawBoard = true;
        battleship.nextTurn();
        spanel.setTurn(battleship.getTurn());
        battleship.getCurrentTiles();
        ships = battleship.getCurrentShips();
        repaint();

    }
    
    private void resetCoords() {
        carrierCoords = new int[5][2];
        bsCoords = new int[4][2];
        cruiserCoords = new int[3][2];
        subCoords = new int[3][2];
        desCoords = new int [2][2];
    }
    
    private void writeWinner(String n) {
        File file = Paths.get("files/PlayerRankings.txt").toFile();
        Writer w;
        
        try {
            w = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(w);
            bw.newLine();
            bw.write(n);
            bw.close();
        } catch (IOException e) {
            status.setText("An error occured");
            return;
        } 
    }
    
    private void updateSPanel(TreeMap<String, Ship> ships1, TreeMap<String, Ship> ships2) {
        
        for (Entry<String, Ship> e : ships1.entrySet()) {
            boolean sunk = e.getValue().getSunk(); 
            if (sunk) {
                spanel.sinkShip(e.getKey(), 1);
            }
        }
        for (Entry<String, Ship> e : ships2.entrySet()) {
            boolean sunk = e.getValue().getSunk();
            if (sunk) {
                spanel.sinkShip(e.getKey(), 2);
            }
        }
    }

    
    //ACCESSORS
    
    public TreeMap<String, Ship> getShips() {
        return ships;
    }
    
    public boolean getUser1Placed() {
        return user1Placed;
    }
    
    public boolean getUser2Placed() {
        return user2Placed;
    }
    
    public String getTurn() {
        return battleship.getTurn();
    }
    
    public String getNotTurn() {
        return battleship.getNotTurn();
    }
    
    public boolean getPlayingGame() {
        return battleship.getPlayingGame();
    }

    
    //FIGURE OUT HOW TO UNDRAW WHEN DESELECTING?
    private void drawSelectedTiles(Graphics g) {
        g.setColor(Color.GREEN);

        if (!battleship.getPlayingGame() && !gameover) {
            int[][] selected = battleship.getSelected();
            
            if (selected.length > 0) {
                for (int i = 0; i < selected.length; i ++) {
                    int x = selected[i][0];
                    int y = selected[i][1];
                    
                    g.fillRect(40 * x + 20, 40 * y, 40, 40);
                }
                
            }
        } else {
            int[] selected = bsViewer.getSelected();

            if (selected.length == 2) {
                g.fillRect(40 * selected[0] + 20, 40 * selected[1], 40, 40);
            }
        }
        
        g.setColor(Color.BLACK);

    }
    
    private void drawGrid(Graphics g, int x) {
     // Draws board grid
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        
        for (int i = 0; i < 11; i++) {
            g.drawLine(40 * i  + x, 0, 40 * i + x, 400);
            g.drawLine(x, 40 * i, 400 + x, 40 * i);
            if (i != 10) {
                g.drawString(Integer.toString(i + 1), (40 * i) + 20 + x, 415);
                g.drawString(letters[i], 5, 40 * i + 20);
                g.drawString(letters[i], 420 + x, 40 * i + 20);
            }
        }
        
    }
    
    private void drawUserShips(Graphics g, Tile[][] tList) {
        //Meaning user is selecting ships
        if (!battleship.getPlayingGame()) {
            if (carrierPlaced) {
                drawShip(g, carrierCoords);
            }
            if (bsPlaced) {

                drawShip(g, bsCoords);
            }
            if (cruiserPlaced) {
                drawShip(g, cruiserCoords);

            }
            if (subPlaced) {
                drawShip(g, subCoords);
 
            }
            if (desPlaced) {
                drawShip(g, desCoords); 
            }
        }
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Tile t = tList[i][j];
                if (!t.getEmpty()) {
                    g.setColor(Color.BLUE);
                    g.fillRect(40 * i + 20, 40 * j, 40, 40);
                } 
            }
        } 
        g.setColor(Color.BLACK);
    }
    
    private void drawShip(Graphics g, int[][] coords) {
        
        if (coords != null) {
            g.setColor(Color.BLUE);
            for (int i = 0; i < coords.length; i++) {
                int x = coords[i][0];
                int y = coords[i][1];
                
                g.fillRect(40 * x + 20, 40 * y, 40, 40);
            }  
        } 
    }
    
    private void drawAllShips(Graphics g) {
        Tile[][] b1 = battleship.getUser1Board();
        Tile[][] b2 = battleship.getUser2Board();
        g.setColor(Color.BLUE);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Tile t = b1[i][j];
                if (!t.getEmpty()) {
                    g.fillRect(40 * i + 20, 40 * j, 40, 40);
                } 
            }
        } 
        
        for (int x = 11; x < 21; x++) {
            for (int y = 0; y < 10; y++) {
                Tile t = b2[x - 11][y];
                if (!t.getEmpty()) {
                    g.fillRect(40 * x + 20, 40 * y, 40, 40);
                } 
            }
        } 
        
        g.setColor(Color.BLACK);
        
    }
    
    private void drawFires(Graphics g, Tile[][] tList, int x) {
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Tile t = tList[i][j];
                if (t.getHit() && !t.getEmpty()) {
                    g.setColor(Color.RED);
                    g.fillOval((400 * i) / 10 + 40 + x, (400 * j) / 10 + 20, 6, 6);
                    
                } else if (t.getHit()) {
                    g.setColor(Color.GRAY);
                    g.fillOval((400 * i) / 10 + 40 + x, (400 * j) / 10 + 20, 6, 6);
                }
                g.setColor(Color.BLACK);
            }
        }
        
    }
    
    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board.  This approach
     * will not be sufficient for most games, because it is not 
     * modular.  All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper 
     * methods.  Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
      
        Tile[][] userTiles = battleship.getCurrentTiles();
        Tile[][] enemyTiles = battleship.getEnemyTiles();
        
        drawFires(g, enemyTiles, 440);
        
        if (drawBoard) {
            drawSelectedTiles(g);
            if (gameover) {
                drawAllShips(g);
            } else {
                drawUserShips(g, userTiles);
            }
        }
        
        drawFires(g, userTiles, 0);
        drawFires(g, enemyTiles, 440);

        drawGrid(g, 20);
        drawGrid(g, 460);
    }
    
    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}