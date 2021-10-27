import java.util.Scanner;

public class GameEx {
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

    private BoardEx gameBoard;
    // Minimal constructor. Expand as needed (kt54)
    public GameEx() {
        gameBoard = new BoardEx();
    }

    // Build on this method to implement game logic.
    public void play() {

        Scanner reader = new Scanner(System.in);

        gameBoard = new BoardEx();

        int turnCount = 1;
        String currentPlayer;
        boolean done = false;
        boolean isMoveLegal;

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

            String coordinates = convertToCoords(pos1,pos2);

            isMoveLegal = isMoveLegal(coordinates,currentPlayer); 

            if (isMoveLegal) {
                movePiece(coordinates);
            }

            if(isMoveLegal == false) {
                System.out.println(ILLEGALMOVE_MSG);
            }

            if(isMoveLegal == true) {
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

    public String convertToCoords(String initialPosition, String targetPosition) {
        String initialPositionX = "" + ((int) initialPosition.charAt(0) - 97);
        String initialPositionY = "" + (8 - ((int) initialPosition.charAt(1) - 48));
        String targetPositionX = "" + ((int) targetPosition.charAt(0) - 97);
        String targetPositionY = "" + (8 - ((int) targetPosition.charAt(1) - 48));

        String result = initialPositionX + initialPositionY + targetPositionX + targetPositionY;
        return result;
    }

    public boolean isMoveLegal(String coordinates, String currentPlayer) {
        boolean isPathValid = false;
        boolean isRuleBound = false;
        boolean isColourCorrect = false;
        boolean isTargetValid = false;
        boolean isOutofBounds = false;
        
        /*//converts algebraic notation into 0-7 inclusive x,y coordinates
        int initialPositionX = (int) initialPosition.charAt(0) - 97;
        int initialPositionY = 8 - ((int) initialPosition.charAt(1) - 48);
        int targetPositionX = (int) targetPosition.charAt(0) - 97;
        int targetPositionY = 8 - ((int) targetPosition.charAt(1) - 48);*/

        int initialPositionX = (int)coordinates.charAt(0) - '0';
        int initialPositionY = (int)coordinates.charAt(1) - '0';
        int targetPositionX = (int)coordinates.charAt(2) - '0';
        int targetPositionY = (int)coordinates.charAt(3) - '0';

        //System.out.println(initialPositionX + " " + initialPositionY + " " + targetPositionX + " " + targetPositionY);//debug code
        isOutofBounds = (targetPositionX < 0 || targetPositionX >= 8) | (initialPositionX < 0 || initialPositionX >= 8) |
                        (targetPositionY < 0 || targetPositionY >= 8) | (initialPositionY < 0 || initialPositionY >= 8); 

        if (isOutofBounds) {  
            return false;
        }
        
        //checks if the move is rule bound
        switch(gameBoard.getPiece(initialPositionY,initialPositionX)) {
            case BLACKBISHOP:
                //checks paths
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (Math.abs(targetPositionY-initialPositionY) == Math.abs(targetPositionX-initialPositionX));
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEBISHOP:
                //checks path
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (Math.abs(targetPositionY-initialPositionY) == Math.abs(targetPositionX-initialPositionX));
                isColourCorrect = currentPlayer.equals("white");
                break;
            case BLACKROOK:
                //checks path
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEROOK:
                //checks paths
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
                isColourCorrect = currentPlayer.equals("white");
                break;
            case BLACKPAWN:
                isPathValid = true;
                if (Math.abs(targetPositionX - initialPositionX) == 1 && targetPositionY == initialPositionY + 1) {
                    isRuleBound = isWhite(gameBoard.getPiece(targetPositionY,targetPositionX));
                }
                else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY + 1){
                    isRuleBound = (gameBoard.getPiece(targetPositionY,targetPositionX) == '.');
                }
                else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY + 2){
                    isRuleBound = (gameBoard.getPiece(targetPositionY,targetPositionX) == '.') && initialPositionY == 1;
                }
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEPAWN:
                isPathValid = true;
                if (Math.abs(targetPositionX - initialPositionX) == 1 && targetPositionY == initialPositionY - 1) {
                    isRuleBound = isBlack(gameBoard.getPiece(targetPositionY,targetPositionX));
                }
                else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY - 1){
                    isRuleBound = (gameBoard.getPiece(targetPositionY,targetPositionX) == '.');
                } 
                else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY - 2){
                    isRuleBound = (gameBoard.getPiece(targetPositionY,targetPositionX) == '.') && initialPositionY == 6;
                }
                isColourCorrect = currentPlayer.equals("white");
                break;
            case BLACKKNIGHT:
                //checks path
                isPathValid = true;
                isRuleBound = (Math.abs(targetPositionX - initialPositionX) == 1 && Math.abs(targetPositionY - initialPositionY) == 2) || 
                              (Math.abs(targetPositionX - initialPositionX) == 2 && Math.abs(targetPositionY - initialPositionY) == 1);
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEKNIGHT:
                //checks paths
                isPathValid = true;
                isRuleBound = (Math.abs(targetPositionX - initialPositionX) == 1 && Math.abs(targetPositionY - initialPositionY) == 2) || 
                              (Math.abs(targetPositionX - initialPositionX) == 2 && Math.abs(targetPositionY - initialPositionY) == 1);
                isColourCorrect = currentPlayer.equals("white");
                break;
            case BLACKQUEEN:
                //checks paths
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (Math.abs(targetPositionY-initialPositionY) == Math.abs(targetPositionX-initialPositionX)) || 
                              (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEQUEEN:
                //checks path
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (Math.abs(targetPositionY-initialPositionY) == Math.abs(targetPositionX-initialPositionX)) || 
                              (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
                isColourCorrect = currentPlayer.equals("white");
                break;
            case BLACKKING:
                //checks paths
                isPathValid = checkPath(initialPositionX,initialPositionY,targetPositionX,targetPositionY);
                isRuleBound = (Math.abs(targetPositionX-initialPositionX) == 1 && Math.abs(targetPositionY-initialPositionY) == 1);
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEKING:
                //checks path
                isPathValid = true;
                isRuleBound = (Math.abs(targetPositionX-initialPositionX) == 1 && Math.abs(targetPositionY-initialPositionY) == 1);
                isColourCorrect = currentPlayer.equals("white");
                break;
        }

        //captures
        if (currentPlayer.equals("black") && isWhite(gameBoard.getPiece(targetPositionY,targetPositionX))) {
            isTargetValid = true;
        } else if (currentPlayer.equals("white") && isBlack(gameBoard.getPiece(targetPositionY,targetPositionX))) {
            isTargetValid = true;
        } else if (gameBoard.getPiece(targetPositionY,targetPositionX) == '.') {
            isTargetValid = true;
        }

        //System.out.println("" + isPathValid + isRuleBound + isColourCorrect + isTargetValid);//debug line
        boolean isMoveValid = isPathValid && isRuleBound && isColourCorrect && isTargetValid;
        return isMoveValid;
    }

