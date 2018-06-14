package engine;

import board.ChessBoard;
import board.Color;
import board.Move;

import java.util.ArrayList;

public class MonteCarloTree {
    private int strength;
    private final double c = 1.414;
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
        Move move = n.movesLeft.get(0);
        n.movesLeft.remove(move);
        ChessBoard newGame = new ChessBoard(n.game);
        newGame.getSquare(move.source.getCoordinate()).Occupant().move(newGame.getSquare(move.destination.getCoord()));
        Node child = new Node(n.depth + 1, newGame, n.player.opposite(), n, move);
        n.addChild(child);
    }
    private void calcVals(Node n) {
        if (n.numDescendents == 0) {
            n.decisionVal = 0;
        }
        else {
            n.decisionVal = n.numWins/n.numDescendents + c * Math.sqrt(Math.log(root.numDescendents)/n.numDescendents);
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
        double best = 0;
        Node bestMove = root.children.get(0);
        for (Node n : root.children) {
            if (n.weight > bestMove.weight) {
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
        double weight;
        Color player;
        int numDescendents;
        Move move;
        ArrayList<Move> movesLeft;
        int numWins;
        Node(int depth, ChessBoard game, Color player, Node parent, Move m) {
            this.game = game;
            this.parent = parent;
            this.player = player;
            children = new ArrayList();
            weight = Heuristics.value(game, root().player);
            movesLeft = Heuristics.allMoves(game, player);
            backPropogate();
            numDescendents = 0;
            this.depth = depth;
            move = m;

        }
        Node root() {
            if (this.isRoot()) {
                return this;
            }
            return this.parent;
        }
        void addChild(Node child) {
            children.add(child);
        }
        void backPropogate() {
            if (!this.isRoot()) {
                parent.numDescendents += 1;
                parent.weight += this.weight;
                parent.backPropogate();
                if (weight > parent.weight) { //We 'won'
                    parent.numWins += 1;
                }
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



