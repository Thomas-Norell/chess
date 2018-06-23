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
        } else {
            board.getBlackPieces().remove(this);
        }
    }

    public abstract Piece deepCopy(ChessBoard newBoard);

    public abstract ArrayList<Square> validMoves();

    public final void move(Square dest) {
        if (dest.isOccupied() && dest.Occupant() instanceof King) {
            throw new Error("I shouldn't be allowed to take a king!");
        }


        if (this instanceof Pawn || dest.isOccupied()) {
            this.getBoard().movesSinceCaptureorPawn = 0;
        }
        else {
            this.getBoard().movesSinceCaptureorPawn += 1;
        }


        Square source = board.getSquare(this.getCoordinate());
        int y;
        if (color.isWhite()) {
            y = 0;
        }
        else {
            y = 7;
        }

        if (this instanceof King && !((King) this).hasMoved  //Castling

                && (dest.getCoord().equals(new Coordinate(2, y))
                || dest.getCoord().equals(new Coordinate(6, y)))) {

            this.setCoordinate(dest.getCoord());
            source.setOccupant(null);
            dest.setOccupant(this);
            if (dest.getCoord().equals(new Coordinate(2, y))) {
                board.getSquare(new Coordinate(0,y)).Occupant().move(getBoard().getSquare(new Coordinate(3, y)));
            }
            else if (dest.getCoord().equals(new Coordinate(6, y))) {
                board.getSquare(new Coordinate(7,y)).Occupant().move(getBoard().getSquare(new Coordinate(5, y)));

            }
            ((King) this).hasMoved = true;
            return;


        }


        if (this instanceof Pawn && (dest.getCoord().getY() == 7 || dest.getCoord().getY() == 0)) { //Pawn promotion
            Queen me = new Queen(this.getColor(), dest.getCoord(), getBoard());
            source.setOccupant(null);
            this.setCoordinate(dest.getCoord());
            if (dest.isOccupied()) {
                dest.Occupant().kill();
            }
            dest.setOccupant(me);
            board.pieces(this.getColor()).add(me);
            this.kill();
            return;

        }

        this.setCoordinate(dest.getCoord());
        source.setOccupant(null);
        if (dest.isOccupied()) {
            dest.Occupant().kill();
        }
        dest.setOccupant(this);
        if (this instanceof Pawn) {
            ((Pawn )this).hasMoved = true;
        }
        else if (this instanceof Rook) {
            ((Rook) this).hasMoved = true;
        }
        else if (this instanceof King) {
            ((King) this).hasMoved = true;
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
