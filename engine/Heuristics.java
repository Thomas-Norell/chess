package engine;

import board.*;
import pieces.Piece;

import java.util.ArrayList;

public class Heuristics {
    public static ArrayList<Piece> piecesAttacking(ChessBoard board, Color playerColor) { //returns a list of pieces that player is attacking
        ArrayList<Square> squares = squaresAttacking(board, playerColor);
        ArrayList<Piece> pieces = new ArrayList();
        for (Square s : squares) {
            if (s.isOccupied()) {
                pieces.add(s.Occupant());
            }
        }
        return pieces;
    }
    public static ArrayList<Square> squaresAttacking(ChessBoard board, Color playerColor) {
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

    public static ArrayList<Move> allMoves(ChessBoard board, Color player) {
        ArrayList<Move> moves = new ArrayList<>();
        for (Piece p : board.pieces(player)) {
            for (Square s : p.validMoves()) {
                ChessBoard sim = new ChessBoard(board);
                sim.getSquare(p.getCoordinate()).Occupant().move(sim.getSquare(s.getCoord()));
                if (!sim.isKingChecked(player)) {
                    moves.add(new Move(p, s));
                }

            }
        }
        return moves;
    }

    public static float value(ChessBoard board, Color player) {
        float worth = 0;
        for (Piece p : board.pieces(player)) { //Having pieces on the board is good
            worth += p.getValue();
        }
        /*for (Piece p : board.pieces(player.opposite())) {
            worth -= p.getValue();
        }*/
        worth += squaresAttacking(board, player).size() * 0.05; //every square attacked is 0.1

        for (Piece p : piecesAttacking(board, player)) { //Increase worth for attacking pieces
            worth += 0.1 * p.getValue();
        }
        /*for (Piece p : piecesAttacking(board, player.opposite())) { //Decrease worth if piece is under attack
            worth -= 0.2 * p.getValue();
        }*/

        if (board.isCheckMate(player)) {
            worth += 600;
        }

        return worth;
    }

    public static Color winner(ChessBoard board) {
        if (value(board, new White()) > value(board, new Black())) {
            return new White();
        }
        return new Black();
    }
    private static double probBlack(ChessBoard board) {
        double bVal = value(board, new Black());
        double wVal = value(board, new White());
        double p = 0.5 * Math.pow(2.71, -(wVal - bVal) * (wVal - bVal) / 50);
        if (wVal > bVal) {
            return p;
        }
        else {
            return 1 - p;
        }
    }

    public static double probWin(ChessBoard board, Color player) {
        if (player.isWhite()) {
            return 1 - probBlack(board);
        }
        return probBlack(board);
    }

}
