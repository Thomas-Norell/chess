package evaluation;

import board.*;
import engine.MonteCarloTree;
import org.tensorflow.*;

import pieces.*;

import java.util.ArrayList;
import java.util.List;

public class PositionEval {

    private static final SavedModelBundle save = SavedModelBundle.load("evaluation/eval-model", "serve");
    private static Session sess = save.session();
    private static Session.Runner runner = sess.runner();

    PositionEval(ChessBoard board) {
    }
    public static double probWin(ChessBoard board, Color color) {
        float[][] result = new float[1][2];
        sess = save.session();
        runner = sess.runner();
        runner.feed("x", tensor(board));
        runner.fetch("y_pred");
        List<Tensor<?>> out = runner.run();
        out.get(0).copyTo(result);

        if (color.isWhite()) {
            return result[0][0];
        }
        return result[0][1];

    }
    public static double[] probWin(ArrayList<ChessBoard> children, Color color) {
        float[][] result = new float[children.size()][2];
        sess = save.session();
        runner = sess.runner();
        runner.feed("x", tensor(children));
        runner.fetch("y_pred");
        List<Tensor<?>> out = runner.run();
        out.get(0).copyTo(result);
        double[] returnVal = new double[children.size()];



        for (int i = 0; i < result.length; i++) {
            if (color.isWhite()) {
                returnVal[i] = result[i][1];
            }
            returnVal[i] = result[i][0];
        }
        return returnVal;
    }
    private static int depth (Piece p) {
        if (p instanceof Rook) {
            return 0;
        }
        else if (p instanceof Knight) {
            return 1;
        }
        else if (p instanceof Bishop) {
            return 2;
        }
        else if (p instanceof Queen) {
            return 3;
        }
        else if (p instanceof King) {
            return 4;
        }
        else if (p instanceof Pawn) {
            return 5;
        }
        throw new Error("Something went wrong!");
    }
    private static Tensor<Float> tensor(ChessBoard board) {
        float[][][] tens = new float[8][8][6];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square s = board.getSquare(new Coordinate(x, y));
                if (s.isOccupied()) {
                    tens[x][y][depth(s.Occupant())] = s.Occupant().getColor().tensorVal();
                }
            }
        }
        float[][][][] tenso = new float[1][8][8][6];
        tenso[0] = tens;
        return (Tensor<Float>) Tensor.create(tenso);
    }
    private static Tensor<Float> tensor(ArrayList<ChessBoard> children) {
        float[][][][] tenso = new float[children.size()][8][8][6];
        for (int i = 0; i < children.size(); i++) {
            ChessBoard c = children.get(i);
            float[][][] tens = new float[8][8][6];
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Square s = c.getSquare(new Coordinate(x, y));
                    if (s.isOccupied()) {
                        tens[x][y][depth(s.Occupant())] = s.Occupant().getColor().tensorVal();
                    }
                }
            }
            tenso[i] = tens;

        }
        return (Tensor<Float>) Tensor.create(tenso);
    }
}


