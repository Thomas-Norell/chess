package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;
import java.util.ArrayList;
import board.Color;
import javafx.scene.image.Image;

public class Knight extends Piece {
    final int direction;


    //TODO: En Passant
    public Knight(Color col, Coordinate c, ChessBoard b) {
        setCoordinate(c);
        setColor(col);
        setBoard(b);
        if (col.isWhite()) {
            direction = 1;
        }
        else {
            direction = -1;
        }
    }

    @Override
    public Piece deepCopy(ChessBoard b) {
        return new Knight(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()), b);
    }

    private void verifyAdd(ArrayList<Square> moves, Square s) {
        if (s.isValid()) {
            if (s.isOccupied()) {
                if (s.Occupant().getColor().sameColor(this.getColor())) {
                    return;
                }
            }
            moves.add(s);
        }
    }

    @Override
    public ArrayList<Square> validMoves() {
        ChessBoard board = this.getBoard();
        ArrayList<Square> potentials = new ArrayList();
        ArrayList<Square> moves = new ArrayList();
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() - 2, this.getCoordinate().getY() + 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() + 2)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 2, this.getCoordinate().getY() + 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() + 2)));


        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 2, this.getCoordinate().getY() - 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() - 2)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() - 2, this.getCoordinate().getY() - 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() - 2)));

        for (Square s : potentials) {
            verifyAdd(moves, s);
        }
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
            return new Image("images/whiteKnight.png");
        }
        return new Image("images/blackKnight.png");
    }

}
