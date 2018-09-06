package board;
public interface Color {
    //black = false, white = true
    boolean isWhite();
    default boolean sameColor(Color other) {
        return this.isWhite() == other.isWhite();
    }
    default Color opposite() {
        if (this.isWhite()) {
            return new Black();
        }
        return new White();
    }
    default Float tensorVal() {
        if (isWhite()) {
            return (float) 1.0;
        }
        return (float) -1.0;
    }


}