package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;
import game.Visualizer;
import javafx.scene.image.Image;

public class Rook extends Piece {
    boolean hasMoved = false;


    public Rook(Color col, Coordinate c, ChessBoard b) {
        setCoordinate(c);
        setColor(col);
        setBoard(b);
    }

    @Override
    public double getValue() {
        return 5;
    }

    @Override
    public Piece deepCopy(ChessBoard b) {
        return new Rook(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()), b);
    }

    static void checkAdd(Piece self, int direction, ArrayList<Square> moves, ChessBoard board) {
        int myX = self.getCoordinate().getX();
        int myY = self.getCoordinate().getY();
        for (int i = myX + direction; i < 8 & i >= 0; i += direction) {
            Square thisSquare = board.getSquare(new Coordinate(i, myY));
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
        for (int i = myY + direction; i < 8 && i >= 0; i += direction) {
            Square thisSquare = board.getSquare(new Coordinate(myX, i));
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
        checkAdd(this, 1, moves, board);
        checkAdd(this, -1, moves, board);
        return moves;
    }

    public Image image() {
        if (getColor().isWhite()) {
            return new Image("images/whiteRook.png");
        }
        return new Image("images/blackRook.png");
    }
}
