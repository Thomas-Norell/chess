package game;
import board.*;
import engine.MonteCarloTree;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import pieces.King;
import pieces.Piece;
import java.io.File;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class Visualizer extends Application{

    private static final int xDim = 600;
    public Thread backgroundThread;
    private static final int yDim = 600;
    Ponder ponder;
    Controller controller;
    Stage stage;
    GridPane gridPane;
    Scene scene;
    Move m;
    MonteCarloTree tree;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        controller = new Controller(new White(), stage, this);
    }
    public void endGame(Color winner) {
        if (winner.isWhite()) {
            stage.setTitle("White wins!");
        }
        else if (winner != null) {
            stage.setTitle("Black wins!");
        }
        else {
            stage.setTitle("Stalemate!");
        }

    }
    public void startMove() {
        Runnable thisMove = new Runnable() {
            @Override
            public void run() {
                move();
            }
        };
        Thread backroundThread = new Thread(thisMove);
        this.backgroundThread = backroundThread;
        backroundThread.start();

    }

    public void move() {
        while (true) {
            this.tree.deepen();
        }
    }

    public void startChangeTurn(Square clickedSquare) {
        Runnable thisMove = new Runnable() {
            @Override
            public void run() {
                changeTurn(clickedSquare);
            }
        };

        Thread giveController = new Thread(thisMove);
        giveController.start();

    }

    public void changeTurn(Square clickedSquare) {
        controller.addTarget(clickedSquare, this);
    }


    public Runnable setUpdate(ChessBoard b, Move m) {
        Runnable update = new Runnable() {
            @Override
            public void run() {
                update(b, m);
                Media sound = new Media(new File("images/chess.wav").toURI().toString());
                new MediaPlayer(sound).play();
            }
        };
        return update;
    }

    public Runnable end(Color player) {
        Runnable update = new Runnable() {
            @Override
            public void run() {
                endGame(player);
            }
        };
        return update;
    }



    public static void renderBoard(ChessBoard board) {
        Stage s = new Stage();
        render(board, new Controller(new White(), s, new Visualizer()), s);
    }

    public void renderBoard(ChessBoard board, Controller controller, Stage stage) {
        gridPane = new GridPane();
        setup(board);
        scene = new Scene(gridPane, xDim, yDim);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
        Visualizer me = this;
        this.ponder = new Ponder( this.tree);
        ponder.start();
        gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {


                for(Node node: gridPane.getChildren()) {

                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY())) {
                            Square clickedSquare = board.getSquare(new Coordinate(GridPane.getColumnIndex( node), 7 - GridPane.getRowIndex( node)));
                            startChangeTurn(clickedSquare);
                            return;
                        }
                    }
                }
            }
        });


    }

    public static void render(ChessBoard board, Controller controller, Stage stage) {
        GridPane gridPane = new GridPane();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                ImageView square = new ImageView();
                square.setImage(board.getSquare(new Coordinate(x, y)).image());
                square.setFitHeight(yDim / 8);
                square.setFitWidth(xDim / 8);
                GridPane.setRowIndex(square, 7 - y);
                GridPane.setColumnIndex(square, x);
                gridPane.getChildren().add(square);
                if (board.getSquare(new Coordinate(x, y)).isOccupied()) {
                    ImageView piece = new ImageView();
                    piece.setImage(board.getSquare(new Coordinate(x, y)).Occupant().image());
                    piece.setFitHeight(yDim / 8);
                    piece.setFitWidth(xDim / 8);
                    GridPane.setRowIndex(piece, 7 - y);
                    GridPane.setColumnIndex(piece, x);
                    gridPane.getChildren().add(piece);
                }
            }
        }
        Scene scene = new Scene(gridPane, xDim, yDim);
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();

    }
    public void setup(ChessBoard board) {
        gridPane.getChildren().removeAll();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                ImageView square = new ImageView();
                square.setImage(board.getSquare(new Coordinate(x, y)).image());
                square.setFitHeight(yDim / 8);
                square.setFitWidth(xDim / 8);
                GridPane.setRowIndex(square, 7 - y);
                GridPane.setColumnIndex(square, x);
                gridPane.getChildren().add(square);
                if (board.getSquare(new Coordinate(x, y)).isOccupied()) {
                    ImageView piece = new ImageView();
                    piece.setImage(board.getSquare(new Coordinate(x, y)).Occupant().image());
                    piece.setFitHeight(yDim / 8);
                    piece.setFitWidth(xDim / 8);
                    GridPane.setRowIndex(piece, 7 - y);
                    GridPane.setColumnIndex(piece, x);
                    gridPane.getChildren().add(piece);
                    board.getSquare(new Coordinate(x, y)).Occupant().setNode(piece);
                }
            }
        }
    }

    //TODO: Known issue, pawn's 'shadow' stay on the board
    public void update(ChessBoard board, Move m) {
        int x, y;
        x = m.source.getCoordinate().getX();
        y = m.source.getCoordinate().getY();
        Piece thisPiece = board.getSquare(new Coordinate(x, y)).Occupant();
        gridPane.getChildren().remove(thisPiece.getNode());
        ImageView square = new ImageView();
        square.setImage(board.getSquare(new Coordinate(x, y)).image());
        square.setFitHeight(yDim / 8);
        square.setFitWidth(xDim / 8);
        GridPane.setRowIndex(square, 7 - y);
        GridPane.setColumnIndex(square, x);
        gridPane.getChildren().add(square);

        x = m.destination.getCoord().getX();
        y = m.destination.getCoord().getY();
        gridPane.getChildren().remove(board.getSquare(new Coordinate(x, y)).Occupant().getNode());
        square = new ImageView();
        square.setImage(board.getSquare(new Coordinate(x, y)).image());
        square.setFitHeight(yDim / 8);
        square.setFitWidth(xDim / 8);
        GridPane.setRowIndex(square, 7 - y);
        GridPane.setColumnIndex(square, x);
        gridPane.getChildren().add(square);
        if (board.getSquare(new Coordinate(x, y)).isOccupied()) {
            ImageView piece = new ImageView();
            piece.setImage(board.getSquare(new Coordinate(x, y)).Occupant().image());
            piece.setFitHeight(yDim / 8);
            piece.setFitWidth(xDim / 8);
            GridPane.setRowIndex(piece, 7 - y);
            GridPane.setColumnIndex(piece, x);
            gridPane.getChildren().add(piece);
            board.getSquare(new Coordinate(x, y)).Occupant().setNode(piece);
        }

        if (m.source instanceof King && m.isCastle) { //Castle
            int rank;
            int rookNew;
            if (m.destination.getCoord().getX() == 6) {
                rookNew = 5;
            }
            else {
                rookNew = 3;
            }
            if (m.source.getColor().isWhite()) {
                rank = 0;
            }
            else {
                rank = 7;
            }

            Move rookMove = new Move(board.getSquare(new Coordinate(rookNew, rank)).Occupant(), board.getSquare(new Coordinate(rookNew, rank)));
            assert  rookMove.source instanceof pieces.Rook;
            update(board, rookMove);
        }

    }

}
