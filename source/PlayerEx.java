import java.util.ArrayList;

public class PlayerEx {
    String colour;
    int kingX;
    int kingY;
    boolean hasMovedKing;
    boolean isInCheck;
    ArrayList<String> moveSet;

    public PlayerEx () {
        
    }
    public PlayerEx (String player) {
        colour = player;
        hasMovedKing = false;
        isInCheck = false;
        if (colour == "black") {
            kingX = 4;
            kingY = 0;
        }
        else if (colour == "white") {
            kingX = 4;
            kingY = 7;
        }
        moveSet = new ArrayList<String>();
    }
}
