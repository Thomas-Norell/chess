package board;

import pieces.Piece;
import pieces.Pawn;
import pieces.Queen;

public class Move {
    public Piece source;
    public Square destination;
    public boolean isCastle = false;
    public Move(Piece source, Square destination) {
        this.source = source;
        this.destination = destination;
    }
    public void makeMove() {
        source.move(destination);
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)) {
            return false;
        }
        this.getClass();
        return ((Move) o).source.getCoordinate().equals(this.source.getCoordinate()) && ((Move) o).destination.getCoord().equals(this.destination.getCoord());
    }

}
