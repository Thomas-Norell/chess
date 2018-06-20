package board;

import pieces.Piece;
import pieces.Pawn;
import pieces.Queen;

public class Move {
    public Piece source;
    public Square destination;
    public Move(Piece source, Square destination) {
        this.source = source;
        this.destination = destination;
    }
    public void makeMove() {
        source.move(destination);
    }

}
