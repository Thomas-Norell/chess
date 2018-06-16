package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import board.Color;

import javafx.scene.Node;
import javafx.scene.image.Image;

import java.util.ArrayList;


public abstract class Piece {
    private Coordinate coord;
    private Color color;
    private boolean alive = true;
    private ChessBoard board;
    Node node;

    protected final void setBoard(ChessBoard b) {
        board = b;
    }

    public final ChessBoard getBoard() {
        return board;
    }

    public final Coordinate getCoordinate() {
        return this.coord;
    }
    public final void setCoordinate(Coordinate c) {
        this.coord = c;
    }
    public final Color getColor() {
        return this.color;
    }
    public final void setColor(Color c) {
        this.color = c;
    }
    public boolean isAlive() {
        return alive;
    }
    public void kill() {
        alive = false;
        if (this.color.isWhite()) {
            board.getWhitePieces().remove(this);
        }
        else {
            board.getBlackPieces().remove(this);
        }
    }
    public abstract Piece deepCopy(ChessBoard newBoard);

    public abstract ArrayList<Square> validMoves();

    public final void move(Square dest) {
        Square source = board.getSquare(this.getCoordinate());
        this.setCoordinate(dest.getCoord());
        source.setOccupant(null);
        if (dest.isOccupied()) {
            dest.Occupant().kill();
        }
        dest.setOccupant(this);
        if (this instanceof Pawn) {
            ((Pawn) this).hasMoved = true;
        }

    }

    public void setNode(Node n) {
        node = n;
    }
    public Node getNode() {
        return node;
    }

    public abstract double getValue();

    public abstract Image image();
}
