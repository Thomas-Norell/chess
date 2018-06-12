package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;

public class Queen extends Piece {


    public Queen(Color col, Coordinate c, ChessBoard b) {
        setCoordinate(c);
        setColor(col);
        setBoard(b);
    }

    @Override
    public Piece deepCopy(ChessBoard b) {
        return new Queen(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()), b);
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
