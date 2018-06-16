package game;

import board.*;
import engine.MonteCarloTree;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pieces.Piece;

public class Controller {
    private final int strength = 4000000;
    ChessBoard board;
    Color playerColor;
    Piece source;
    Square destination;
    Stage stage;
    public Controller(Color color, Stage stage) {
        playerColor = color;
        board = new ChessBoard();

        if (!playerColor.isWhite()) {
            Move m = new MonteCarloTree(board, playerColor.opposite(), strength).bestMove();
            m.source.move(m.destination);
        }
        this.stage = stage;
        Visualizer.renderBoard(board, this, stage);

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
                Visualizer.renderBoard(board, this, stage);
            }
            destination = null;

        }

    }
}
