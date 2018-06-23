package engine;

import board.*;
import pieces.Piece;

import java.util.ArrayList;

public class Heuristics {
    public static ArrayList<Piece> piecesAttacking(ChessBoard board, Color playerColor, ArrayList<Move> moves) { //returns a list of pieces that player is attacking
        ArrayList<Piece> pieces = new ArrayList();
        for (Move s : moves) {
            if (s.destination.isOccupied()) {
                pieces.add(s.destination.Occupant());
            }
        }
        return pieces;
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

    public static float value(ChessBoard board, Color player, ArrayList<Move> moves) {
        float worth = 0;
        for (Piece p : board.pieces(player)) { //Having pieces on the board is good
            worth += p.getValue();
        }
        worth += moves.size() * 0.05; //every square attacked is 0.1

        for (Piece p : piecesAttacking(board, player, moves)) { //Increase worth for attacking pieces
            worth += 0.1 * p.getValue();
        }

        return worth;
    }

    public static Color winner(ChessBoard board, ArrayList<Move> moves) {
        if (value(board, new White(), moves) > value(board, new Black(), moves)) {
            return new White();
        }
        return new Black();
    }
    private static double probBlack(ChessBoard board, MonteCarloTree.Node n) {
        if (board.isCheckMate(new White())) {
            return 1;
        }

        else if (board.isStaleMate(n.player.opposite(), n.movesLeft)) {
            return 0.5;
        }
        double bVal = value(board, new Black(), n.movesLeft);
        double wVal;
        if (!n.isRoot()) {
             wVal = value(board, new White(), n.parent.allMoves);
        }
        else {
            wVal = bVal;
        }

        double p = 0.5 * Math.pow(2.71, -(wVal - bVal) * (wVal - bVal) / 25);
        if (wVal > bVal) {
            return p;
        }
        else {
            return 1 - p;
        }
    }

    public static double probWin(ChessBoard board, Color player, MonteCarloTree.Node n) {
        if (player.isWhite()) {
            return 1 - probBlack(board, n);
        }
        return probBlack(board, n);
    }


}
