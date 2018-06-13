package engine;

import board.ChessBoard;
import board.Square;
import pieces.Piece;
import board.Color;

import java.util.ArrayList;

public class Heuristics {
    public static ArrayList<Piece> piecesAttacking(Color playerColor, ChessBoard board) { //returns a list of pieces that player is attacking
        ArrayList<Square> squares = squaresAttacking(playerColor, board);
        ArrayList<Piece> pieces = new ArrayList();
        for (Square s : squares) {
            if (s.isOccupied()) {
                pieces.add(s.Occupant());
            }
        }
        return pieces;
    }
    public static ArrayList<Square> squaresAttacking(Color playerColor, ChessBoard board) {
        ArrayList<Piece> myPieces;
        if (playerColor.isWhite()) {
            myPieces = board.getWhitePieces();
        }
        else {
            myPieces = board.getBlackPieces();
        }

        ArrayList<Square> moves = new ArrayList();
        for (Piece p : myPieces) {
            for (Square s : p.validMoves()) {
                moves.add(s);
            }
        }
        return moves;
    }
}
