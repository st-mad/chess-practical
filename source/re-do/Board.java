public class Board {

    // The following five constants were defined in the starter code (kt54)
    private static final int  DEFAULT_SIZE = 8;
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

    private int boardsize;
    private Piece[][] board;

    // Default constructor was provided by the starter code. Extend as needed (kt54) 
    public Board() {
        this.boardsize = DEFAULT_SIZE;

        board = new Piece[boardsize][boardsize];

        // Clear all playable fields
        for(int row=0; row<boardsize; row++){
            for(int col=0; col<boardsize; col++){
                board[row][col].type = FREE;
            }
        }
    }

    // Prints the board. This method was provided with the starter code. Better not modify to ensure
    // output consistent with the autochecker (kt54)
    public void printBoard() {

        // Print the letters at the top
        System.out.print(" ");
        for(int col=0; col<boardsize; col++) {
            System.out.print(" " + (char)(col + 'a'));
        }
        System.out.println();

        for(int row=0; row<boardsize; row++) {
            // Print the numbers on the left side
            System.out.print(8-row);

            // Print the actual board fields
            for(int col=0; col<boardsize; col++) {
                System.out.print(" ");
                char value = board[row][col].type;
                if(value == FREE) {
                    System.out.print(".");
                } else {
                    System.out.print(value);
                }
            }
            // Print the numbers on the right side
            System.out.println(" " + (8-row));
        }

        // Print the letters at the bottom
        System.out.print(" ");
        for(int col=0; col<boardsize; col++) 
            System.out.print(" " + (char)(col + 'a'));
        System.out.print("\n\n");
    }

    public Piece getPiece(int row, int column) {
        return board[row][column];
    }

    public void setPiece(int row, int column, Piece piece) {
        board[row][column] = piece;
    }
}
