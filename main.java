import board.ChessBoard;
import board.Coordinate;
import board.Square;
import pieces.Piece;

public class main {
    public static void main(String[] args) {
        ChessBoard b = new ChessBoard();
        Piece source = b.getSquare(new Coordinate(3, 0)).Occupant();
        for (Square s : source.validMoves()) {
            System.out.println(b.moveNotate(source, s));
        }

        ChessBoard c = new ChessBoard(b);
        System.out.print(b.getSquare(new Coordinate(0,0)) == c.getSquare(new Coordinate(0,0))); //Ensuring a deep copy

    }
}
