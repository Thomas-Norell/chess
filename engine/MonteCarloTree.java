package engine;

import board.ChessBoard;
import board.Color;
import board.Move;
import game.Controller;
import game.Visualizer;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.Random;

public class MonteCarloTree {
    private int strength;
    private final double c = 1.4;
    private final int depthLimit = 10;
    private ArrayList<Node> fringe;
    private Node root;
    public MonteCarloTree(ChessBoard board, Color player, int strength) {
        root = new Node(0, board,player, null, null);
        fringe = new ArrayList();
        this.strength = strength;
    }
    private void deepen(Node n) {
        if (n.movesLeft.size() == 0) {
            throw new Error("Either I ran out of depth or something is broken!");
        }
        Move move = n.movesLeft.get(new Random().nextInt(n.movesLeft.size()));
        n.movesLeft.remove(move);
        ChessBoard newGame = new ChessBoard(n.game);
        newGame.getSquare(move.source.getCoordinate()).Occupant().move(newGame.getSquare(move.destination.getCoord()));
        Node child = new Node(n.depth + 1, newGame, n.player.opposite(), n, move);
        n.addChild(child);
    }
    private void calcVals(Node n) {
        if (n.movesLeft.size() == 0 || n.depth > depthLimit) {
            n.decisionVal = 0;
        }
        else if (n.numDescendents == 0) {
            n.decisionVal = Math.abs(n.wins) + c * Math.sqrt(Math.log(root.numDescendents));

        }

        else {
            n.decisionVal = Math.abs(n.wins) / n.numDescendents + c * Math.sqrt(Math.log(root.numDescendents) / n.numDescendents);

        }
        if (n.isLeaf()) {
            return;
        }
        for (Node c : n.children) {
            calcVals(c);
        }
    }

    private Node getBestNode(Node n) {
        Node best = n;
        for (Node c : n.children) {
            Node potential = getBestNode(c);
            if (potential.decisionVal > best.decisionVal) {
                best = potential;
            }
        }
        return best;
    }

    public Move bestMove() {
        for (int i = 0; i < strength; i++) {
            deepen(whichNodeToDeepen());
        }
        Node bestMove = root.children.get(new Random().nextInt(root.children.size()));
        for (Node n : root.children) {
            if (n.wins/n.numDescendents < bestMove.wins / bestMove.numDescendents) {
                bestMove = n;
            }
        }
        return bestMove.move;


    }

    private Node whichNodeToDeepen() {
        calcVals(root);
        return getBestNode(root);
    }
    private class Node {
        double decisionVal;
        int depth;
        ChessBoard game;
        ArrayList<Node> children;
        Node parent;
        float wins;
        Color player;
        int numDescendents;
        Move move;
        ArrayList<Move> movesLeft;
        Node(int depth, ChessBoard game, Color player, Node parent, Move m) {
            this.game = game;
            this.parent = parent;
            this.player = player;
            children = new ArrayList();
            wins = Heuristics.value(game, player);
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
                    parent.wins += sourceNode.wins;
                }
                else {
                    parent.wins -= sourceNode.wins;
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



