package board;

import pieces.Piece;

public class Move {
    public Piece source;
    public Square destination;
    public Move(Piece source, Square destination) {
        this.source = source;
        this.destination = destination;
    }
}
