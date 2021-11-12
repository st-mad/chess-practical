public class BoardEx {

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
    private char[][] board;

    // Default constructor was provided by the starter code. Extend as needed (kt54) 
    public BoardEx() {
        this.boardsize = DEFAULT_SIZE;

        board = new char[boardsize][boardsize];

        // Clear all playable fields
        for(int row=0; row<boardsize; row++){
            for(int col=0; col<boardsize; col++){
                board[row][col] = FREE;
            }
        }
        
        //STARTING SETUP modified to be the full chess game (sms31)
        for (int i = 0; i < 8; i++) {
            board[1][i] = BLACKPAWN;
            board[6][i] = WHITEPAWN;
        }
        
        board[0][0] = BLACKROOK;
        board[0][7] = BLACKROOK;
        board[7][0] = WHITEROOK;
        board[7][7] = WHITEROOK;
        
        board[0][2] = BLACKBISHOP;
        board[0][5] = BLACKBISHOP;
        board[7][2] = WHITEBISHOP;
        board[7][5] = WHITEBISHOP;

        board[0][1] = BLACKKNIGHT;
        board[0][6] = BLACKKNIGHT;
        board[7][1] = WHITEKNIGHT;
        board[7][6] = WHITEKNIGHT;

        board[0][3] = BLACKQUEEN;
        board[7][3] = WHITEQUEEN;
        
        board[0][4] = BLACKKING;
        board[7][4] = WHITEKING;      
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
                char value = board[row][col];
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

    //getters and setters

    public char getPiece(int row, int column) {
        return board[row][column];
    }

    public void setPiece(int row, int column, char piece) {
        board[row][column] = piece;
    }

    public char[][] getBoard() {
        char[][] result = new char[8][8];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;
    }

    public void setBoard(char[][] gameBoard) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = gameBoard[i][j];
            }
        }
    }

    public void movePiece(String coordinates, PlayerEx blackPlayer, PlayerEx whitePlayer) {
        // this function should maybe be moved to the board class, but the wording of
        // the specification makes me think its meant to be here.

        // converts integer string into ints
        int initialPositionX = (int) coordinates.charAt(0) - 'a';
        int initialPositionY = 8 - ((int) coordinates.charAt(1) - '0');
        int targetPositionX = (int) coordinates.charAt(2) - 'a';
        int targetPositionY = 8 - ((int) coordinates.charAt(3) - '0');

        // tracks position of the king in a field.
        if (board[initialPositionY][initialPositionX]  == 'k') {
            blackPlayer.kingX = targetPositionX;
            blackPlayer.kingY = targetPositionY;
            blackPlayer.hasMovedKing = true;
        }

        // tracks position of the king in a field.
        if (board[initialPositionY][initialPositionX] == 'K') {
            whitePlayer.kingX = targetPositionX;
            whitePlayer.kingY = targetPositionY;
            whitePlayer.hasMovedKing = true;
        }

        //castling
        
        if (board[initialPositionY][initialPositionX] == 'K') {
            if (coordinates.equals("e1g1")) {
                char buffer = board[initialPositionY][initialPositionX];
                board[targetPositionY][targetPositionX] = buffer; 
                board[initialPositionY][initialPositionX] = FREE;

                movePiece("h1f1",blackPlayer,whitePlayer);

            } else if (coordinates.equals("e1c1")){
                char buffer = board[initialPositionY][initialPositionX];
                board[targetPositionY][targetPositionX] = buffer; 
                board[initialPositionY][initialPositionX] = FREE;

                movePiece("a1d1",blackPlayer,whitePlayer);
            }
        } else if (board[initialPositionY][initialPositionX] == 'k') {
            if (coordinates.equals("e8g8")) {
                char buffer = board[initialPositionY][initialPositionX];
                board[targetPositionY][targetPositionX] = buffer; 
                board[initialPositionY][initialPositionX] = FREE;

                movePiece("h8f8",blackPlayer,whitePlayer);

            } else if (coordinates.equals("e8c8")){
                char buffer = board[initialPositionY][initialPositionX];
                board[targetPositionY][targetPositionX] = buffer; 
                board[initialPositionY][initialPositionX] = FREE;
                
                movePiece("a8d8",blackPlayer,whitePlayer);
            }
        } else {
            // actual swap
            char buffer = board[initialPositionY][initialPositionX];
            board[targetPositionY][targetPositionX] = buffer; 
            board[initialPositionY][initialPositionX] = FREE;
        }
       

    }
}
