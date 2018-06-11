package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import board.Color;

import java.util.ArrayList;

public abstract class Piece {
    private Coordinate coord;
    private Color color;
    private boolean alive = true;

    protected Coordinate getCoordinate() {
        return this.coord;
    }
    protected void setCoordinate(Coordinate c) {
        this.coord = c;
    }
    protected Color getColor() {
        return this.color;
    }
    protected void setColor(Color c) {
        this.color = c;
    }
    public boolean isAlive() {
        return alive;
    }
    public void kill() {
        alive = false;
    }


    public abstract ArrayList<Square> validMoves(ChessBoard board);
}
