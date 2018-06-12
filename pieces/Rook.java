package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;

public class Rook extends Piece {


    public Rook(Color col, Coordinate c) {
        this.setCoordinate(c);
        this.setColor(col);
    }

    @Override
    public Piece deepCopy() {
        return new Rook(this.getColor(), this.getCoordinate());
    }

    static void checkAdd(Piece self, int direction, ArrayList<Square> moves, ChessBoard board) {
        int myX = self.getCoordinate().getX();
        int myY = self.getCoordinate().getY();
        for (int i = myX + direction; i < 8 - myX && i >= 0; i += direction) {
            Square thisSquare = board.getSquare(new Coordinate(i, myY));
            if (thisSquare.isOccupied()) {
                if (thisSquare.Occupant().getColor().sameColor(self.getColor())) { //We followed a path to a piece of the same color
                    break;
                }
                else {
                    moves.add(thisSquare); //We can attack an enemy piece
                    break; //we can't jump over it
                }
            }
            moves.add(thisSquare);
        }
        for (int i = myY + direction; i < 8 - myX && i >= 0; i += direction) {
            Square thisSquare = board.getSquare(new Coordinate(myX, i));
            if (thisSquare.isOccupied()) {
                if (thisSquare.Occupant().getColor().sameColor(self.getColor())) { //We followed a path to a piece of the same color
                    break;
                }
                else {
                    moves.add(thisSquare); //We can attack an enemy piece
                    break; //we can't jump over it
                }
            }
            moves.add(thisSquare);
        }

    }

    @Override
    public ArrayList<Square> validMoves(ChessBoard board) {
        ArrayList<Square> moves = new ArrayList();
        checkAdd(this, 1, moves, board);
        checkAdd(this, -1, moves, board);
        return moves;
    }
}
