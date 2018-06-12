package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;

public class Bishop extends Piece {


    public Bishop(Color col, Coordinate c) {
        this.setCoordinate(c);
        this.setColor(col);
    }

    @Override
    public Piece deepCopy() {
        return new Bishop(this.getColor(), this.getCoordinate());
    }

    static void checkAdd(Piece self, int xDir, int yDir, ArrayList<Square> moves, ChessBoard board) {
        int myX = self.getCoordinate().getX();
        int myY = self.getCoordinate().getY();
        for (int x = myX + xDir,y = myY + yDir; x < 8 && y < 8 && x >= 0 && y >= 0; x += xDir, y += yDir) {
            Square thisSquare = board.getSquare(new Coordinate(x, y));
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
        checkAdd(this, 1, 1, moves, board);
        checkAdd(this, -1, 1, moves, board);
        checkAdd(this, 1, -1, moves, board);
        checkAdd(this, -1, -1, moves, board);
        return moves;
    }
}
