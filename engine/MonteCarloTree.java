package engine;

import board.ChessBoard;
import board.Color;
import board.Move;
import board.White;

import java.util.ArrayList;
import java.util.Random;

public class MonteCarloTree {
    private int strength;
    private final double c = Math.sqrt(2);
    private final int depthLimit = 3;
    public Node root;
    public MonteCarloTree(ChessBoard board, Color player, int strength) {
        root = new Node(0, board,player, null, null);
        this.strength = strength;
    }
    public Color playout(Node n) {
        ChessBoard b = new ChessBoard(n.board);
        Color currentPlayer = n.player;
        ArrayList<Move> moves;
        int x = 0;
        while (b.whoCheckMate() == null) {
            moves = Heuristics.allMoves(b, currentPlayer);
             moves.get(new Random().nextInt(moves.size())).makeMove();
            currentPlayer = currentPlayer.opposite();
            x++;
        }
        return b.whoCheckMate();
    }



    private void deepen(Node n) {
        if (n.movesLeft.size() == 0) {
            throw new Error("Either I ran out of depth or something is broken!");
        }
        Move move = n.movesLeft.get(new Random().nextInt(n.movesLeft.size()));
        n.movesLeft.remove(move);
        ChessBoard newGame = new ChessBoard(n.board);
        newGame.getSquare(move.source.getCoordinate()).Occupant().move(newGame.getSquare(move.destination.getCoord()));
        Node child = new Node(n.depth + 1, newGame, n.player.opposite(), n, move);
        n.addChild(child);
    }
    private void calcVals(Node n) {
        if (n.isRoot() && n.movesLeft.size() > 0) {
            n.decisionVal = Integer.MAX_VALUE;
        }
        else if (n.movesLeft.size() == 0 || n.depth > depthLimit || n.isMate) {
            n.decisionVal = -Integer.MAX_VALUE;
        }
        else if (n.numDescendents == 0) {
            n.decisionVal = (n.wins + c * Math.sqrt(Math.log(root.numDescendents)));

        }

        else {
            n.decisionVal =  (n.wins / n.numDescendents + c * Math.sqrt(Math.log(root.numDescendents) / n.numDescendents));

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
            if (n.numDescendents != 0 && n.wins/n.numDescendents < bestMove.wins / bestMove.numDescendents) {
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
        ChessBoard board;
        ArrayList<Node> children;
        Node parent;
        float wins;
        Color player;
        int numDescendents;
        Move move;
        boolean isMate;
        ArrayList<Move> movesLeft;
        Node(int depth, ChessBoard board, Color player, Node parent, Move m) {
            this.board = board;
            this.parent = parent;
            this.player = player;
            children = new ArrayList();
            wins = Heuristics.value(board, player);
            movesLeft = Heuristics.allMoves(board, player);
            if (!isRoot()) {
                parent.backPropogate(this);
            }
            isMate = board.isCheckMate(new White()) || board.isCheckMate(new White());

            numDescendents = 0;
            this.depth = depth;
            move = m;

        }
        void addChild(Node child) {
            children.add(child);
        }
        void backPropogate(Node sourceNode) {
            numDescendents += 1;
            if ((sourceNode.player.sameColor(player) && sourceNode.wins == 1) || (!sourceNode.player.sameColor(player) && sourceNode.wins == 0)) {
                wins += 1;
            }

            if (!isRoot()) {
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



