package game;

import board.*;
import engine.Heuristics;
import engine.MonteCarloTree;
import javafx.application.Platform;
import javafx.stage.Stage;
import pieces.Piece;

public class Controller {
    private double strength = 30;
    ChessBoard board;
    Color playerColor;
    Piece source;
    Square destination;
    Stage stage;
    public Controller(Color color, Stage stage, Visualizer vis) {
        playerColor = color;
        board = new ChessBoard();
        this.stage = stage;
        vis.tree = new MonteCarloTree(new ChessBoard(), new Black(), strength);
        vis.renderBoard(board, this, stage);


    }
    public void setStrength(double strength) {
        this.strength = strength;
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
                    vis.ponder.suspend();
                    vis.tree.setStrength(strength);

                    vis.tree.advance(m);
                    source.move(destination);
                    Platform.runLater(vis.setUpdate(board, m));
                    if (board.isCheckMate(playerColor.opposite()) || board.isDraw(playerColor.opposite(), vis.tree)) {
                        Platform.runLater(vis.end(playerColor.opposite()));
                    }

                    Move best = vis.tree.bestMove();

                    vis.tree.advance(best);
                    best = (new Move(board.getSquare(best.source.getCoordinate()).Occupant(), board.getSquare(best.destination.getCoord())));
                    best.makeMove();
                    Platform.runLater(vis.setUpdate(board, best));

                    vis.ponder.resume(vis.tree);
                    if (board.isCheckMate(playerColor) || board.isDraw(playerColor.opposite(), vis.tree)) {
                        Platform.runLater(vis.end(playerColor.opposite()));
                    }
                    return best;

                }
            }
            destination = null;

        }
        return null;

    }
}
