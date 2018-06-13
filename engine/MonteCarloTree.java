package engine;

import board.ChessBoard;
import board.Color;

import java.util.ArrayList;

public class MonteCarloTree {
    private final double c = 1.414;
    private final int depthLimit = 3;
    public MonteCarloTree(ChessBoard board) {


    }
    private class Node {
        int depth;
        ArrayList<Node> children;
        Node parent;
        double weight;
        Color player;
        Node() {

        }
    }

}



