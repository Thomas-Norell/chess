package engine;

import board.ChessBoard;
import board.Color;
import board.Move;
import game.Controller;
import game.Visualizer;

import java.util.ArrayList;
import java.util.Random;

public class MonteCarloTree {
    private int strength;
    private final double c = 10;
    private final int depthLimit = 3;
    private ArrayList<Node> fringe;
    private Node root;
    public MonteCarloTree(ChessBoard board, Color player, int strength) {
        root = new Node(0, board,player, null, null);
        fringe = new ArrayList();
        this.strength = strength;
    }
    private void deepen(Node n) {
        if (n.movesLeft.size() == 0) {
            return;
        }
        Move move = n.movesLeft.get(new Random().nextInt(n.movesLeft.size()));
        n.movesLeft.remove(move);
        ChessBoard newGame = new ChessBoard(n.game);
        newGame.getSquare(move.source.getCoordinate()).Occupant().move(newGame.getSquare(move.destination.getCoord()));
        Node child = new Node(n.depth + 1, newGame, n.player.opposite(), n, move);
        n.addChild(child);
    }
    private void calcVals(Node n) {
        if (n.movesLeft.size() == 0) {
            n.decisionVal = 0;
        }
        else if (n.numDescendents == 0) {
            n.decisionVal = n.wins + c * Math.sqrt(Math.log(root.numDescendents));
        }

        else {
            n.decisionVal = n.wins/n.numDescendents + c * Math.sqrt(Math.log(root.numDescendents)/n.numDescendents);
        }
        if (n.isLeaf()) {
            return;
        }
        for (Node c : n.children) {
            calcVals(c);
        }
    }

    private Node getBestNode(Node n, Node best) {

        if (n.isLeaf()) {
            if (n.decisionVal > best.decisionVal) {
                return n;
            }
        }
        for (Node c : n.children) {
            if (getBestNode(c, best).decisionVal > best.decisionVal) {
                best = c;
            }
        }
        return best;
    }

    public Move bestMove() {
        for (int i = 0; i < strength; i++) {
            deepen(whichNodeToDeepen());
        }
        Node bestMove = root.children.get(0);
        for (Node n : root.children) {
            if (n.numDescendents != 0 && n.wins/n.numDescendents > bestMove.wins / bestMove.numDescendents) {
                bestMove = n;
            }
        }
        return bestMove.move;


    }

    private Node whichNodeToDeepen() {
        calcVals(root);
        return getBestNode(root, root);
    }
    private class Node {
        double decisionVal;
        int depth;
        ChessBoard game;
        ArrayList<Node> children;
        Node parent;
        int wins;
        Color player;
        int numDescendents;
        Move move;
        ArrayList<Move> movesLeft;
        Node(int depth, ChessBoard game, Color player, Node parent, Move m) {
            this.game = game;
            this.parent = parent;
            this.player = player;
            children = new ArrayList();
            if (Heuristics.winner(game) == player) {
                wins = 1;
            }
            else {
                wins = 0;
            }
            movesLeft = Heuristics.allMoves(game, player);
            backPropogate(this);
            numDescendents = 0;
            this.depth = depth;
            move = m;

        }
        void addChild(Node child) {
            children.add(child);
        }
        void backPropogate(Node sourceNode) {
            if (!this.isRoot()) {
                parent.numDescendents += 1;
                if (sourceNode.player.sameColor(parent.player)) {
                    parent.wins += 1;
                }
                parent.backPropogate(sourceNode);
            }
        }
        boolean isRoot() {
            return parent == null;
        }
        boolean isLeaf() {
            return children.size() == 0;
        }

    }

}



