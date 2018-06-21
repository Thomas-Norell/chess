package engine;

import board.*;

import java.util.ArrayList;
import java.util.Random;

public class MonteCarloTree {
    //private ArrayHeap<Node> heap;
    private double strength;
    private final double c = .075;
    public Node root;
    public MonteCarloTree(ChessBoard board, Color player, double strength) {
        //this.heap = new ArrayHeap();
        root = new Node(0, board,player, null, null, this);
        this.strength = strength * 1e9;
    }

    public Move bestMove() {
        Long start = System.nanoTime();

        while (System.nanoTime() - start < strength) {
            deepen(whichNodeToDeepen(root));
        }
        Node bestMove = root.children.get(new Random().nextInt(root.children.size()));
        for (Node n : root.children) {
            if (n.numDescendents != 0 && n.numDescendents > bestMove.numDescendents) {
                bestMove = n;
            }
        }
        return bestMove.move;
    }
    public void advance(Move m) {
        for (Node n : root.children) {
            if (n.move.equals(m)) {
                root = n;
                n.parent = null;
                return;
            }
        }
        for (Move move : root.movesLeft) {
            if (move.equals(m)) {
                root = new Node(root.depth += 1, root.board, root.player.opposite(), null, move, this);
                return;
            }
        }
        throw new Error("Could not advance, move not found");
    }


    private void deepen(Node n) {
        if (n.movesLeft.size() == 0) {
            //throw new Error("Either I ran out of depth or something is broken!");
            n.numDescendents += 1;
            n.parent.backPropogate(n);
            return;
        }
        Move move = n.movesLeft.get(new Random().nextInt(n.movesLeft.size()));
        n.movesLeft.remove(move);
        ChessBoard newGame = new ChessBoard(n.board);
        newGame.getSquare(move.source.getCoordinate()).Occupant().move(newGame.getSquare(move.destination.getCoord()));
        Node child = new Node(n.depth + 1, newGame, n.player.opposite(), n, move, this);
        n.addChild(child);
    }
    private double calcVals(Node n) {
        if (n.isRoot()) {
            return 0;
        }
        return n.wins / n.numDescendents + c * Math.sqrt(Math.log(n.parent.numDescendents) / n.numDescendents);
    }

    private Node whichNodeToDeepen(Node n) {
        if (n.movesLeft.size() > 0) {
            return n;
        }
        if (n.childP.size() == 0) {
            return n;
        }
        return whichNodeToDeepen(n.childP.peek());
    }

    class Node {
        MonteCarloTree tree;
        double priority;
        int depth;
        ChessBoard board;
        ArrayList<Node> children;
        Node parent;
        double wins;
        Color player;
        int numDescendents;
        Move move;
        boolean isMate;
        int index;
        ArrayHeap childP;
        ArrayList<Move> movesLeft;
        Node(int depth, ChessBoard board, Color player, Node parent, Move m, MonteCarloTree tree) {
            this.tree = tree;
            this.board = board;
            this.parent = parent;
            this.player = player;
            this.depth = depth;
            numDescendents = 1;
            childP = new ArrayHeap();
            children = new ArrayList();
            wins = Heuristics.probWin(board, player);
            movesLeft = Heuristics.allMoves(board, player.opposite());
            priority = calcVals(this);
            //tree.heap.insert(this, priority);
            isMate = board.isCheckMate(new White()) || board.isCheckMate(new White());
            move = m;
            if (!isRoot()) {
                parent.backPropogate(this);
            }
        }
        void addChild(Node child) {
            children.add(child);
            childP.insert(child, child.priority);
        }
        void backPropogate(Node sourceNode) {
            numDescendents += 1;
            if ((sourceNode.player.sameColor(player))) {
                wins += sourceNode.wins;
            }
            else {
                wins += 1 - sourceNode.wins;
            }
            this.priority = calcVals(this);
            if (!isRoot()) {
                this.parent.childP.changePriority(this, this.priority);
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



