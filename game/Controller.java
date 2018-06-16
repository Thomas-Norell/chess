package game;

import board.*;
import engine.MonteCarloTree;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pieces.Piece;

public class Controller {
    private final int strength = 20000;
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
        /*if (target.isOccupied()) {
            source = target.Occupant();
        }*/
        else if (source != null) {
            destination = target;
            if (source.validMoves().contains(destination)) {
                source.move(destination);
                vis.update(board, new Move(source, destination));
                /*Move m = new MonteCarloTree(board, playerColor.opposite(), strength).bestMove();
                m.source.move(m.destination);
                vis.update(board);*/
                //there are other methods such as positioning mouse and mouseclicks etc.
                return new MonteCarloTree(board, playerColor.opposite(), strength).bestMove();
                //Visualizer.renderBoard(board, this, stage);
            }
            destination = null;

        }
        return null;

    }
}
