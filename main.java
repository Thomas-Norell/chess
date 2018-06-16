import board.Black;
import board.ChessBoard;
import board.Coordinate;
import board.Square;
import engine.MonteCarloTree;
import game.Visualizer;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Test;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import board.Move;
import pieces.Queen;


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
        Visualizer.renderBoard(b);
        Move m = decision.bestMove();
        m.makeMove();
        Visualizer.renderBoard(b);

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
        Visualizer.renderBoard(b);
        Move m = decision.bestMove();
        m.makeMove();
        Visualizer.renderBoard(b);

    }

    @Test()
    public void testOpen() {
        ChessBoard b = new ChessBoard();
        Pawn whiteQueenPawn = (Pawn) b.getSquare(new Coordinate(4, 1)).Occupant();
        whiteQueenPawn.move(b.getSquare(new Coordinate(4, 3)));
        MonteCarloTree decision = new MonteCarloTree(b, new Black(), 500);
        Visualizer.renderBoard(b);
        Move m = decision.bestMove();
        m.makeMove();
        Visualizer.renderBoard(b);

    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage stage) {
        testWeird();
    }
}
