package game;

import board.*;
import engine.Heuristics;
import engine.MonteCarloTree;
import javafx.stage.Stage;
import pieces.Piece;

public class Controller {
    private final double strength = 60;
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
                    source.move(destination);
                    vis.update(board, new Move(source, destination));
                    if (board.isCheckMate(playerColor)) {
                        vis.endGame(playerColor);
                    }
                    return new MonteCarloTree(board, playerColor, strength).bestMove();
                }
            }
            destination = null;

        }
        return null;

    }
}
