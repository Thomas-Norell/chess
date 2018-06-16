package game;
import board.ChessBoard;
import board.Coordinate;
import board.Square;
import board.White;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import board.Black;
import board.Move;

public class Visualizer extends Application{
    private static final int xDim = 600;
    private static final int yDim = 600;
    Controller controller;
    Stage stage;
    GridPane gridPane;
    Scene scene;
    Move m;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        controller = new Controller(new White(), stage, this);
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

        gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                for(Node node: gridPane.getChildren()) {

                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY())) {
                            //System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            Square clickedSquare = board.getSquare(new Coordinate(GridPane.getColumnIndex( node), 7 - GridPane.getRowIndex( node)));
                            //System.out.print(clickedSquare.Occupant());
                            me.m = controller.addTarget(clickedSquare, me);
                            if (m != null) {
                                m.source.move(m.destination);
                                update(board, m);
                                m = null;
                            }
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
    public void update(ChessBoard board, Move m) {
        int x, y;
        x = m.source.getCoordinate().getX();
        y = m.source.getCoordinate().getY();
        gridPane.getChildren().remove(board.getSquare(new Coordinate(x, y)).Occupant().getNode());
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

    }

}
