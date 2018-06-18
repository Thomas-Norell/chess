package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;
import game.Visualizer;
import javafx.scene.image.Image;

public class Bishop extends Piece {

    @Override
    public double getValue() {
        return 3.1;
    }


    public Bishop(Color col, Coordinate c, ChessBoard board) {
        setCoordinate(c);
        setColor(col);
        setBoard(board);
    }

    @Override
    public Piece deepCopy(ChessBoard b) {
        return new Bishop(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()), b);
    }

    static void checkAdd(Piece self, int xDir, int yDir, ArrayList<Square> moves, ChessBoard board) {
        int myX = self.getCoordinate().getX();
        int myY = self.getCoordinate().getY();
        for (int x = myX + xDir,y = myY + yDir; x < 8 && y < 8 && x >= 0 && y >= 0; x += xDir, y += yDir) {
            Square thisSquare = board.getSquare(new Coordinate(x, y));
            if (thisSquare.isOccupied()) {
                if (thisSquare.Occupant().getColor().sameColor(self.getColor())) { //We followed a path to a piece of the same color
                    break;
                }
                else {
                    moves.add(thisSquare); //We can attack an enemy piece
                    break; //we can't jump over it
                }
            }
            moves.add(thisSquare);
        }

    }

    @Override
    public ArrayList<Square> validMoves() {
        ChessBoard board = this.getBoard();
        ArrayList<Square> moves = new ArrayList();
        checkAdd(this, 1, 1, moves, board);
        checkAdd(this, -1, 1, moves, board);
        checkAdd(this, 1, -1, moves, board);
        checkAdd(this, -1, -1, moves, board);
        return moves;
    }

    public Image image() {
        if (getColor().isWhite()) {
            return new Image("images/whiteBishop.png");
        }
        return new Image("images/blackBishop.png");
    }
}
