package game;

import board.ChessBoard;
import board.Color;
import javafx.stage.Stage;
import pieces.Piece;
import board.Square;

public class Controller {
    ChessBoard board;
    Color playerColor;
    Piece source;
    Square destination;
    Stage stage;
    public Controller(Color color, Stage stage) {
        playerColor = color;
        board = new ChessBoard();
        this.stage = stage;
        Visualizer.renderBoard(stage, board, this);
    }
    public void addTarget(Square target) {
        if (!target.isValid()) {
            return;
        }
        if (target.isOccupied() && target.Occupant().getColor().sameColor(playerColor)) {
                source = target.Occupant();
        }
        else if (source != null) {
            destination = target;
            if (source.validMoves().contains(destination)) {
                //TODO: check if king is in check
                source.move(destination);
                //TODO: Burning down and rebuilding javaFX everytime is slow and ugly
                Visualizer.renderBoard(stage, board, this);
            }

        }

    }
}