    public void movePiece(String coordinates) {
        //converts algebraic notation into 0-7 inclusive x,y coordinates
        int initialPositionX = (int)coordinates.charAt(0) - '0';
        int initialPositionY = (int)coordinates.charAt(1) - '0';
        int targetPositionX = (int)coordinates.charAt(2) - '0';
        int targetPositionY = (int)coordinates.charAt(3) - '0';
        
        char buffer = gameBoard.getPiece(initialPositionY,initialPositionX);
        gameBoard.setPiece(targetPositionY, targetPositionX, buffer);
        gameBoard.setPiece(initialPositionY, initialPositionX, FREE);
    }

    public char checkGameOver() {
        //implement checks later. For now its classic chess.
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

            //System.out.println(y2 + "," + j + " : " + x2 + "," + i + " : "+ gameBoard.getPiece(j,i)); debug
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

    public boolean isReachable(int x, int y, String colour) {
        //I understand that his approach is very inefficient, but with the current structure of my code, I can't think of any better way to do it.
        boolean result = false;
        String coords;
        
        char BISHOP = 'b';
        char QUEEN = 'q';
        char ROOK = 'r';
        char KNIGHT = 'k';
        char PAWN = 'p';

        if(colour.equals("white")) { 
            BISHOP = 'B';
            QUEEN = 'Q';
            ROOK = 'R';
            KNIGHT = 'K';
            PAWN = 'P';
        }
        // check diagonals

        //diag 1
        //for the diagonal going from bottom left to top right, the equation in terms of game coordinates should be y = -x + (x1 + y1), where (x1.y1) is the point the line passes through.
        //the intersections of this line with the bottom of the board occurs when y = 7. so the starting point will be y = 7 and x = -7 + (x1 + y1)
        int xCounter1 = -7 + x + y;
        int yCounter1 = 7;
        while (xCounter1 != 8 || xCounter1 != -1 || yCounter1 != 8 || yCounter1 != -1) {
            if ((xCounter1 != x && yCounter1 != y) && (gameBoard.getPiece(yCounter1, xCounter1) == BISHOP || gameBoard.getPiece(yCounter1, xCounter1) == QUEEN)) {
                coords = Integer.toString(xCounter1) + Integer.toString(yCounter1) + Integer.toString(x) + Integer.toString(y);
                if (isMoveLegal(coords,colour)) {
                    result = true;
                }
            }
            xCounter1++;
            yCounter1--;
        }

        //diag 2
        //same theory as for the last one, but here the line is of slope +1 and it intersects with y = 0
        int xCounter2 = 0;
        int yCounter2 = y - x;
        while (xCounter2 != 8 || xCounter2 != -1 || yCounter2 != 8 || yCounter2 != -1) {
            if ((xCounter2 != x && yCounter2 != y) && (gameBoard.getPiece(yCounter2, xCounter2) == BISHOP || gameBoard.getPiece(yCounter2, xCounter2) == QUEEN)) {
                coords = Integer.toString(xCounter2) + Integer.toString(yCounter2) + Integer.toString(x) + Integer.toString(y);
                if (isMoveLegal(coords,colour)) {
                    result = true;
                }
            }
            xCounter2++;
            yCounter2++;
        }

        // check horizontals
        
        //x axis:
        for (int temp = 0; temp < 8; temp ++) {
            if (temp != y &&(gameBoard.getPiece(y, temp) == ROOK || gameBoard.getPiece(y,temp) == QUEEN)) {
                coords = Integer.toString(temp) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
                if (isMoveLegal(coords,colour)) {
                    result = true;
                }
            }
        }

        //y axis:
        for (int temp = 0; temp < 8; temp ++) {
            if (temp != x &&(gameBoard.getPiece(temp, x) == ROOK || gameBoard.getPiece(temp, x) == QUEEN)) {
                coords = Integer.toString(x) + Integer.toString(temp) + Integer.toString(x) + Integer.toString(y);
                if (isMoveLegal(coords,colour)) {
                    result = true;
                }
            }
        }

        // horses

        //I apologise for my god-awful formatting.
        int[] temp = {-2,-1,1,2};

        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                if (gameBoard.getPiece(y + temp[j], x + temp[i]) == KNIGHT) { 
                    coords = Integer.toString(x + temp[i]) + Integer.toString(y + temp[j]) + Integer.toString(x) + Integer.toString(y);
                    if (isMoveLegal(coords, colour)) {
                        result = true;
                    }
                }
            }
        }

