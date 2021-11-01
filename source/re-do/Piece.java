import java.util.ArrayList;

public class Piece {
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

    char type;
    int positionX;
    int positionY;
    Board gameBoard;
    ArrayList<String> legalMoves;

    public void generateLegalMoves() {
    
    }

    public boolean isMoveLegal(String coordinates, String currentPlayer) {
        boolean isPathValid = false;
        boolean isRuleBound = false;
        boolean isColourCorrect = false;
        boolean isTargetValid = false;
        boolean isOutofBounds = false;
        
        //converts algebraic notation into 0-7 inclusive x,y coordinates
        int initialPositionX = (int) coordinates.charAt(0) - 97;
        int initialPositionY = 8 - ((int) coordinates.charAt(1) - 48);
        int targetPositionX = (int) coordinates.charAt(2) - 97;
        int targetPositionY = 8 - ((int) coordinates.charAt(3) - 48);

        /*int initialPositionX = (int)coordinates.charAt(0) - '0';
        int initialPositionY = (int)coordinates.charAt(1) - '0';
        int targetPositionX = (int)coordinates.charAt(2) - '0';
        int targetPositionY = (int)coordinates.charAt(3) - '0';*/

        //System.out.println(initialPositionX + " " + initialPositionY + " " + targetPositionX + " " + targetPositionY);//debug code
        isOutofBounds = (targetPositionX < 0 || targetPositionX >= 8) | (initialPositionX < 0 || initialPositionX >= 8) |
                        (targetPositionY < 0 || targetPositionY >= 8) | (initialPositionY < 0 || initialPositionY >= 8); 

        if (isOutofBounds) {  
            return false;
        }
        
        //checks if the move is rule bound
        switch(gameBoard.getPiece(initialPositionY,initialPositionX).type) {
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
                    isRuleBound = isWhite(gameBoard.getPiece(targetPositionY,targetPositionX).type);
                }
                else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY + 1){
                    isRuleBound = (gameBoard.getPiece(targetPositionY,targetPositionX).type == '.');
                }
                else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY + 2){
                    isRuleBound = (gameBoard.getPiece(targetPositionY,targetPositionX).type == '.') && initialPositionY == 1;
                }
                isColourCorrect = currentPlayer.equals("black");
                break;
            case WHITEPAWN:
                isPathValid = true;
                if (Math.abs(targetPositionX - initialPositionX) == 1 && targetPositionY == initialPositionY - 1) {
                    isRuleBound = isBlack(gameBoard.getPiece(targetPositionY,targetPositionX).type);
                }
                else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY - 1){
                    isRuleBound = (gameBoard.getPiece(targetPositionY,targetPositionX).type == '.');
                } 
                else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY - 2){
                    isRuleBound = (gameBoard.getPiece(targetPositionY,targetPositionX).type == '.') && initialPositionY == 6;
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
        if (currentPlayer.equals("black") && isWhite(gameBoard.getPiece(targetPositionY,targetPositionX).type)) {
            isTargetValid = true;
        } else if (currentPlayer.equals("white") && isBlack(gameBoard.getPiece(targetPositionY,targetPositionX).type)) {
            isTargetValid = true;
        } else if (gameBoard.getPiece(targetPositionY,targetPositionX).type == '.') {
            isTargetValid = true;
        }

        //System.out.println("" + isPathValid + isRuleBound + isColourCorrect + isTargetValid);//debug line
        boolean isMoveValid = isPathValid && isRuleBound && isColourCorrect && isTargetValid;
        return isMoveValid;
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
            if (gameBoard.getPiece(j,i).type != '.') {
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
