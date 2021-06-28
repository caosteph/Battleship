/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  We STRONGLY 
 * recommend you review these lecture slides, starting at slide 8, 
 * for more details on Model-View-Controller:  
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard.  The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class Game implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("BATTLESHIP!");
        frame.setLocation(200, 200);
        

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);
        
      //Side bar
        final JPanel side_panel = new JPanel();
        final StatusPanel statPanel = new StatusPanel();
        frame.add(statPanel, BorderLayout.EAST);
        

        //GAME BOARD
        final JPanel board_panel = new JPanel();
        board_panel.setLayout(new BorderLayout());
        final JPanel board_label_panel = new JPanel();
        board_label_panel.setLayout(new GridLayout(1, 2));
        final JLabel board1Label = new JLabel("User 1's Board");
        final JLabel board2Label = new JLabel("User 2's Board");
        board_label_panel.add(board1Label);
        board_label_panel.add(board2Label);
        board_panel.add(board_label_panel, BorderLayout.NORTH);
        
        final GameBoard board = new GameBoard(status, statPanel);
        board.updateStatus();
        board_panel.add(board, BorderLayout.SOUTH);
        frame.add(board_panel, BorderLayout.WEST);

        // TOP PANEL
        final JPanel control_panel = new JPanel();
        final JPanel left_corner = new JPanel();
        left_corner.setLayout(new GridLayout(1, 3));
        
        final JButton fireButton = new JButton("Place");
        final JButton saveButton = new JButton("Restore");
        final JButton reset = new JButton("Reset");
        
        fireButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!board.getPlayingGame()) {
                    placeButtonAction(board);
                    if (board.getPlayingGame()) {
                        fireButton.setText("Fire");
                        saveButton.setText("Save");
                    }
                    updateBoardLabels(board1Label, board2Label, board);
                } else {
                    boolean fired = board.fire();
                    if (fired) {
                        updateBoardLabels(board1Label, board2Label, board);
                    }
                }
            }
        });
        
        
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveButton.getText().equals("Save")) {
                    saveButtonAction(board);
                } else {
                    if (restoreState(board)) {
                        saveButton.setText("Save");
                        fireButton.setText("Fire");
                    } else {
                        status.setText("Unable to restore game.");
                    }
                }
                
                if (board.getPlayingGame()) {
                    saveButton.setText("Save");
                }
            }
        }); 
        
        
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
                saveButton.setText("Restore");
                fireButton.setText("Place");
                updateBoardLabels(board1Label, board2Label, board);
                
            }
        });
        
        
        
        left_corner.add(saveButton);
        left_corner.add(reset);
        left_corner.add(fireButton);

        
        final JLabel rankingsLabel = new JLabel(readPlayerRankings());
        final JLabel titleLabel = new JLabel("BATTLESHIP");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setPreferredSize(new Dimension(400, 100));
        
        control_panel.setLayout(new GridLayout(1, 3));
        control_panel.add(left_corner);
        control_panel.add(titleLabel);
        control_panel.add(rankingsLabel);
        frame.add(control_panel, BorderLayout.NORTH);
        
        side_panel.setLayout(new GridLayout(5, 1));

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
        
        showInstructions();

        String u1 = getUsername(1);
        String u2 = getUsername(2);
        board.setNames(u1, u2);
        board1Label.setText(u1 + "'s Board");
        board2Label.setText(u2 + "'s Board");

    }
    
    private void showInstructions() {
        JFrame f = new JFrame();  
        
        String n = "Hi! Welcome to Battleship. \n"
                + "GAME OBJECTIVE: \n"
                + "The object of Battleship is to try and sink all of the other "
                + "player's ships before they sink all of your ships.\n "
                + "All of the other player's ships are somewhere on his/her board.\n"
                + "You try and hit them by calling out the coordinates of "
                + "one of the squares on the board.\n"
                + "The other player also tries to hit your ships by calling out coordinates.\n"
                + "Neither you nor the other player can see the other's board "
                + "so you must try to guess where they are.\n"
                + "Each board in the physical game has two grids: the lower (horizontal) section "
                + "for the player's ships\n"
                + "and the upper part (vertical during play) for recording the player's guesses.\n"
                + "STARTING A NEW GAME: \n"
                + "Each player places the 5 ships somewhere on their board.\n"
                + "The ships can only be placed vertically or horizontally.\n"
                + "Diagonal placement is not allowed.\n"
                + "No part of a ship may hang off the edge of the board.\n"
                + "Ships may not overlap each other.\n"
                + "No ships may be placed on another ship.\n"
                + "No ships can be placed within one tile of each other. \n"
                + "Once the guessing begins, the players may not move the ships.\n"
                + "The 5 ships are:  Carrier (occupies 5 spaces), Battleship (4), Cruiser (3), "
                + "Submarine (3), and Destroyer (2).  \n"
                + "PLAYING THE GAME:\n"
                + "Player's take turns guessing by selecting coordinates to fire on.\n"
                + "The board will be marked with dots:  red for hit, grey for miss.\n"
                + "When all of the squares that one your ships occupies have been hit, "
                + "the ship will be sunk.\n"
                + "You can keep track of which ships have been sunk at the side.\n"
                + "As soon as all of one player's ships have been sunk, the game ends.\n";
        
        JOptionPane.showMessageDialog(f,n);  
    }
    
    private String getUsername(int n) {
        JFrame f = new JFrame();   
        String name = JOptionPane.showInputDialog(f,"Enter a nickname for player " + n + ".");  
        
        return name;
    }
    
    private void updateBoardLabels(JLabel l1, JLabel l2, GameBoard board) {
        l1.setText(board.getTurn() + "'s Board");
        l2.setText(board.getNotTurn() + "'s Board");
    }

    
    private void placeButtonAction(GameBoard b) {
        if (!b.getUser1Placed()) {
            b.placeShip();
            
            if (b.getUser1Placed()) {
                b.setPlacedBools(false);
            }
        } else if (!b.getUser2Placed()) {
            b.placeShip();
            if (b.getUser2Placed()) {
                b.startGame();
            }
        } 
        
    }
    
    private void saveButtonAction(GameBoard b) {
        
        File file = Paths.get("files/GameState.txt").toFile();
        Writer w;
        
        try {
            w = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(w);
            String s = b.saveState();
            bw.write(s);
            bw.close();
            
        } catch (IOException e) {
            return;
        }
        
    }
    
    /**
     * 
     * @param b GameBoard for current game
     * @return boolean that is true if state was successfully able to be restored
     * Reading contents of GameState.txt to restore state
     */
    @SuppressWarnings({ "resource", "unused" })
    private boolean restoreState(GameBoard b) {
        File file = Paths.get("files/GameState.txt").toFile();
        Reader r;
        Writer w;
        
        try {
            
            r = new FileReader(file);
            BufferedReader br = new BufferedReader(r);
            
            String line1 = br.readLine();
            String line2;
            if (line1 != null) {
                line2 = br.readLine(); 
            } else {
                return false;
            }
            
            if (line2 == null) {
                return false;
            }
            
            //Reading lines for information about where ships are positions and 
            //what tiles have been fired upon already.
            String[] line3 = null;
            String[] line4 = null;
            String[] line5 = null;
            String[] line6 = null;
            String[][] lines = {line3, line4, line5, line6};
            
            for (int i = 0; i < 4; i++) {
                String nextLine = br.readLine();
                if (nextLine != null) {
                    if (nextLine.contains(" ")) {
                        lines[i] = nextLine.split(" ");  
                    } else {
                        lines[i] = new String[1];
                        lines[i][0] = "";
                    }
                } else {
                    return false;
                }
            }
            
            br.close();

            String name1 = line1;
            String name2 = line2;

            //Getting array of coordinates into an ArrayList 
            ArrayList<String> shipCoords1 = lineToArray(lines[0]);
            ArrayList<String> shipCoords2 = lineToArray(lines[1]);
            ArrayList<String> firedCoords1 = lineToArray(lines[2]);
            ArrayList<String> firedCoords2 = lineToArray(lines[3]);
            
            
            b.restoreState(name1, name2, shipCoords1, shipCoords2, firedCoords1, firedCoords2);
            
           
            

        } catch (IOException e) {
            return false;
        } 
        
        return true;
        
    }
    
    private ArrayList<String> lineToArray(String[] line) {
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < line.length; i++) {
            output.add(line[i]);
        }
        return output;
    }
    
    private String readPlayerRankings() {
        File file = Paths.get("files/PlayerRankings.txt").toFile();
        Reader r;
        
        try {
            r = new FileReader(file);
            BufferedReader br = new BufferedReader(r);
            TreeMap<String, Integer> nameOccurences = new TreeMap<String, Integer>();
            
            String currentLine = br.readLine();
            
            while (currentLine != null) {
                if (nameOccurences.containsKey(currentLine)) {
                    int occured = nameOccurences.get(currentLine);
                    nameOccurences.replace(currentLine, occured + 1);
                } else {
                    nameOccurences.put(currentLine, 1);
                }
                currentLine = br.readLine();
            }
            
            br.close();

            
            int[] maxes = {0, 0, 0};
            String[] names = {"", "", ""};
            
            for (Entry<String, Integer> e : nameOccurences.entrySet()) {
                String name = e.getKey();
                int n = e.getValue();
                if (n > maxes[0]) {
                    maxes[2] = maxes[1];
                    maxes[1] = maxes[0];
                    maxes[0] = n;
                    
                    names[2] = names[1];
                    names[1] = names[0];
                    names[0] = name;
                    
                } else if (n > maxes[1]) {
                    maxes[2] = maxes[1];
                    maxes[1] = n;
                    
                    names[2] = names[1];
                    names[1] = name;
                    
                } else if (n > maxes[2]) {
                    maxes [2] = n;
                    names[2] = name;
                }
                
            }
            
            String output = "RANKINGS:\n(1) " + names[0] + " - " + maxes[0] + 
                    "\n(2) " + names[1] + " - " + maxes[1] + "\n(3) " + names[2] + " - " + maxes[2];

            return output;
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error occured";
        } 
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}