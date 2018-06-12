package board;
import javafx.scene.image.Image;
import pieces.Piece;
public class Square {
    private Color color;
    private Coordinate coord;
    private ChessBoard board;
    private Piece occupant;
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
    public void setOccupant(Piece p) {
        this.occupant = p;
    }

    public Piece Occupant() {
        return occupant;
    }
    public boolean isOccupied() {
        return occupant != null;
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

    public Image image() {
        if (getColor().isWhite()) {
            return new Image("images/whiteSquare.png");
        }
        return new Image("images/blackSquare.png");
    }

}
