package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;
import javafx.scene.image.Image;

public class Queen extends Piece {
    static final Image wImage = new Image("images/whiteQueen.png");
    static final Image bImage = new Image("images/blackQueen.png");


    public Queen(Color col, Coordinate c, ChessBoard b) {
        setCoordinate(c);
        setColor(col);
        setBoard(b);
    }

    @Override
    public double getValue() {
        return 10;
    }

    @Override
    public Piece deepCopy(ChessBoard b) {
        return new Queen(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()), b);
    }



    @Override
    public ArrayList<Square> validMoves() {
        ChessBoard board = this.getBoard();
        ArrayList<Square> moves = new ArrayList();
        Rook.checkAdd(this, 1, moves, board);
        Rook.checkAdd(this, -1, moves, board);
        Bishop.checkAdd(this, 1, 1, moves, board);
        Bishop.checkAdd(this, -1, 1, moves, board);
        Bishop.checkAdd(this, 1, -1, moves, board);
        Bishop.checkAdd(this, -1, -1, moves, board);
        return moves;
    }

    public Image image() {
        if (getColor().isWhite()) {
            return wImage;
        }
        return bImage;
    }
}
