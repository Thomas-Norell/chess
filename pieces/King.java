package pieces;

import board.ChessBoard;
import board.Coordinate;
import board.Square;

import java.sql.SQLRecoverableException;
import java.util.ArrayList;
import board.Color;
import engine.Heuristics;
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
                if (s.Occupant() instanceof Bishop || s.Occupant() instanceof Queen) {
                    return true;
                }

            }
        }


        if (xD == 1 && yD == 1) {
            return (checkDiagHelper(-1, -1) || checkDiagHelper(-1, 1) || checkDiagHelper(1, -1));
        }

        return false;
    }
    private boolean checkKing() {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y ++) {
                Coordinate coord = new Coordinate(this.getCoordinate().getX() + x, this.getCoordinate().getY() + y);
                if (coord.getX() < 8 && coord.getX() >=0 && coord.getY() < 8 && coord.getY() >= 0) {
                    if (getBoard().getSquare(coord).isOccupied() && !getBoard().getSquare(coord).Occupant().getColor().sameColor(getColor()) && getBoard().getSquare(coord).Occupant() instanceof King) {
                        return true;
                    }
                }
            }
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
            if (c.getX() > 7 || c.getX() < 0 || c.getY() > 7 || c.getY() < 0) {
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
            if (c.getX() > 7 || c.getX() < 0 || c.getY() >7 || c.getY() < 0) {
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

        return (checkStraightHelper(1) || checkDiagHelper(1,1) || checkKnight() || checkPawn()) || checkKing();

    }

    public Image image() {
        if (getColor().isWhite()) {
            return new Image("images/whiteKing.png");
        }
        return new Image("images/blackKing.png");
    }
}
