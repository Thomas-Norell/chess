package game;

import board.*;
import engine.Heuristics;
import engine.MonteCarloTree;
import javafx.stage.Stage;
import pieces.Piece;
import pieces.King;

public class Controller {
    private final double strength = 1;
    ChessBoard board;
    Color playerColor;
    Piece source;
    Square destination;
    Stage stage;
    public Controller(Color color, Stage stage, Visualizer vis) {
        playerColor = color;
        board = new ChessBoard();

        if (!playerColor.isWhite()) {
            Move m = new MonteCarloTree(board, playerColor.opposite(), strength).bestMove();
            m.source.move(m.destination);
        }
        this.stage = stage;
        vis.tree = new MonteCarloTree(board, new Black(), strength);
        vis.renderBoard(board, this, stage);


    }

    public Move addTarget(Square target, Visualizer vis) {
        if (!target.isValid()) {
            return null;
        }
        if (target.isOccupied() && target.Occupant().getColor().sameColor(playerColor)) {
                source = target.Occupant();
        }
        else if (source != null) {
            destination = target;
            for (Move m : Heuristics.allMoves(board, playerColor)) {
                if (m.source == source && m.destination == destination) {
                    if (m.source instanceof King && Math.abs(m.source.getCoordinate().getX() - m.destination.getCoord().getX()) > 1) {
                        m.isCastle = true;
                    }
                    vis.tree.advance(m);
                    source.move(destination);
                    vis.update(board, m);
                    if (board.isCheckMate(playerColor.opposite())) {
                        vis.endGame(playerColor);
                    }
                    Move best = vis.tree.bestMove();
                    vis.tree.advance(best);
                    best = (new Move(board.getSquare(best.source.getCoordinate()).Occupant(), board.getSquare(best.destination.getCoord())));

                    best.makeMove();

                    //best = new Move(board.getSquare(best.source.getCoordinate()).Occupant(), board.getSquare(best.destination.getCoord()));


                    vis.update(board, best);



                    if (board.isCheckMate(playerColor)) {
                        vis.endGame(playerColor.opposite());
                    }

                    return best;
                }
            }
            destination = null;

        }
        return null;

    }
}
