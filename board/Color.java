package board;
public interface Color {
    //black = false, white = true
    boolean isWhite();
    default boolean sameColor(Color other) {
        return this.isWhite() == other.isWhite();
    }


}