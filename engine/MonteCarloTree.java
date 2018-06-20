package engine;

import board.*;

import java.util.ArrayList;
import java.util.Random;
import engine.ArrayHeap;

public class MonteCarloTree {
    private Node bestNode;
    private ArrayHeap<Node> heap;
    private double strength;
    private final double c = Math.sqrt(2);
    private final int depthLimit = 3;
    public Node root;
    public MonteCarloTree(ChessBoard board, Color player, double strength) {
        this.heap = new ArrayHeap();
        root = new Node(0, board,player, null, null, this);
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
            //throw new Error("Either I ran out of depth or something is broken!");
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
        if (n.isRoot() && n.movesLeft.size() > 0) {
            return Integer.MAX_VALUE;
        }
        else if (n.movesLeft.size() == 0 || n.depth > depthLimit || n.isMate) {
             return -Integer.MAX_VALUE;
        }
        else if (n.numDescendents == 0) {
            return (n.wins + c * Math.sqrt(Math.log(root.numDescendents + 1)))/Math.pow(2.3, n.depth);

        }
        else {
            return (n.wins / n.numDescendents + c * Math.sqrt(Math.log(root.numDescendents) / n.numDescendents))/Math.pow(2.3, n.depth);

        }

    }

    public Move bestMove() {
        Long start = System.nanoTime();
        strength = strength * 1e9;
        while (System.nanoTime() - start < strength) {
            deepen(whichNodeToDeepen());
        }
        Node bestMove = root.children.get(new Random().nextInt(root.children.size()));
        for (Node n : root.children) {
            if (n.numDescendents != 0 && n.wins/n.numDescendents > bestMove.wins / bestMove.numDescendents) {
                bestMove = n;
            }
        }
        heap = null;
        return bestMove.move;


    }

    private Node whichNodeToDeepen() {
        return heap.peek();

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
        int index; //Heap contents index
        ArrayList<Move> movesLeft;
        Node(int depth, ChessBoard board, Color player, Node parent, Move m, MonteCarloTree tree) {
            this.tree = tree;
            this.board = board;
            this.parent = parent;
            this.player = player;
            this.depth = depth;
            children = new ArrayList();
            wins = Heuristics.probWin(board, player);
            movesLeft = Heuristics.allMoves(board, player.opposite());
            priority = calcVals(this);
            tree.heap.insert(this, priority);
            isMate = board.isCheckMate(new White()) || board.isCheckMate(new White());
            numDescendents = 0;

            move = m;
            if (!isRoot()) {
                parent.backPropogate(this);
            }
        }
        void addChild(Node child) {
            children.add(child);
        }
        void backPropogate(Node sourceNode) {
            numDescendents += 1;
            if ((sourceNode.player.sameColor(player))) {
                wins += sourceNode.wins;
            }
            else {
                wins += 1 - sourceNode.wins;
            }

            if (!isRoot()) {
                parent.backPropogate(sourceNode);
            }
            this.priority = calcVals(this);
            this.tree.heap.changePriority(this, priority);
        }
        boolean isRoot() {
            return parent == null;
        }
        boolean isLeaf() {
            return children.size() == 0;
        }

    }

}