        // pawns
 
        //checks for friendly pawns. i.e. reachablity without capture
        if (colour == "white" && isWhite(gameBoard.getPiece(y,x))) {
            if (gameBoard.getPiece(y + 1,x) == PAWN) {
                coords = Integer.toString(x) + Integer.toString(y + 1) + Integer.toString(x) + Integer.toString(y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
            if (gameBoard.getPiece(y + 2,x) == PAWN) {
                coords = Integer.toString(x) + Integer.toString(y + 2) + Integer.toString(x) + Integer.toString(y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
        } 
        else if (colour == "black" && isBlack(gameBoard.getPiece(y,x))) {
            if (gameBoard.getPiece(y - 1,x) == PAWN) {
                coords = Integer.toString(x) + Integer.toString(y - 1) + Integer.toString(x) + Integer.toString(y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
            if (gameBoard.getPiece(y - 2,x) == PAWN) {
                coords = Integer.toString(x) + Integer.toString(y - 2) + Integer.toString(x) + Integer.toString(y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
        }

        //checks for enemy pawns. i.e. reachablity through capture
        int[] tempArr = {-1,1};
        if (colour == "white" && !isWhite(gameBoard.getPiece(y,x))) {
            for (int i = 0; i < tempArr.length; i++) {
                for (int j = 0; j < tempArr.length; j++) {
                    if (gameBoard.getPiece(y + j, x + i) == PAWN) {
                        coords = Integer.toString(x + i) + Integer.toString(y + j) + Integer.toString(x) + Integer.toString(y);
                        if (isMoveLegal(coords, colour)) {
                            result = true;
                        }
                    }
                }
            }
        } 
        else if (colour == "black" && !isBlack(gameBoard.getPiece(y,x))) {
            for (int i = 0; i < tempArr.length; i++) {
                for (int j = 0; j < tempArr.length; j++) {
                    if (gameBoard.getPiece(y + j, x + i) == PAWN) {
                        coords = Integer.toString(x + i) + Integer.toString(y + j) + Integer.toString(x) + Integer.toString(y);
                        if (isMoveLegal(coords, colour)) {
                            result = true;
                        }
                    }
                }
            }
        }

        return result;
    }
}