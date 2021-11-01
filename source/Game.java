import java.util.Scanner;

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
    // Minimal constructor. Expand as needed (kt54)
    public Game() {
        gameBoard = new Board();
    }

    // Build on this method to implement game logic.
    public void play() {

        Scanner reader = new Scanner(System.in);

        gameBoard = new Board();

        //variables to track game state.
        int turnCount = 1;
        String currentPlayer;
        boolean done = false;
        boolean isMoveLegal;

        while(!done) {
            //prints the board
            gameBoard.printBoard();
            
            //determines whose turn it is
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

            //This is to make sure input is independent of capitalisation.
            pos1 = pos1.toLowerCase();
            pos2 = pos2.toLowerCase();


            //checks the legality of the move and executes it
            isMoveLegal = movePiece(pos1,pos2,currentPlayer); 

            if(isMoveLegal == false){
                System.out.println(ILLEGALMOVE_MSG);
            } else {
                turnCount++;
            }
            
            switch(checkGameOver()) {
                case 'b':
                    gameBoard.printBoard();
                    System.out.println(BLACKWINS_MSG);
                    done = true;
                    break;
                case 'w':
                    gameBoard.printBoard();
                    System.out.println(WHITEWINS_MSG);
                    done = true;
                    break;
            }
       }

        reader.close();
    }

    public boolean movePiece(String initialPosition, String targetPosition, String currentPlayer) {
        //This method checks the legality of the move and also moves the piece. This functionality is split into two methods for the extension.

        //different flags for components of move validity.
        boolean isRuleBound = false;
        boolean isColourCorrect = false;
        boolean isTargetValid = false;
        boolean isOutofBounds = false;
        boolean isPathValid = false;
        
        //converts algebraic notation into 0-7 inclusive x,y coordinates
        int initialPositionX = (int) initialPosition.charAt(0) - 'a';
        int initialPositionY = 8 - ((int) initialPosition.charAt(1) - '0');
        int targetPositionX = (int) targetPosition.charAt(0) - 'a';
        int targetPositionY = 8 - ((int) targetPosition.charAt(1) - '0');

        //checks if the move is trying to go out of bounds.
        isOutofBounds = (targetPositionX < 0 || targetPositionX >= 8) | (initialPositionX < 0 || initialPositionX >= 8) |
                        (targetPositionY < 0 || targetPositionY >= 8) | (initialPositionY < 0 || initialPositionY >= 8); 

        if (isOutofBounds) {  
            return false;
        }
        
        
        //checks if the move is rule bound.i.e. valid for the pieces moveset.
        switch(gameBoard.getPiece(initialPositionY,initialPositionX)) {
            case BLACKBISHOP:
                //check paths
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (Math.abs(targetPositionY-initialPositionY) == Math.abs(targetPositionX-initialPositionX));
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEBISHOP:
                //check path
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (Math.abs(targetPositionY-initialPositionY) == Math.abs(targetPositionX-initialPositionX));
                isColourCorrect = currentPlayer.equals("white");
                break;
            case BLACKROOK:
                //check path
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEROOK:
                //check paths
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
                isColourCorrect = currentPlayer.equals("white");
                break;
        }

        //checks if the piece on target position is capturable.
        if (currentPlayer.equals("black") && isWhite(gameBoard.getPiece(targetPositionY,targetPositionX))) {
            isTargetValid = true;
        } else if (currentPlayer.equals("white") && isBlack(gameBoard.getPiece(targetPositionY,targetPositionX))) {
            isTargetValid = true;
        } else if (gameBoard.getPiece(targetPositionY,targetPositionX) == '.') {
            isTargetValid = true;
        }


        //puts all the components together to determine isMoveValid
        boolean isMoveValid = isPathValid && isRuleBound && isColourCorrect && isTargetValid;

        //executes the move.
        if (isMoveValid) {
            char buffer = gameBoard.getPiece(initialPositionY,initialPositionX);
            gameBoard.setPiece(targetPositionY, targetPositionX, buffer);
            gameBoard.setPiece(initialPositionY, initialPositionX, FREE);
        }
        return isMoveValid;
    }

    public char checkGameOver() {
        //Naive win condition, where winning means the other player has no pieces.
        //Counts up the number of pieces of each colour.
        int numberOfBlackPieces = 0;
        int numberOfWhitePieces = 0;
        
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char temp =  gameBoard.getPiece(i,j);
                if (temp != '.' && Character.isUpperCase(temp)) {
                    numberOfWhitePieces++;
                } else if (temp != '.' && Character.isLowerCase(temp)) {
                    numberOfBlackPieces++;
                }
            }
        }

        if (numberOfBlackPieces == 0) return 'w';
        if (numberOfWhitePieces == 0) return 'b';
        return 'n';
    }

    public boolean checkPath(int x1, int y1, int x2, int y2) {
        //I have a slight suspicion that this is overcomplicated, and might not be the best solution.
        //Increment X,Y determine the direction that the target square is from the initial square.
        int incrementX = 0;
        int incrementY = 0;

        //this makes the value either -1, 0, or 1
        if(x1 != x2) incrementX = (x2-x1)/(Math.abs(x2-x1));
        if(y1 != y2) incrementY = (y2-y1)/(Math.abs(y2-y1));

        //the algorithm starts one square in the direction of the target and checks if the square is empty, before incrementing i,j with incrementx or increment y respectively.
        //Each step the while loop moves closer to the target, until it is on the target. If a square is not empty it returns false to indicate that the path is not valid.
        int i = x1 + incrementX;
        int j = y1 + incrementY;
        while ((i != x2) || (j != y2)) {
            if (gameBoard.getPiece(j,i) != '.') {
                return false;
            }

            if (i != x2){i = i + incrementX;}
            if (j != y2){j = j + incrementY;}
        }
        return true;
    }

    public boolean isWhite(char n) {
        if (n <= 'Z' && n >= 'A') {
            return true;
        }
        return false;
    }

    public boolean isBlack(char n) {
        if (n <= 'z' && n >= 'a') {
            return true;
        }
        return false;
    }
}