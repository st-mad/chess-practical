import java.util.Scanner;
import java.util.ArrayList;


public class Game {
    // The following five constants were defined in the starter code (kt54)
    private static final char FREE         = '.';
    private static final char WHITEROOK    = 'R';
    private static final char BLACKROOK    = 'r';
    private static final char WHITEBISHOP  = 'B';
    private static final char BLACKBISHOP  = 'b';

    // The following constants are not needed for the basic solution, but some
    // people might find them useful for extensions
    private static final char WHITEKING    = 'K';
    private static final char BLACKKING    = 'k';
    private static final char WHITEQUEEN   = 'Q';
    private static final char BLACKQUEEN   = 'q';
    private static final char WHITEKNIGHT  = 'N';
    private static final char BLACKKNIGHT  = 'n';
    private static final char WHITEPAWN    = 'P';
    private static final char BLACKPAWN    = 'p';

    // The following five constants were defined in the starter code (kt54)
    private static String WHITEPLAYS_MSG    = "White plays. Enter move:";
    private static String BLACKPLAYS_MSG    = "Black plays. Enter move:";
    private static String ILLEGALMOVE_MSG   = "Illegal move!";
    private static String WHITEWINS_MSG     = "White wins!";
    private static String BLACKWINS_MSG     = "Black wins!";

    private Board gameBoard;
    ArrayList<Piece> activePieces;
    ArrayList<String> moveList;
    // Minimal constructor. Expand as needed (kt54)
    public Game() {
        gameBoard = new Board();
    }

    // Build on this method to implement game logic.
    public void play() {

        Scanner reader = new Scanner(System.in);

        gameBoard = new Board();

        int turnCount = 1;
        String currentPlayer;
        boolean done = false;
        boolean isMoveLegal;
        activePieces = new ArrayList<Piece>();
        moveList = new ArrayList<String>();

        while(!done) {
            
            gameBoard.printBoard();
            
            if (turnCount % 2 == 1) {
                currentPlayer = "white";
                System.out.println(WHITEPLAYS_MSG);
            } else {
                currentPlayer = "black";
                System.out.println(BLACKPLAYS_MSG);
            }
            String pos1 = reader.nextLine();
            if (pos1.equals("quit")) {
                done = true;
                break;
            }
            String pos2 = reader.nextLine();
            pos1 = pos1.toLowerCase();
            pos2 = pos2.toLowerCase();

       }

        reader.close();
    }

    
}