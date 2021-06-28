
public class Tile {
    private Ship ship;
    private boolean empty;
    private boolean hit;
    private boolean selected;
    
    public Tile(Ship tileShip, int i, int j) {
        ship = tileShip;
        if (tileShip != null) {
            empty = false;
        } else {
            empty = true;
        }
        
        hit = false;
        selected = false;
    }
    
    public void placeShip(Ship placedShip) {
        ship = placedShip;
        empty = false;
    }
    
    public void hitTile() {
        hit = true;
    }
    
    public void selectTile() {
        selected = !selected;
    }
    
    public boolean getEmpty() {
        return empty;
    }
    
    public Ship getShip() {
        return ship;
    }
    
    public boolean getHit() {
        return hit;
    } 
   
    public boolean getSelected() {
        return selected;
    }
    
    
    
}
