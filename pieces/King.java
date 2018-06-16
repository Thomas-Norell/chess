package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;

import java.sql.SQLRecoverableException;
import java.util.ArrayList;
import board.Color;
import javafx.scene.image.Image;

public class King extends Piece {

    @Override
    public double getValue() {
        return 1;
    }


    public King(Color col, Coordinate c, ChessBoard b) {
        setCoordinate(c);
        setColor(col);
        setBoard(b);
    }

    @Override
    public Piece deepCopy(ChessBoard b) {
        return new King(this.getColor(), new Coordinate(this.getCoordinate().getX(), this.getCoordinate().getY()), b);
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
        //TODO: Castle
        ChessBoard board = this.getBoard();
        ArrayList<Square> potentials = new ArrayList();
        ArrayList<Square> moves = new ArrayList();
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() + 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() + 0)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() - 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 0, this.getCoordinate().getY() + 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 0, this.getCoordinate().getY() - 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() + 1)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() + 0)));
        potentials.add(board.getSquare(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() - 1)));

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

    private boolean checkStraightHelper(int direction) {
        for (int y = this.getCoordinate().getY() + direction; y < 8 && y >= 0; y += direction) {
            Square s = getBoard().getSquare(new Coordinate(this.getCoordinate().getX(), y));
            if (s.isOccupied()) {
                if (s.Occupant().getColor().sameColor(this.getColor())) {
                    break;
                }
                if (s.Occupant() instanceof Rook || s.Occupant() instanceof Queen) {
                    return true;
                }

            }
        }

        for (int x = this.getCoordinate().getX() + direction; x < 8 && x >= 0; x += direction) {
            Square s = getBoard().getSquare(new Coordinate(x, this.getCoordinate().getY()));
            if (s.isOccupied()) {
                if (s.Occupant().getColor().sameColor(this.getColor())) {
                    break;
                }
                if (s.Occupant() instanceof Rook || s.Occupant() instanceof Queen) {
                    return true;
                }

            }
        }

        if (direction == 1) {
            return checkStraightHelper(-1);
        }

        return false;
    }

    private boolean checkDiagHelper(int xD, int yD) {
        for (int y = this.getCoordinate().getY() + yD, x = this.getCoordinate().getX() + xD; y < 8 && y >= 0 && x < 8 && x >= 0; y += yD, x += xD) {
            Square s = getBoard().getSquare(new Coordinate(x, y));
            if (s.isOccupied()) {
                if (s.Occupant().getColor().sameColor(this.getColor())) {
                    break;
                }
                if (s.Occupant() instanceof Rook || s.Occupant() instanceof Queen) {
                    return true;
                }

            }
        }


        if (xD == 1 && yD == 1) {
            return (checkDiagHelper(-1, -1) || checkDiagHelper(-1, 1) || checkDiagHelper(1, -1));
        }

        return false;
    }

    private boolean checkKnight() { //Also checks pawn
        ArrayList<Coordinate> moves = new ArrayList();
        moves.add(new Coordinate(this.getCoordinate().getX() - 2, this.getCoordinate().getY() - 1));
        moves.add(new Coordinate(this.getCoordinate().getX() - 2, this.getCoordinate().getY() + 1));
        moves.add(new Coordinate(this.getCoordinate().getX() + 2, this.getCoordinate().getY() - 1));
        moves.add(new Coordinate(this.getCoordinate().getX() + 2, this.getCoordinate().getY() + 1));

        moves.add(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() - 2));
        moves.add(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() + 2));
        moves.add(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() - 2));
        moves.add(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() + 2));

        ArrayList<Coordinate> bads = new ArrayList();
        for (Coordinate c : moves) {
            if (c.getX() > 7 || c.getX() <=0 || c.getY() >7 || c.getY() <= 0) {
                bads.add(c);
            }
        }
        moves.removeAll(bads);

        for (Coordinate c : moves) {
            if (getBoard().getSquare(c).isOccupied() && !getBoard().getSquare(c).Occupant().getColor().sameColor(getColor()) && getBoard().getSquare(c).Occupant() instanceof Knight) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPawn() {
        ArrayList<Coordinate> moves = new ArrayList();
        if (getColor().isWhite()) {
            moves.add(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() + 1));
            moves.add(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() + 1));

        }
        else {
            moves.add(new Coordinate(this.getCoordinate().getX() + 1, this.getCoordinate().getY() - 1));
            moves.add(new Coordinate(this.getCoordinate().getX() - 1, this.getCoordinate().getY() - 1));
        }

        ArrayList<Coordinate> bads = new ArrayList();
        for (Coordinate c : moves) {
            if (c.getX() > 7 || c.getX() <=0 || c.getY() >7 || c.getY() <= 0) {
                bads.add(c);
            }
        }
        moves.removeAll(bads);

        for (Coordinate c : moves) {
            if (getBoard().getSquare(c).isOccupied() && !getBoard().getSquare(c).Occupant().getColor().sameColor(getColor()) && getBoard().getSquare(c).Occupant() instanceof Pawn) {
                return true;
            }
        }
        return false;
    }


    public boolean isChecked() {

        return (checkStraightHelper(1) || checkDiagHelper(1,1) || checkKnight() || checkPawn());

    }

    public Image image() {
        if (getColor().isWhite()) {
            return new Image("images/whiteKing.png");
        }
        return new Image("images/blackKing.png");
    }
}
