
public class BattleShipViewer {
    private Tile[][] board;
    private int[] selected;
    
    public BattleShipViewer() {
        board = newBoard();
        selected = new int[2];
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
    
    //Returns true for if ship is hit
    public boolean fire(int[] p) {
        int x = p[0];
        int y = p[1];
        
        Tile t = board[x][y];
        
        boolean isEmpty = t.getEmpty();
        
        if (!isEmpty) {
            Ship s = t.getShip();
            s.hitShip();
        }
        
        t.hitTile();
        
        return (!isEmpty);
    }
    
    public void select(int[] p) {
        if (selected != p) {
            selected = p;
        } else {
            selected = new int[2];
        }
    }
    
    public void deselect() {
        selected = new int[2];
    }
    
    public int[] getSelected() {
        return selected;
    }
    
    
    
    
    
}
