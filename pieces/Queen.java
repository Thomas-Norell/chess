package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;

public class Queen extends Piece {


    public Queen(Color col, Coordinate c) {
        this.setCoordinate(c);
        this.setColor(col);
    }

    @Override
    public Piece deepCopy() {
        return new Queen(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()));
    }



    @Override
    public ArrayList<Square> validMoves(ChessBoard board) {
        ArrayList<Square> moves = new ArrayList();
        Rook.checkAdd(this, 1, moves, board);
        Rook.checkAdd(this, -1, moves, board);
        Bishop.checkAdd(this, 1, 1, moves, board);
        Bishop.checkAdd(this, -1, 1, moves, board);
        Bishop.checkAdd(this, 1, -1, moves, board);
        Bishop.checkAdd(this, -1, -1, moves, board);
        return moves;
    }
}
