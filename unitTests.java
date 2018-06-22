import board.*;
import engine.Heuristics;
import engine.MonteCarloTree;
import game.Visualizer;
import javafx.stage.Stage;
import org.junit.Test;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import javafx.application.Application;


import javax.management.monitor.MonitorSettingException;
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
        assertEquals(expected, source.validMoves());
    }

    @Test
    public void checkColor(){

        ChessBoard b = new ChessBoard();
        assertFalse(b.getSquare(new Coordinate(0,0)).getColor().isWhite());
        assertTrue(b.getSquare(new Coordinate(1,0)).getColor().isWhite());
    }

    @Test
    public void deepBoardCopy(){
        ChessBoard b = new ChessBoard();
        ChessBoard c = new ChessBoard(b);
        assertNotEquals(b.getSquare(new Coordinate(0,0)), c.getSquare(new Coordinate(0,0))); //Ensuring a deep copy of board
        assertNotEquals(b.getSquare(new Coordinate(3,0)).Occupant(), c.getSquare(new Coordinate(3,0)).Occupant());  //Ensuring deep piece copy

    }

    @Test
    public void testMove() {
        ChessBoard b = new ChessBoard();
        Pawn kingPawn = (Pawn) b.getSquare(new Coordinate(4, 1)).Occupant();
        kingPawn.move(b.getSquare(new Coordinate(4, 3)));
        assertNull(b.getSquare(new Coordinate(4, 1)).Occupant());
        assertEquals(kingPawn, b.getSquare(new Coordinate(4, 3)).Occupant());
    }

    @Test
    public void testCapture() {
        ChessBoard b = new ChessBoard();
        Pawn kingPawn = (Pawn) b.getSquare(new Coordinate(4, 1)).Occupant();
        Pawn blackQueenPawn = (Pawn) b.getSquare(new Coordinate(3, 6)).Occupant();
        kingPawn.move(b.getSquare(new Coordinate(4, 3)));
        blackQueenPawn.move(b.getSquare(new Coordinate(3, 4)));
        Square dest = b.getSquare(blackQueenPawn.getCoordinate());
        assertTrue(kingPawn.validMoves().contains(b.getSquare(blackQueenPawn.getCoordinate()))); //Checking if pawn recognizes capture as valid move
        kingPawn.move(b.getSquare(blackQueenPawn.getCoordinate()));
        assertFalse(blackQueenPawn.isAlive());
        assertEquals(kingPawn, dest.Occupant());

    }
    /*@Test
    public void testMonte() {
        ChessBoard b = new ChessBoard();
        Pawn whiteKingPawn = (Pawn) b.getSquare(new Coordinate(4, 1)).Occupant();
        whiteKingPawn.move(b.getSquare(new Coordinate(4, 3)));
        Pawn blackKingPawn = (Pawn) b.getSquare(new Coordinate(6, 6)).Occupant();
        blackKingPawn.move(b.getSquare(new Coordinate(6, 5)));
        Queen whiteQueen = (Queen) b.getSquare(new Coordinate(3,0)).Occupant();
        whiteQueen.move(b.getSquare(new Coordinate(7, 4)));
        MonteCarloTree decision = new MonteCarloTree(b, new Black(), 2.5);
        assertEquals(blackKingPawn, decision.bestMove().source);
    }*/

    @Test
    public void testPromote() {
        ChessBoard b = new ChessBoard();
        Pawn whiteBish = (Pawn) b.getSquare(new Coordinate(5, 1)).Occupant();
        whiteBish.move(b.getSquare(new Coordinate(7, 7)));
        assertTrue(b.getSquare(new Coordinate(7, 7)).Occupant() instanceof Queen);

    }

    public Color testGame() {
        ChessBoard b = new ChessBoard();
        ChessBoard c = new ChessBoard();
        MonteCarloTree white = new MonteCarloTree(b, new Black(), 0.5);
        MonteCarloTree black = new MonteCarloTree(c, new Black(), 0.25);
        Move best;
        while (!b.isCheckMate(new White()) || !b.isCheckMate(new Black())) {
            //System.out.println(Heuristics.probWin(b, new Black()));
            best = white.bestMove();
            best = new Move(b.getSquare(best.source.getCoordinate()).Occupant(), b.getSquare(best.destination.getCoord()));
            white.advance(best);
            black.advance(best);
            /*
            System.out.println("(" + (Integer.toString(best.source.getCoordinate().getX())

                    + ", "+Integer.toString(best.source.getCoordinate().getY())
                    + ") -> " + "(" + Integer.toString(best.destination.getCoord().getX())
                    + ", " + Integer.toString(best.destination.getCoord().getY()) + ")"));
                    */
            best.makeMove();

            if (b.isCheckMate(new Black())) {
                System.out.println("White win!");
                return new White();
            }
            if (b.isDraw()) {
                System.out.println("It's a Draw!");
                return null;

            }
            best = black.bestMove();
            best = new Move(b.getSquare(best.source.getCoordinate()).Occupant(), b.getSquare(best.destination.getCoord()));
            black.advance(best);
            white.advance(best);
            /*
            System.out.println("(" + (Integer.toString(best.source.getCoordinate().getX())
                    + ", "+Integer.toString(best.source.getCoordinate().getY())
                    + ") -> " + "(" + Integer.toString(best.destination.getCoord().getX())
                    + ", " + Integer.toString(best.destination.getCoord().getY()) + ")"));
                    */
            best.makeMove();
            if (b.isCheckMate(new White())) {
                System.out.println("Black win!");
                return new Black();
            }
            if (b.isDraw()) {
                System.out.println("It's a draw!");
                return null;
            }
        }
        return null;



    }
    @Test
    public void testManyGames() {
        double whiteWins = 0;
        double blackWins = 0;
        double draws = 0;
        for (int x = 0; x < 10; x++) {
            Color result = testGame();
            if (result == null) {
                draws += 1;
            }
            else if (result.isWhite()) {
                whiteWins++;
            }
            else {
                blackWins++;
            }

        }
        System.out.println("White: " + Double.toString(whiteWins));
        System.out.println("Black: " + Double.toString(blackWins));
        System.out.println("Draws: " + Double.toString(draws));


    }

    public static void main(String[] args) {
        //Application.launch(args);
    }

    public void start(Stage stage) {
        //testFoolsMate();
    }


}
