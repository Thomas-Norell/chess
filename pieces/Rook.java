package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;
import javafx.scene.image.Image;

public class Rook extends Piece {


    public Rook(Color col, Coordinate c, ChessBoard b) {
        setCoordinate(c);
        setColor(col);
        setBoard(b);
    }

    @Override
    public Piece deepCopy(ChessBoard b) {
        return new Rook(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()), b);
    }

    static void checkAdd(Piece self, int direction, ArrayList<Square> moves, ChessBoard board) {
        int myX = self.getCoordinate().getX();
        int myY = self.getCoordinate().getY();
        for (int i = myX + direction; i < 8 - myX && i >= 0; i += direction) {
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
        for (int i = myY + direction; i < 8 - myX && i >= 0; i += direction) {
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
        ArrayList<Square> badMoves = new ArrayList<>();
        for (Square s : moves) {
            ChessBoard sim = new ChessBoard(board);
            Square aMove = sim.getSquare(s.getCoord());
            Piece aPiece = sim.getSquare(this.getCoordinate()).Occupant();
            aPiece.move(aMove);
            if (sim.isKingChecked(this.getColor())) {
                badMoves.add(s);
            }
        }
        for (Square s : badMoves) {
            moves.remove(s);
        }
        return moves;
    }

    public Image image() {
        if (getColor().isWhite()) {
            return new Image("images/whiteRook.png");
        }
        return new Image("images/blackRook.png");
    }
}
