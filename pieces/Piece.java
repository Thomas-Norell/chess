package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import board.Color;

import java.util.ArrayList;

public abstract class Piece {
    Coordinate coord;
    Color color;

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

    protected abstract ArrayList<Square> validMoves(ChessBoard board);
}
