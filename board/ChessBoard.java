package board;

import game.Visualizer;
import pieces.*;

import java.util.ArrayList;

public class ChessBoard {
    private Square[] board = new Square[64];
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    public ChessBoard() {
        whitePieces = new ArrayList();
        blackPieces = new ArrayList();
        int mode;
        for (int i = 0; i < 64; i++) {
            if ((i / 8) % 2 == 0) {
                mode = 0;
            }
            else {
                mode = 1;
            }

            if ((i % 8) % 2 == mode) {
                board[i] = new Square(new Black(), new Coordinate(i % 8, i / 8), this);
            }
            else {
                board[i] = new Square(new White(), new Coordinate(i % 8, i / 8), this);
            }


        }
        for (int x = 0; x < 8; x++) { //Fills white pawns
            Pawn thisPawn = new Pawn(new White(), new Coordinate(x, 1), this);
            board[1 * 8 + x].setOccupant(thisPawn);
            whitePieces.add(thisPawn);
        }

        Rook thisRook = new Rook(new White(), new Coordinate(0,0), this);
        board[0 * 8 + 0].setOccupant(thisRook);
        whitePieces.add(thisRook);

        thisRook = new Rook(new White(), new Coordinate(7,0), this);
        board[0 * 8 + 7].setOccupant(thisRook);
        whitePieces.add(thisRook);

        Knight thisKnight = new Knight(new White(), new Coordinate(1,0), this);
        board[0 * 8 + 1].setOccupant(thisKnight);
        whitePieces.add(thisKnight);

        thisKnight = new Knight(new White(), new Coordinate(6,0), this);
        board[0 * 8 + 6].setOccupant(thisKnight);
        whitePieces.add(thisKnight);

        Bishop thisBishop = new Bishop(new White(), new Coordinate(2,0), this);
        board[0 * 8 + 2].setOccupant(thisBishop);
        whitePieces.add(thisBishop);

        thisBishop = new Bishop(new White(), new Coordinate(5,0), this);
        board[0 * 8 + 5].setOccupant(thisBishop);
        whitePieces.add(thisBishop);

        Queen thisQueen = new Queen(new White(), new Coordinate(3, 0), this);
        board[0 * 8 + 3].setOccupant(thisQueen);
        whitePieces.add(thisQueen);

        King thisKing = new King(new White(), new Coordinate(4, 0), this);
        board[0 * 8 + 4].setOccupant(thisKing);
        whitePieces.add(thisKing);




        for (int x = 0; x < 8; x++) { //Fills black pawns
            Pawn thisPawn = new Pawn(new Black(), new Coordinate(x, 6), this);
            board[6 * 8 + x].setOccupant(thisPawn);
            blackPieces.add(thisPawn);
        }

        thisRook = new Rook(new Black(), new Coordinate(0,7), this);
        board[7 * 8 + 0].setOccupant(thisRook);
        blackPieces.add(thisRook);

        thisRook = new Rook(new Black(), new Coordinate(7,7), this);
        board[7 * 8 + 7].setOccupant(thisRook);
        blackPieces.add(thisRook);

        thisKnight = new Knight(new Black(), new Coordinate(1,7), this);
        board[7 * 8 + 1].setOccupant(thisKnight);
        blackPieces.add(thisKnight);

        thisKnight = new Knight(new Black(), new Coordinate(6,7), this);
        board[7 * 8 + 6].setOccupant(thisKnight);
        blackPieces.add(thisKnight);

        thisBishop = new Bishop(new Black(), new Coordinate(2,7), this);
        board[7 * 8 + 2].setOccupant(thisBishop);
        blackPieces.add(thisBishop);

        thisBishop = new Bishop(new Black(), new Coordinate(5,7), this);
        board[7 * 8 + 5].setOccupant(thisBishop);
        blackPieces.add(thisBishop);

        thisQueen = new Queen(new Black(), new Coordinate(3, 7), this);
        board[7 * 8 + 3].setOccupant(thisQueen);
        blackPieces.add(thisQueen);

        thisKing = new King(new Black(), new Coordinate(4, 7), this);
        board[7 * 8 + 4].setOccupant(thisKing);
        blackPieces.add(thisKing);
    }
    public ChessBoard(ChessBoard b) {
        board = new Square[64];
        int count = 0;
        for (Square s : b.board) {
            board[count] = new Square(s.getColor(), new Coordinate(s.getCoord().getX(), s.getCoord().getY()), this);
            count++;
        }
        whitePieces = new ArrayList();
        blackPieces = new ArrayList();
        for (Piece p : b.whitePieces) {
            Piece copy = p.deepCopy(this);
            whitePieces.add(copy);
            board[8*p.getCoordinate().getY() + p.getCoordinate().getX()].setOccupant(copy);

        }
        for (Piece p : b.blackPieces) {
            Piece copy = p.deepCopy(this);
            blackPieces.add(copy);
            board[8*p.getCoordinate().getY() + p.getCoordinate().getX()].setOccupant(copy);
        }

    }
    public boolean isKingChecked(Color player) {
        ArrayList<Piece> pieces;
        if (player.isWhite()) {
            pieces = this.whitePieces;

        }
        else {
            pieces = this.blackPieces;
        }
        for (Piece p : pieces) {
            if (p instanceof King) {
                return ((King) p).isChecked();
            }
        }
        Visualizer.renderBoard(this);
        throw new ArrayIndexOutOfBoundsException("I didn't find a king in your pieces, something is wrong!");
    }

    public Square getSquare(Coordinate c) {
        if (c.getY() > 7 || c.getY() < 0 || c.getX() > 7 || c.getX() < 0) {
            //throw new ArrayIndexOutOfBoundsException("I can't make a coordinate that big!");
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
    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public ArrayList<Piece> pieces(Color player) {
        if (player.isWhite()) {
            return whitePieces;
        }
        return blackPieces;
    }


}
