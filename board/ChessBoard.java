package board;

import pieces.Piece;
import java.util.HashMap;

public class ChessBoard {
    private Square[] board = new Square[64];
    private HashMap<Square, Piece> whitePieces;
    private HashMap<Square, Piece> blackPieces;
    public ChessBoard() {

    }
    public boolean isKingChecked(ChessBoard board) {

    }

    public Square getSquare(Coordinate c) {
        return board[c.getX() * 8 + c.getY()];
    }
    public Piece getPiece(Coordinate c) {
        if (whitePieces.get(getSquare(c)) != null) {
            return whitePieces.get(getSquare(c));
        }
        return blackPieces.get(getSquare(c));
    }
    public Piece getPiece(Square s) {
        if (whitePieces.get(s) != null) {
            return whitePieces.get(s);
        }
        return blackPieces.get(s);
    }

}
