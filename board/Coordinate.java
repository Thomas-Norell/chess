package board;

import java.util.Objects;

public class Coordinate {
    private int x;
    private int y;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

     public int getY() {
        return y;
     }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public boolean equals(Coordinate other) {
        return this.x == other.x && this.y == other.y;
    }
}
