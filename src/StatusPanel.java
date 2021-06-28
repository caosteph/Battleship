import javax.swing.JPanel;
import java.awt.*;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {
    private String name1;
    private String name2;
    private String turn;
    private boolean[] ships1;
    private boolean[] ships2;
    
    public StatusPanel() {
        name1 = "User 1";
        name2 = "User 2";
        turn = name1;
        initializeShipBools();
        
    }
    
    public void setNames(String n1, String n2) {
        name1 = n1;
        name2 = n2;
        repaint();
    }
    
    public void setTurn(String n) {
        turn = n;
        repaint();
    }
    
    public void sinkShip(String shipName, int user) {
        String[] shipNames = {"carrier", "battleship", "cruiser", "submarine", "destroyer"};
        int index = 10;
        for (int i = 0; i < 5; i++) {
            if (shipNames[i].equals(shipName)) {
                index = i;
            }
        }
        
        if (user == 1) {
            ships1[index] = true;
        } else {
            ships2[index] = true;
        }
    }
    
    public void reset() {
        initializeShipBools();
        repaint();
    }
    
    private void initializeShipBools() {
        ships1 = new boolean[5];
        ships2 = new boolean[5];
        
        for (int i = 0; i < 5; i++) {
            ships1[i] = false;
            ships2[i] = false;
        }
    }
    
    private void drawShipsAndLabels(Graphics g, int y, int user) {
        boolean[] b = new boolean[5];
        
        if (user == 1) {
            b = ships1;
        } else {
            b = ships2;
        }
        
        g.drawString("Carrier", 20, y);
        drawShip(g, 5, 70, y - 10, b[0]);
        
        g.drawString("Battleship", 20, y + 30);
        drawShip(g, 4, 90, y + 20, b[1]);
        
        g.drawString("Cruiser", 20, y + 60);
        drawShip(g, 3, 75, y + 50, b[2]);
        
        g.drawString("Submarine", 20, y + 90);
        drawShip(g, 3, 90, y + 80, b[3]);
        
        g.drawString("Destroyer", 20, y + 120);
        drawShip(g, 2, 90, y + 110, b[4]);
        
    }
    
    private void drawShip(Graphics g, int size, int x, int y, boolean sunk) {
        
        if (sunk) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }

        for (int i = 0; i < size; i++) {
            g.fillRect(x + 20 * i, y, 10, 10);
        }
        g.setColor(Color.BLACK);
        
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //g.drawLine(0, 0, 200, 450);
        
        g.drawString(name1 + "'s ships", 50, 10);
        drawShipsAndLabels(g, 30, 1);
        g.drawString(name2 + "'s ships", 50, 180);
        drawShipsAndLabels(g, 200, 2);
        g.drawString("Turn: " + turn, 50, 350);
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 450);
    }
}
