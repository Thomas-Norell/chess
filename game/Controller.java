package game;

import board.*;
import engine.MonteCarloTree;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pieces.Piece;

public class Controller {
    private final int strength = 10000;
    ChessBoard board;
    Color playerColor;
    Piece source;
    Square destination;
    Stage stage;
    Visualizer visual;
    public Controller(Color color, Visualizer visual) {
        playerColor = color;
        board = new ChessBoard();
        this.stage = stage;

        if (!playerColor.isWhite()) {
            Move m = new MonteCarloTree(board, playerColor.opposite(), strength).bestMove();
            m.source.move(m.destination);
        }
        this.visual = visual;
        visual.renderBoard(board, this);

    }

    public void addTarget(Square target, GridPane gridPane) {
        if (!target.isValid()) {
            return;
        }
        if (target.isOccupied() && target.Occupant().getColor().sameColor(playerColor)) {
                source = target.Occupant();
        }
        /*if (target.isOccupied()) {
            source = target.Occupant();
        }*/
        else if (source != null) {
            destination = target;
            if (source.validMoves().contains(destination)) {
                //TODO: check if king is in check
                source.move(destination);
                //TODO: Burning down and rebuilding javaFX everytime is slow and ugly
                Move m = new MonteCarloTree(board, playerColor.opposite(), strength).bestMove();
                m.source.move(m.destination);
                visual.renderBoard(board, this);
            }
            destination = null;

        }

    }
}
