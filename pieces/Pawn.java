package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import java.util.TreeMap;

import board.Color;
import game.Visualizer;
import javafx.scene.image.Image;

public class Pawn extends Piece {
    final int direction;
    boolean hasMoved = false;

    @Override
    public double getValue() {
        return 1;
    }


    //TODO: En Passant
    public Pawn(Color col, Coordinate c, ChessBoard b) {
        setCoordinate(c);
        setColor(col);
        setBoard(b);
        if (col.isWhite()) {
            direction = 1;
        }
        else {
            direction = -1;
        }
    }

    @Override
    public Piece deepCopy(ChessBoard b) {
        Pawn a = new Pawn(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()), b);
        a.hasMoved = this.hasMoved;
        return a;
    }

    @Override
    public ArrayList<Square> validMoves() {
        ChessBoard board = this.getBoard();
        ArrayList<Square> moves = new ArrayList();
        Square front = board.getSquare(new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY() + direction));
        Square frontLeft = board.getSquare(new Coordinate(this.getCoordinate().getX() - direction, this.getCoordinate().getY() + direction));
        Square frontRight = board.getSquare(new Coordinate(this.getCoordinate().getX() + direction, this.getCoordinate().getY() + direction));
        if (front != null && front.isValid() &&!front.isOccupied()) {
            moves.add(front);
        }
        if (frontLeft != null && frontLeft.isValid() && frontLeft.isOccupied() && !frontLeft.Occupant().getColor().sameColor(this.getColor())) {
            moves.add(frontLeft);
        }
        if (frontRight != null && frontRight.isValid() &&frontRight.isOccupied() && !frontRight.Occupant().getColor().sameColor(this.getColor())) {
            moves.add(frontRight);
        }
        if (!hasMoved) {
            Square frontFront = board.getSquare(new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY() + direction * 2));
            if (!front.isOccupied() && !frontFront.isOccupied() && frontFront != null && frontFront.isValid()) {
                moves.add(frontFront);
            }
        }
        return moves;
    }

    public Image image() {
        if (getColor().isWhite()) {
            return new Image("images/whitePawn.png");
        }
        return new Image("images/blackPawn.png");
    }
}
