

public class Ship {
    private int size;
    private boolean sunk;
    private int hits;
    private int[][] tiles;
    
    public Ship(int initSize) {
        size = initSize;
        sunk = false;
        hits = 0;
        
    }
    
    public void hitShip() {
        if (!sunk) {
            hits += 1;

            if (hits == size) {
                sunk = true;
            }

            
        } else {
            System.out.println("called hitShip on already sunk ship");
        }
           
    }
    
    public void placeShip(int[][] coords) {
        tiles = coords;
    }
    
    public boolean getSunk() {
        return sunk;
    }
    
    public int getHits() {
        return hits;
    }
    
    public boolean getHorizontal() {
        return (tiles[0][1] == tiles[1][1]);
    }
    
    public int[] getStartPoint() {
        int[] pt = new int[2];
        int min = 9;
        
        if (getHorizontal()) {
            for (int i = 0; i < size; i++) {
                if (tiles[i][0] < min) {
                    min = tiles[i][0];
                    pt[0] = tiles[i][0];
                    pt[1] = tiles[i][1];
                }
            }
        } else {
            for (int j = 0; j < size; j++) {
                if (tiles[j][1] < min) {
                    min = tiles[j][1];
                    pt[0] = tiles[j][0];
                    pt[1] = tiles[j][1];
                }
            }
        }
        
        return pt;
    }
    
    public int getSize() {
        return size;
    }
    
    public int[][] getPos() {
        return tiles;
    }
    
   
}
