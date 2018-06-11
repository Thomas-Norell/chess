package board;
import pieces.Piece;
public class Square {
    private Color color;
    private Coordinate coord;
    private ChessBoard board;
    private boolean valid;
    public Square(Color c, Coordinate coord, ChessBoard board){
        if (coord.getX() < 0 || coord.getX() > 7 || coord.getY() < 0 || coord.getY() > 7) {
            valid = false;
        }
        else {
            valid = true;
        }
        this.color = c;
        this.coord = coord;
        this.board = board;
    }
    public Color getColor() {
        return color;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public Piece Occupant() {
        return board.getPiece(this);
    }
    public boolean isOccupied() {
        return board.getPiece(this) != null;
    }
    public Square neighbor(int xDiff, int yDiff) {
        if (coord.getX() + xDiff < 0 || coord.getX() + xDiff > 7 || coord.getY() + yDiff < 0 || coord.getY() + yDiff > 7) {
            return null;
        }
        return board.getSquare(new Coordinate(coord.getX() + xDiff, coord.getY() + yDiff));
    }
    public boolean isValid() {
        return valid;
    }

}
