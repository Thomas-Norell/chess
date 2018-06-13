package game;

import board.*;
import engine.Thinker;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pieces.Piece;

public class Controller {
    ChessBoard board;
    Color playerColor;
    Color computerColor;
    Piece source;
    Square destination;
    Stage stage;
    public Controller(Color color, Stage stage) {
        playerColor = color;
        if (playerColor.isWhite()) {
            computerColor = new Black();
        }
        else {
            computerColor = new White();
        }
        board = new ChessBoard();
        this.stage = stage;
        Visualizer.renderBoard(stage, board, this);
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
                Thinker.makeMove(computerColor, board);
                if (playerColor.isWhite()) {
                    playerColor = new Black();
                }
                else {
                    playerColor = new White();
                }
                Visualizer.renderBoard(stage, board, this);
            }
            destination = null;

        }

    }
}
