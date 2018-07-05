package game;
import board.*;
import engine.MonteCarloTree;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import java.io.File;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

public class Visualizer extends Application{

    private static final int xDim = 600;
    public Thread backgroundThread;
    private static final int yDim = 600;
    Ponder ponder;
    Controller controller;
    Stage stage;
    GridPane gridPane;
    MonteCarloTree tree;
    Color human;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.human = new White();
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
                update(b);
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

    private void switchSides() {
        this.human = human.opposite();
        controller = new Controller(human, stage, this);
    }


    public void renderBoard(ChessBoard board, Controller controller, Stage stage) {
        gridPane = new GridPane();
        setup(board);
        ToolBar toolBar = new ToolBar();
        toolBar.setMaxHeight(20);
        ImageView switchbtn = new ImageView(new Image("images/switch.png"));
        switchbtn.setFitHeight(15);
        switchbtn.setFitWidth(15);
        Button btnNew = new Button("", switchbtn);
        Slider difficulty = new Slider(0.1, 60.0, 1);
        difficulty.showTickMarksProperty();
        difficulty.showTickLabelsProperty();
        toolBar.getItems().add(btnNew);
        toolBar.getItems().add(difficulty);
        BorderPane pane = new BorderPane();
        pane.setBottom(toolBar);
        pane.setTop(gridPane);
        Scene main = new Scene(pane, xDim, yDim + 39);
        stage.setTitle("Chess");
        stage.setScene(main);
        stage.show();

        if (!human.isWhite()) {
            Move best = tree.bestMove();
            tree.advance(best);
            best = (new Move(board.getSquare(best.source.getCoordinate()).Occupant(), board.getSquare(best.destination.getCoord())));
            best.makeMove();
            update(board);
        }


        this.ponder = new Ponder( this.tree);
        ponder.start();
        gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                for(Node node: gridPane.getChildren()) {
                    controller.setStrength(difficulty.getValue());

                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY())) {
                            Square clickedSquare = board.getSquare(new Coordinate(transformColumn(GridPane.getColumnIndex(node), 0), transformRow(0, GridPane.getRowIndex( node))));
                            startChangeTurn(clickedSquare);
                            return;
                        }
                    }
                }
            }
        });

        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switchSides();
                //setup(new ChessBoard());
            }
        });
    }
    private int transformRow(Integer x, Integer y) {
        if (human.isWhite()) {
            return 7 - y;
        }
        else {
            return y;
        }
    }
    private int transformColumn(Integer x, Integer y) {
        if (human.isWhite()) {
            return x;
        }
        else {
            return x;
        }

    }
    public void setup(ChessBoard board) {
        gridPane.getChildren().removeAll();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                ImageView square = new ImageView();
                square.setImage(board.getSquare(new Coordinate(x, y)).image());
                square.setFitHeight(yDim / 8);
                square.setFitWidth(xDim / 8);
                GridPane.setRowIndex(square, transformRow(x , y));
                GridPane.setColumnIndex(square, transformColumn(x , y));
                gridPane.getChildren().add(square);
                if (board.getSquare(new Coordinate(x, y)).isOccupied()) {
                    ImageView piece = new ImageView();
                    piece.setImage(board.getSquare(new Coordinate(x, y)).Occupant().image());
                    piece.setFitHeight(yDim / 8);
                    piece.setFitWidth(xDim / 8);
                    GridPane.setRowIndex(piece, transformRow(x, y));
                    GridPane.setColumnIndex(piece, transformColumn(x, y));
                    gridPane.getChildren().add(piece);
                    board.getSquare(new Coordinate(x, y)).Occupant().setNode(piece);
                }
            }
        }
    }

    public void update(ChessBoard board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board.getSquare(new Coordinate(x, y)).isOccupied()) {
                    gridPane.getChildren().remove(board.getSquare(new Coordinate(x, y)).Occupant().getNode());
                }
                ImageView square = new ImageView();
                square.setImage(board.getSquare(new Coordinate(x, y)).image());
                square.setFitHeight(yDim / 8);
                square.setFitWidth(xDim / 8);
                GridPane.setRowIndex(square, transformRow(x , y));
                GridPane.setColumnIndex(square, transformColumn(x , y));
                gridPane.getChildren().add(square);
                if (board.getSquare(new Coordinate(x, y)).isOccupied()) {
                    ImageView piece = new ImageView();
                    piece.setImage(board.getSquare(new Coordinate(x, y)).Occupant().image());
                    piece.setFitHeight(yDim / 8);
                    piece.setFitWidth(xDim / 8);
                    GridPane.setRowIndex(piece, transformRow(x , y));
                    GridPane.setColumnIndex(piece, transformColumn(x , y));
                    gridPane.getChildren().add(piece);
                    board.getSquare(new Coordinate(x, y)).Occupant().setNode(piece);
                }
            }
        }
    }
}
