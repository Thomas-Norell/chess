package board;

import pieces.*;

import java.util.HashMap;

public class ChessBoard {
    private Square[] board = new Square[64];
    public ChessBoard() {
        board = new Square[64];
        for (int i = 0; i < 64; i++) {
            if (i % 2 == 0) {
                board[i] = new Square(new White(), new Coordinate(i % 8, i / 8), this);
            }
            else {
                board[i] = new Square(new Black(), new Coordinate(i % 8, i / 8), this);
            }
        }
        for (int x = 0; x < 8; x++) { //Fills white pawns
            Pawn thisPawn = new Pawn(new White(), new Coordinate(x, 1));
            board[1 * 8 + x].setOccupant(thisPawn);
        }

        Rook thisRook = new Rook(new White(), new Coordinate(0,0));
        board[0 * 8 + 0].setOccupant(thisRook);

        thisRook = new Rook(new White(), new Coordinate(7,0));
        board[0 * 8 + 7].setOccupant(thisRook);

        Knight thisKnight = new Knight(new White(), new Coordinate(1,0));
        board[0 * 8 + 1].setOccupant(thisKnight);

        thisKnight = new Knight(new White(), new Coordinate(6,0));
        board[0 * 8 + 6].setOccupant(thisKnight);


        Bishop thisBishop = new Bishop(new White(), new Coordinate(2,0));
        board[0 * 8 + 2].setOccupant(thisBishop);

        thisBishop = new Bishop(new White(), new Coordinate(5,0));
        board[0 * 8 + 5].setOccupant(thisBishop);

        Queen thisQueen = new Queen(new White(), new Coordinate(3, 0));
        board[0 * 8 + 3].setOccupant(thisQueen);

        King thisKing = new King(new White(), new Coordinate(4, 0));
        board[0 * 8 + 4].setOccupant(thisKing);




        for (int x = 0; x < 8; x++) { //Fills black pawns
            Pawn thisPawn = new Pawn(new Black(), new Coordinate(x, 6));
            board[6 * 8 + x].setOccupant(thisPawn);
        }

        thisRook = new Rook(new Black(), new Coordinate(0,7));
        board[7 * 8 + 0].setOccupant(thisRook);

        thisRook = new Rook(new Black(), new Coordinate(7,7));
        board[7 * 8 + 7].setOccupant(thisRook);

        thisKnight = new Knight(new Black(), new Coordinate(1,7));
        board[7 * 8 + 1].setOccupant(thisKnight);

        thisKnight = new Knight(new Black(), new Coordinate(6,7));
        board[7 * 8 + 6].setOccupant(thisKnight);

        thisBishop = new Bishop(new Black(), new Coordinate(2,7));
        board[7 * 8 + 2].setOccupant(thisBishop);

        thisBishop = new Bishop(new Black(), new Coordinate(5,7));
        board[7 * 8 + 5].setOccupant(thisBishop);

        thisQueen = new Queen(new Black(), new Coordinate(3, 7));
        board[7 * 8 + 3].setOccupant(thisQueen);

        thisKing = new King(new Black(), new Coordinate(4, 7));
        board[7 * 8 + 4].setOccupant(thisKing);
    }
    public ChessBoard(ChessBoard b) {
        board = new Square[64];
        System.arraycopy(b.board, 0, board, 0, b.board.length);

    }
    public boolean isKingChecked(ChessBoard board) {
        //TODO: IMPLEMENT
        return false;
    }

    public Square getSquare(Coordinate c) {
        if (c.getY() > 7 || c.getY() < 0 || c.getX() > 7 || c.getX() < 0) {
            return new Square(new White(), new Coordinate(100, 100), this);
        }
        return board[c.getY() * 8 + c.getX()];
    }
    public String moveNotate(Piece p, Square dest) {
        char[] map = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        String move = "";
        move += map[p.getCoordinate().getX()] + Integer.toString(p.getCoordinate().getY() + 1);
        move += " -> ";
        move += map[dest.getCoord().getX()] + Integer.toString(dest.getCoord().getY() + 1);
        return move;

    }
}
