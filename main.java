import board.*;
import engine.MonteCarloTree;
import game.Visualizer;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Test;
import pieces.*;
import evaluation.PositionEval;

import java.util.ArrayList;


public class main extends Application{
    @Test
    public void testMonte() {
        ChessBoard b = new ChessBoard();
        Pawn whiteKingPawn = (Pawn) b.getSquare(new Coordinate(4, 1)).Occupant();
        whiteKingPawn.move(b.getSquare(new Coordinate(4, 3)));
        Pawn blackKingPawn = (Pawn) b.getSquare(new Coordinate(6, 6)).Occupant();
        blackKingPawn.move(b.getSquare(new Coordinate(6, 5)));
        Queen whiteQueen = (Queen) b.getSquare(new Coordinate(3,0)).Occupant();
        whiteQueen.move(b.getSquare(new Coordinate(7, 4)));
        MonteCarloTree decision = new MonteCarloTree(b, new Black(), 200);
        Move m = decision.bestMove();
        m.makeMove();

    }

    @Test()
    public void testWeird() {
        ChessBoard b = new ChessBoard();
        Pawn whiteKingPawn = (Pawn) b.getSquare(new Coordinate(4, 1)).Occupant();
        whiteKingPawn.move(b.getSquare(new Coordinate(4, 3)));

        Pawn blackKingPawn = (Pawn) b.getSquare(new Coordinate(5, 6)).Occupant();
        blackKingPawn.move(b.getSquare(new Coordinate(5, 5)));

        Knight whiteKnight = (Knight) b.getSquare(new Coordinate(6, 0)).Occupant();
        whiteKnight.move(b.getSquare(new Coordinate(5, 2)));


        Queen blackQueen = (Queen) b.getSquare(new Coordinate(3, 7)).Occupant();
        blackQueen.move(b.getSquare(new Coordinate(3, 6)));

        Pawn whiteBishopPawn = (Pawn) b.getSquare(new Coordinate(2, 1)).Occupant();
        whiteBishopPawn.move(b.getSquare(new Coordinate(2, 3)));


        MonteCarloTree decision = new MonteCarloTree(b, new Black(), 500);
        Move m = decision.bestMove();
        m.makeMove();

    }

    @Test()
    public void testOpen() {
        ChessBoard b = new ChessBoard();
        Pawn whiteQueenPawn = (Pawn) b.getSquare(new Coordinate(4, 1)).Occupant();
        whiteQueenPawn.move(b.getSquare(new Coordinate(4, 3)));
        MonteCarloTree decision = new MonteCarloTree(b, new Black(), 500);
        Move m = decision.bestMove();
        m.makeMove();

    }

    @Test()
    public static void testOpen2() {
        ChessBoard b = new ChessBoard();
        Pawn whiteQueenPawn = (Pawn) b.getSquare(new Coordinate(3, 1)).Occupant();
        whiteQueenPawn.move(b.getSquare(new Coordinate(3, 3)));

        Knight blackKnight = (Knight) b.getSquare(new Coordinate(1, 7)).Occupant();
        blackKnight.move(b.getSquare(new Coordinate(2, 5)));

        Knight whiteKnight = (Knight) b.getSquare(new Coordinate(6, 0)).Occupant();
        whiteKnight.move(b.getSquare(new Coordinate(5, 2)));

        MonteCarloTree decision = new MonteCarloTree(b, new Black(), 500);
        Move m = decision.bestMove();
        m.makeMove();

    }
    @Test()
    public static void testFork() {
        ChessBoard b = new ChessBoard();
        Pawn whiteQueenPawn = (Pawn) b.getSquare(new Coordinate(3, 1)).Occupant();
        whiteQueenPawn.move(b.getSquare(new Coordinate(1, 4)));

        b.getSquare(new Coordinate(3, 7)).Occupant().kill();
        b.getSquare(new Coordinate(3,7)).setOccupant(null);

        Bishop whiteBishop = (Bishop) b.getSquare(new Coordinate(2, 0)).Occupant();
        whiteBishop.move(b.getSquare(new Coordinate(3, 1)));

        Queen whiteQueen = (Queen) b.getSquare(new Coordinate(3,0)).Occupant();
        whiteQueen.kill();
        b.getSquare(new Coordinate(3, 0)).setOccupant(null);

        King whiteKing = (King) b.getSquare(new Coordinate(4, 0)).Occupant();
        whiteKing.move(b.getSquare(new Coordinate(3, 0)));

        Knight blackKnight = (Knight) b.getSquare(new Coordinate(1, 7)).Occupant();
        blackKnight.move(b.getSquare(new Coordinate(2, 1)));


        MonteCarloTree decision = new MonteCarloTree(b, new Black(), 100);
        Move m = decision.bestMove();
        m.makeMove();

    }
    public static void main(String[] args) {
        //testFork();
       Application.launch(args);
    }
    @Test
    public void testPromote() {
        ChessBoard b = new ChessBoard();
        Pawn whiteBish = (Pawn) b.getSquare(new Coordinate(5, 1)).Occupant();
        whiteBish.move(b.getSquare(new Coordinate(7, 7)));

    }
    public Color testGame(double cConstant) {
        ChessBoard b = new ChessBoard();
        ChessBoard c = new ChessBoard();
        MonteCarloTree white = new MonteCarloTree(b, new Black(), 1);
        MonteCarloTree black = new MonteCarloTree(c, new Black(), 1);
        //black.setC(cConstant);
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
                //System.out.println("White wins!");
                return new White();
            }
            if (b.isDraw(new Black(), black)) {
                //System.out.println("It's a Draw!");
                return null;

            }
            best = black.bestMove();
            best = new Move(b.getSquare(best.source.getCoordinate()).Occupant(), b.getSquare(best.destination.getCoord()));
            black.advance(best);
            white.advance(best);

            /*System.out.println("(" + (Integer.toString(best.source.getCoordinate().getX())
                    + ", "+Integer.toString(best.source.getCoordinate().getY())
                    + ") -> " + "(" + Integer.toString(best.destination.getCoord().getX())
                    + ", " + Integer.toString(best.destination.getCoord().getY()) + ")"));
*/
            best.makeMove();
            if (b.isCheckMate(new White())) {
                //System.out.println("Black wins!");
                return new Black();
            }
            if (b.isDraw(new White(), white)) {
                //System.out.println("It's a draw!");
                return null;
            }
        }
        return null;



    }
    @Test
    public double testManyGames(double cConstant) {
        double whiteWins = 0;
        double blackWins = 0;
        double draws = 0;
        for (int x = 0; x < 10; x++) {
            Color result = testGame(cConstant);
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
        System.out.println("Results for c = " + Double.toString(cConstant));
        System.out.println("White: " + Double.toString(whiteWins));
        System.out.println("Black: " + Double.toString(blackWins));
        System.out.println("Draws: " + Double.toString(draws));
        System.out.println("-----------------------");
        return (blackWins + draws / 2) / (whiteWins + draws / 2);

    }

    public void start(Stage stage) {
        /*
        double c = .075;
        ArrayList<Double> results = new ArrayList();
        for (c = .025; c < .2; c += .015) {
            results.add(testManyGames(c));
        }
        int x = 0;
        for (c = .025; c < .2; c += .015) {
            System.out.println("(" + Double.toString(c) + ", " + Double.toString(results.get(x)) + ")");
            x++;
        }
        */
        ChessBoard b = new ChessBoard();
        PositionEval.probWin(b, new White());


    }
}
