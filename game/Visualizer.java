package game;
import board.ChessBoard;
import board.Coordinate;
import board.Square;
import board.White;
import javafx.scene.image.Image;
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

public class Visualizer extends Application{

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        Controller controller = new Controller(new White(), stage);
    }



    public static void renderBoard(Stage stage, ChessBoard board, Controller controller) {
        int xDim = 600;
        int yDim = 600;

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

        gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                for(Node node: gridPane.getChildren()) {

                    if( node instanceof ImageView) {
                        if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY())) {
                            //System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            Square clickedSquare = board.getSquare(new Coordinate(GridPane.getColumnIndex( node), 7 - GridPane.getRowIndex( node)));
                            //System.out.print(clickedSquare.Occupant());
                            controller.addTarget(clickedSquare, gridPane);

                        }
                    }
                }
            }
        });


    }

}
