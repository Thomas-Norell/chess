package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;

public class Pawn extends Piece {
    final int direction;


    //TODO: En Passant
    public Pawn(Color col, Coordinate c) {
        this.setCoordinate(c);
        this.setColor(col);
        if (col.isWhite()) {
            direction = 1;
        }
        else {
            direction = -1;
        }
    }

    @Override
    public ArrayList<Square> validMoves(ChessBoard board) {
        ArrayList<Square> moves = new ArrayList();
        Square front = board.getSquare(new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY() + direction));
        Square frontLeft = board.getSquare(new Coordinate(this.getCoordinate().getX() - direction, this.getCoordinate().getY() + direction));
        Square frontRight = board.getSquare(new Coordinate(this.getCoordinate().getX() + direction, this.getCoordinate().getY() + direction));
        if (front.isValid() && !front.isOccupied()) {
            moves.add(front);
        }
        if (frontLeft.isValid() && !frontLeft.Occupant().getColor().sameColor(this.getColor())) {
            moves.add(frontLeft);
        }
        if (frontRight.isValid() && !frontRight.Occupant().getColor().sameColor(this.getColor())) {
            moves.add(frontRight);
        }
        return moves;
    }
}
