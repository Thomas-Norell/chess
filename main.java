import board.ChessBoard;
import board.Coordinate;
import board.Square;
import pieces.Piece;

public class main {
    public static void main(String[] args) {
        ChessBoard b = new ChessBoard();
        Piece source = b.getSquare(new Coordinate(1, 7)).Occupant();
        for (Square s : source.validMoves(b)) {
            System.out.println(b.moveNotate(source, s));
        }

    }
}
