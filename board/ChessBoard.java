package board;

import pieces.Pawn;
import pieces.Piece;
import java.util.HashMap;

public class ChessBoard {
    private Square[] board = new Square[64];
    public ChessBoard() {
        board = new Square[64];
        for (int x = 0; x < 8; x++) { //Fills white pawns
            Pawn thisPawn = new Pawn(new White(), new Coordinate(x, 1));
            Color thisColor;
            if (x % 2 == 0) {
                thisColor = new White();
            }
            else {
                thisColor = new Black();
            }
            board[x * 8 + 1] = new Square(thisColor, new Coordinate(x, 1), this);
        }
        for (int x = 0; x < 8; x++) { //Fills black pawns
            Pawn thisPawn = new Pawn(new Black(), new Coordinate(x, 6));
            Color thisColor;
            if (x % 2 == 0) {
                thisColor = new Black();
            }
            else {
                thisColor = new White();
            }
            board[x * 8 + 1] = new Square(thisColor, new Coordinate(x, 6), this);
        }



    }
    public ChessBoard(ChessBoard b) {
        board = new Square[64];
        System.arraycopy(b.board, 0, board, 0, b.board.length);

    }
    public boolean isKingChecked(ChessBoard board) {
        //TODO: IMPLEMENT
        return false;
    }

    public Square getSquare(Coordinate c) {
        return board[c.getX() * 8 + c.getY()];
    }
}
