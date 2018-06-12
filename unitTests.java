import board.ChessBoard;
import board.Coordinate;
import board.Square;
import org.junit.Test;
import pieces.Pawn;
import pieces.Piece;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class unitTests {

    @Test
    public void fullTest(){

        ChessBoard b = new ChessBoard();
        ArrayList<Square> expected = new ArrayList();
        expected.add(b.getSquare(new Coordinate(3, 2)));
        expected.add(b.getSquare(new Coordinate(3, 3)));
        Piece source = b.getSquare(new Coordinate(3, 1)).Occupant();
        assertEquals(expected, source.validMoves(b));
    }

    @Test
    public void deepBoardCopy(){
        ChessBoard b = new ChessBoard();
        ChessBoard c = new ChessBoard(b);
        assertNotEquals(b.getSquare(new Coordinate(0,0)), c.getSquare(new Coordinate(0,0))); //Ensuring a deep copy of board
        assertNotEquals(b.getSquare(new Coordinate(3,0)).Occupant(), c.getSquare(new Coordinate(3,0)).Occupant());  //Ensuring deep piece copy

    }

    @Test
    public void testMode() {
        ChessBoard b = new ChessBoard();
        Pawn kingPawn = (Pawn) b.getSquare(new Coordinate(4, 1)).Occupant();
        kingPawn.move(b.getSquare(new Coordinate(4, 3)));
        assertNull(b.getSquare(new Coordinate(4, 1)).Occupant());
        assertEquals(kingPawn, b.getSquare(new Coordinate(4, 3)).Occupant());
    }
}
