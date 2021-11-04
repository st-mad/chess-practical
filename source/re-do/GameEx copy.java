import java.util.Scanner;
import java.util.ArrayList;

public class GameEx {
    // The following five constants were defined in the starter code (kt54)
    private static final char FREE = '.';
    private static final char WHITEROOK = 'R';
    private static final char BLACKROOK = 'r';
    private static final char WHITEBISHOP = 'B';
    private static final char BLACKBISHOP = 'b';

    // The following constants are not needed for the basic solution, but some
    // people might find them useful for extensions
    private static final char WHITEKING = 'K';
    private static final char BLACKKING = 'k';
    private static final char WHITEQUEEN = 'Q';
    private static final char BLACKQUEEN = 'q';
    private static final char WHITEKNIGHT = 'N';
    private static final char BLACKKNIGHT = 'n';
    private static final char WHITEPAWN = 'P';
    private static final char BLACKPAWN = 'p';

    // The following five constants were defined in the starter code (kt54)
    private static String WHITEPLAYS_MSG = "White plays. Enter move:";
    private static String BLACKPLAYS_MSG = "Black plays. Enter move:";
    private static String ILLEGALMOVE_MSG = "Illegal move!";
    private static String WHITEWINS_MSG = "White wins!";
    private static String BLACKWINS_MSG = "Black wins!";

    private BoardEx gameBoard;

    // here on are my own fields not started code (sms31)

    public PlayerEx blackPlayer = new PlayerEx("black");
    public PlayerEx whitePlayer = new PlayerEx("white");

    // Minimal constructor. Expand as needed (kt54)
    public GameEx() {
        gameBoard = new BoardEx();
    }

    // Build on this method to implement game logic.
    public void play() {

        Scanner reader = new Scanner(System.in);

        gameBoard = new BoardEx();

        // variables to track game state.
        int turnCount = 1;
        PlayerEx currentPlayer;
        boolean done = false;

        while (!done) {
            boolean isMoveLegal = false;
            // prints the board
            gameBoard.printBoard();

            // determines whose turn it is
            if (turnCount % 2 == 1) {
                currentPlayer = whitePlayer;
                System.out.println(WHITEPLAYS_MSG);
            } else {
                currentPlayer = blackPlayer;
                System.out.println(BLACKPLAYS_MSG);
            }

            currentPlayer.moveSet.clear();
            updateMoveSets();
            // debug code
            System.out.println("total legal moves: " + whitePlayer.moveSet.size() + " : " + blackPlayer.moveSet.size());
            
                /*
                 * for (int i = 0; i < totalLegalMovesWhite.size(); i++) {
                 * System.out.println(totalLegalMovesWhite.get(i)); }
                 */

            /* else {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (isBlack(gameBoard.getPiece(j, i))) {

                            // debug code
                            System.out.println(gameBoard.getPiece(j, i) + " piece " + generateLegalMoves(i, j).size());

                            for (int k = 0; k < generateLegalMoves(i, j).size(); k++) {
                                System.out.println(generateLegalMoves(i, j).get(k));
                            }

                            currentPlayer.moveSet.addAll(generateLegalMoves(i, j));
                        }
                    }
                }

                // debug code
                System.out.println("total legal moves: " + currentPlayer.moveSet.size());
                /*
                 * for (int i = 0; i < totalLegalMovesBlack.size(); i++) {
                 * System.out.println(totalLegalMovesBlack.get(i)); }
                 *
            }
            */

            // check if current player is in check
            currentPlayer.isInCheck = isInCheck(currentPlayer);
            System.out.println(currentPlayer.isInCheck + " : " + currentPlayer.kingX + "," + currentPlayer.kingY + " : " + currentPlayer.moveSet.size());

            if (currentPlayer.isInCheck) {
                // restrict moves in check
                PlayerEx simulatePlayer = new PlayerEx(currentPlayer.colour);
                simulatePlayer = currentPlayer;
                System.out.println("simulation" + simulatePlayer.colour + " : " + simulatePlayer.moveSet.size());



                int lengthAtStart = currentPlayer.moveSet.size();
                for (int movesChecked = 0, movesLeft = 0; movesChecked < lengthAtStart; movesChecked++) {
                    String move = currentPlayer.moveSet.get(movesLeft);
                    if (isMoveLegal(move, currentPlayer.colour)) {
                        int targetPositionX = (int) move.charAt(2) - 'a';
                        int targetPositionY = 8 - ((int) move.charAt(3) - '0');

                        char buffer = gameBoard.getPiece(targetPositionY, targetPositionX);

                        movePiece(move);

                        System.out.println(movesChecked + " " +  move + " " + isInCheck(currentPlayer));
                        if (isInCheck(currentPlayer)) {
                            currentPlayer.moveSet.remove(movesLeft);
                        } else {
                            movesLeft++;
                            System.out.println("DANGERZONE");
                        }
                        String inverseMove = "" + move.charAt(2) + move.charAt(3) + move.charAt(0) + move.charAt(1);
                        movePiece(inverseMove);
                        gameBoard.setPiece(targetPositionY, targetPositionX, buffer);

                    }
                }
                System.out.println("dick and balls: " + currentPlayer.moveSet.size());
                /*for (int i = 0; i < currentPlayer.moveSet.size(); i++) {
                    System.out.println(currentPlayer.moveSet.get(i));
                }*/
            }

            // takes user input
            String pos1 = reader.nextLine();
            if (pos1.equals("quit")) {
                done = true;
                break;
            }
            String pos2 = reader.nextLine();

            // makes the user input independent of capitalisation
            pos1 = pos1.toLowerCase();
            pos2 = pos2.toLowerCase();

            // converts the user input from algebraic notation to the their value in terms
            // of integer coordinates on the board.
            String coordinates = pos1 + pos2;

            // checking to avoid index out of bounds exceptions
            /*
             * if (coordinates.length() == 4) { // checks the legality of the move
             * isMoveLegal = isMoveLegal(coordinates, currentPlayer); }
             */

            for (int i = 0; i < currentPlayer.moveSet.size(); i++) {
                if (coordinates.equals(currentPlayer.moveSet.get(i))) {
                    isMoveLegal = true;
                }
            }

            // executes the move
            if (isMoveLegal) {
                movePiece(coordinates);
                turnCount++;
            } else {
                System.out.println(ILLEGALMOVE_MSG);
            }

            // naive win condition.
            switch (checkGameOver()) {
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

    public String convertToAlgebraic(int initialPositionX, int initialPositionY, int targetPositionX, int targetPositionY) {
        // this takes integer arguments and creates a result that fits the algebraic
        // notation of representing chess moves ex: a4b5.

        String result = "" + (char) (initialPositionX + 'a') + (char) (8 - initialPositionY + '0')
                + (char) (targetPositionX + 'a') + (char) (8 - targetPositionY + '0');
        return result;
    }

    public boolean isMoveLegal(String coordinates, String currentPlayer) {
        // components of move validity
        boolean isPathValid = false;
        boolean isRuleBound = false;
        boolean isColourCorrect = false;
        boolean isTargetValid = false;
        boolean isOutofBounds = false;

        // converts algebraic notation into 0-7 inclusive x,y coordinates
        int initialPositionX = (int) coordinates.charAt(0) - 'a';
        int initialPositionY = 8 - ((int) coordinates.charAt(1) - '0');
        int targetPositionX = (int) coordinates.charAt(2) - 'a';
        int targetPositionY = 8 - ((int) coordinates.charAt(3) - '0');

        // checks if the move is out of bounds
        isOutofBounds = (targetPositionX < 0 || targetPositionX >= 8) | (initialPositionX < 0 || initialPositionX >= 8)
                | (targetPositionY < 0 || targetPositionY >= 8) | (initialPositionY < 0 || initialPositionY >= 8);

        if (isOutofBounds) {
            return false;
        }

        // checks if the move is rule bound to the pieces moveset
        switch (gameBoard.getPiece(initialPositionY, initialPositionX)) {
        case BLACKBISHOP:
            // checks paths
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY);
            isRuleBound = (Math.abs(targetPositionY - initialPositionY) == Math
                    .abs(targetPositionX - initialPositionX));
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEBISHOP:
            // checks path
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY);
            isRuleBound = (Math.abs(targetPositionY - initialPositionY) == Math
                    .abs(targetPositionX - initialPositionX));
            isColourCorrect = currentPlayer.equals("white");
            break;
        case BLACKROOK:
            // checks path
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY);
            isRuleBound = (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEROOK:
            // checks paths
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY);
            isRuleBound = (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
            isColourCorrect = currentPlayer.equals("white");
            break;
        case BLACKPAWN:
            isPathValid = true; // bypasses the pathchecking

            // diagonal capture if the target is white while the main piece is black.
            if (Math.abs(targetPositionX - initialPositionX) == 1 && targetPositionY == initialPositionY + 1) {
                isRuleBound = isWhite(gameBoard.getPiece(targetPositionY, targetPositionX));
            }
            // if the target is empty, only allow the pawn to move 1 square down (because
            // its black).
            else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY + 1) {
                isRuleBound = (gameBoard.getPiece(targetPositionY, targetPositionX) == '.');
            }
            // if the initialPositionY is the initial position of the pawn, then it also
            // allows you to move down 2 squares
            else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY + 2) {
                isRuleBound = (gameBoard.getPiece(targetPositionY, targetPositionX) == '.') && initialPositionY == 1;
            }
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEPAWN:
            isPathValid = true;// bypasses the pathchecking

            // diagonal capture if the target is white while the main piece is black.
            if (Math.abs(targetPositionX - initialPositionX) == 1 && targetPositionY == initialPositionY - 1) {
                isRuleBound = isBlack(gameBoard.getPiece(targetPositionY, targetPositionX));
            }
            // if the target is empty, only allow the pawn to move 1 square up (because its
            // white).
            else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY - 1) {
                isRuleBound = (gameBoard.getPiece(targetPositionY, targetPositionX) == '.');
            }
            // if the initialPositionY is the initial position of the pawn, then it also
            // allows you to move up 2 squares
            else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY - 2) {
                isRuleBound = (gameBoard.getPiece(targetPositionY, targetPositionX) == '.') && initialPositionY == 6;
            }
            isColourCorrect = currentPlayer.equals("white");
            break;
        case BLACKKNIGHT:
            // checks path
            isPathValid = true;

            // rule set for knights
            isRuleBound = (Math.abs(targetPositionX - initialPositionX) == 1
                    && Math.abs(targetPositionY - initialPositionY) == 2)
                    || (Math.abs(targetPositionX - initialPositionX) == 2
                            && Math.abs(targetPositionY - initialPositionY) == 1);
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEKNIGHT:
            // checks paths
            isPathValid = true;

            // rule set for knights
            isRuleBound = (Math.abs(targetPositionX - initialPositionX) == 1
                    && Math.abs(targetPositionY - initialPositionY) == 2)
                    || (Math.abs(targetPositionX - initialPositionX) == 2
                            && Math.abs(targetPositionY - initialPositionY) == 1);
            isColourCorrect = currentPlayer.equals("white");
            break;
        case BLACKQUEEN:
            // checks paths
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY);

            // combines the rules for bishop and rook
            isRuleBound = (Math.abs(targetPositionY - initialPositionY) == Math.abs(targetPositionX - initialPositionX))
                    || (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEQUEEN:
            // checks path
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY);

            // combines the rules for bishop and rook
            isRuleBound = (Math.abs(targetPositionY - initialPositionY) == Math.abs(targetPositionX - initialPositionX))
                    || (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
            isColourCorrect = currentPlayer.equals("white");
            break;
        case BLACKKING:
            // checks paths
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY);
            isRuleBound = (Math.abs(targetPositionX - initialPositionX) == 1
                    || Math.abs(targetPositionY - initialPositionY) == 1);
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEKING:
            // checks path
            isPathValid = true;
            isRuleBound = (Math.abs(targetPositionX - initialPositionX) == 1
                    || Math.abs(targetPositionY - initialPositionY) == 1);
            isColourCorrect = currentPlayer.equals("white");
            break;
        }

        // captures
        if (currentPlayer.equals("black") && isWhite(gameBoard.getPiece(targetPositionY, targetPositionX))) {
            isTargetValid = true;
        } else if (currentPlayer.equals("white") && isBlack(gameBoard.getPiece(targetPositionY, targetPositionX))) {
            isTargetValid = true;
        } else if (gameBoard.getPiece(targetPositionY, targetPositionX) == '.') {
            isTargetValid = true;
        }

        // debug code
        // System.out.println(isPathValid + ": " + isRuleBound + ": " + isColourCorrect
        // + ": " + isTargetValid);
        boolean isMoveValid = isPathValid && isRuleBound && isColourCorrect && isTargetValid;
        return isMoveValid;
    }

    public void movePiece(String coordinates) {
        // converts integer string into ints
        int initialPositionX = (int) coordinates.charAt(0) - 'a';
        int initialPositionY = 8 - ((int) coordinates.charAt(1) - '0');
        int targetPositionX = (int) coordinates.charAt(2) - 'a';
        int targetPositionY = 8 - ((int) coordinates.charAt(3) - '0');

        // tracks position of the king in a field.
        if (gameBoard.getPiece(initialPositionY, initialPositionX) == 'k') {
            blackPlayer.kingX = targetPositionX;
            blackPlayer.kingY = targetPositionY;
        }

        // tracks position of the king in a field.
        if (gameBoard.getPiece(initialPositionY, initialPositionX) == 'K') {
            whitePlayer.kingX = targetPositionX;
            whitePlayer.kingY = targetPositionY;
        }

        // actual swap
        char buffer = gameBoard.getPiece(initialPositionY, initialPositionX);
        gameBoard.setPiece(targetPositionY, targetPositionX, buffer);
        gameBoard.setPiece(initialPositionY, initialPositionX, FREE);

    }

    public char checkGameOver() {
        // implement checks later.
        int numberOfBlackPieces = 0;
        int numberOfWhitePieces = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char temp = gameBoard.getPiece(i, j);
                if (temp != '.' && Character.isUpperCase(temp)) {
                    numberOfWhitePieces++;
                } else if (temp != '.' && Character.isLowerCase(temp)) {
                    numberOfBlackPieces++;
                }
            }
        }

        if (numberOfBlackPieces == 0)
            return 'w';
        if (numberOfWhitePieces == 0)
            return 'b';
        return 'n';
    }

    public boolean checkPath(int x1, int y1, int x2, int y2) {
        // I have a slight suspicion that this is overcomplicated, and might not be the
        // best solution.
        // Increment X,Y determine the direction that the target square is from the
        // initial square.
        int incrementX = 0;
        int incrementY = 0;

        // this makes the value either -1, 0, or 1
        if (x1 != x2)
            incrementX = (x2 - x1) / (Math.abs(x2 - x1));
        if (y1 != y2)
            incrementY = (y2 - y1) / (Math.abs(y2 - y1));

        // the algorithm starts one square in the direction of the target and checks if
        // the square is empty, before incrementing i,j with incrementx or increment y
        // respectively.
        // Each step the while loop moves closer to the target, until it is on the
        // target. If a square is not empty it returns false to indicate that the path
        // is not valid.
        int i = x1 + incrementX;
        int j = y1 + incrementY;
        while ((i != x2) || (j != y2)) {

            // System.out.println(y2 + "," + j + " : " + x2 + "," + i + " : "+
            // gameBoard.getPiece(j,i)); debug
            if (gameBoard.getPiece(j, i) != '.') {
                return false;
            }

            if (i != x2) {
                i = i + incrementX;
            }
            if (j != y2) {
                j = j + incrementY;
            }
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
        // I understand that his approach is very inefficient, but with the current
        // structure of my code, I can't think of any better way to do it.
        boolean result = false;
        String coords;
        System.out.println(x + " " + y);

        char BISHOP = 'b';
        char QUEEN = 'q';
        char ROOK = 'r';
        char KNIGHT = 'k';
        char PAWN = 'p';

        if (colour.equals("white")) {
            BISHOP = 'B';
            QUEEN = 'Q';
            ROOK = 'R';
            KNIGHT = 'K';
            PAWN = 'P';
        }
        // check diagonals

        // diag 1
        // for the diagonal going from bottom left to top right, the equation in terms
        // of game coordinates should be y = -x + (x1 + y1), where (x1.y1) is the point
        // the line passes through.
        // the intersections of this line with the bottom of the board occurs when y =
        // 7. so the starting point will be y = 7 and x = -7 + (x1 + y1)
        int xCounter1 = -7 + x + y;
        int yCounter1 = 7;

        while (xCounter1 != 8 && xCounter1 != -1 && yCounter1 != 8 && yCounter1 != -1) {
            if ((xCounter1 != x && yCounter1 != y) && (gameBoard.getPiece(yCounter1, xCounter1) == BISHOP
                    || gameBoard.getPiece(yCounter1, xCounter1) == QUEEN)) {
                coords = convertToAlgebraic(xCounter1, yCounter1, x, y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
            xCounter1++;
            yCounter1--;
        }

        // diag 2
        // same theory as for the last one, but here the line is of slope +1 and it
        // intersects with y = 0
        int xCounter2 = 0;
        int yCounter2 = y - x;
        while (xCounter2 != 8 && xCounter2 != -1 && yCounter2 != 8 && yCounter2 != -1) {
            if ((xCounter2 != x && yCounter2 != y) && (gameBoard.getPiece(yCounter2, xCounter2) == BISHOP
                    || gameBoard.getPiece(yCounter2, xCounter2) == QUEEN)) {
                coords = convertToAlgebraic(xCounter2, yCounter2, x, y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
            xCounter2++;
            yCounter2++;
        }

        // check horizontals

        // x axis:
        for (int temp = 0; temp < 8; temp++) {
            if (temp != y && (gameBoard.getPiece(y, temp) == ROOK || gameBoard.getPiece(y, temp) == QUEEN)) {
                coords = convertToAlgebraic(temp, y, x, y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
        }

        // y axis:
        for (int temp = 0; temp < 8; temp++) {
            if (temp != x && (gameBoard.getPiece(temp, x) == ROOK || gameBoard.getPiece(temp, x) == QUEEN)) {
                coords = convertToAlgebraic(x, temp, x, y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
        }

        // horses

        // I apologise for my god-awful formatting.
        int[] temp = { -2, -1, 1, 2 };

        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                if ((y + temp[j] != 8) && (x + temp[i] != 8) && (y + temp[j] != -1) && (x + temp[i] != -1)) {
                    if (gameBoard.getPiece(y + temp[j], x + temp[i]) == KNIGHT) {
                        coords = convertToAlgebraic(x + temp[i], y + temp[j], x, y);
                        if (isMoveLegal(coords, colour)) {
                            result = true;
                        }
                    }
                }
            }
        }

        // pawns

        // checks for friendly pawns. i.e. reachablity without capture
        if (colour == "white" && isWhite(gameBoard.getPiece(y, x))) {
            if (((y + 1 != 8) && (y + 1 != -1) && (x != 8) && (x != -1)) && (gameBoard.getPiece(y + 1, x) == PAWN)) {
                coords = convertToAlgebraic(x, y + 1, x, y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
            if (((y + 2 != 8) && (y + 2 != -1) && (x != 8) && (x != -1)) && (gameBoard.getPiece(y + 2, x) == PAWN)) {
                coords = convertToAlgebraic(x, y + 2, x, y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
        } else if (colour == "black" && isBlack(gameBoard.getPiece(y, x))) {
            if (((y - 1 != 8) && (y - 1 != -1) && (x != 8) && (x != -1)) && (gameBoard.getPiece(y - 1, x) == PAWN)) {
                coords = convertToAlgebraic(x, y - 1, x, y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
            if (((y - 2 != 8) && (y - 2 != -1) && (x != 8) && (x != -1)) && (gameBoard.getPiece(y - 2, x) == PAWN)) {
                coords = convertToAlgebraic(x, y - 2, x, y);
                if (isMoveLegal(coords, colour)) {
                    result = true;
                }
            }
        }

        // checks for enemy pawns. i.e. reachablity through capture
        int[] tempArr = { -1, 1 };
        if (colour == "white" && !isWhite(gameBoard.getPiece(y, x))) {
            for (int i = 0; i < tempArr.length; i++) {
                for (int j = 0; j < tempArr.length; j++) {
                    if (((y + tempArr[j] != 8) && (y + tempArr[j] != -1) && (x + tempArr[i] != 8)
                            && (x + tempArr[i] != -1))
                            && (gameBoard.getPiece(y + tempArr[j], x + tempArr[i]) == PAWN)) {
                        coords = convertToAlgebraic(x + tempArr[i], y + tempArr[j], x, y);
                        if (isMoveLegal(coords, colour)) {
                            result = true;
                        }
                    }
                }
            }
        } else if (colour == "black" && !isBlack(gameBoard.getPiece(y, x))) {
            for (int i = 0; i < tempArr.length; i++) {
                for (int j = 0; j < tempArr.length; j++) {
                    if (((y + tempArr[j] != 8) && (y + tempArr[j] != -1) && (x + tempArr[i] != 8)
                            && (x + tempArr[i] != -1))
                            && (gameBoard.getPiece(y + tempArr[j], x + tempArr[i]) == PAWN)) {
                        coords = convertToAlgebraic(x + tempArr[i], y + tempArr[j], x, y);
                        if (isMoveLegal(coords, colour)) {
                            result = true;
                        }
                    }
                }
            }
        }

        return result;
    }

    public boolean isInCheck(PlayerEx currentPlayer) {
        boolean result = false;

        if (currentPlayer.colour.equals("black")) {
            for (int i = 0; i < whitePlayer.moveSet.size(); i++) {
                String move = whitePlayer.moveSet.get(i);
                int targetX = move.charAt(2) - 'a';
                int targetY = 8 - (move.charAt(3) - '0');

                if (targetX == blackPlayer.kingX && targetY == blackPlayer.kingY) {
                    result = true;
                }
            }
        } else if (currentPlayer.colour.equals("white")) {
            for (int i = 0; i < blackPlayer.moveSet.size(); i++) {
                String move = blackPlayer.moveSet.get(i);
                int targetX = move.charAt(2) - 'a';
                int targetY = 8 - (move.charAt(3) - '0');
                //debug
                //System.out.println("-> " + targetX + "," + targetY + "||" + currentPlayer.kingX + "," + currentPlayer.kingY + "||" + whitePlayer.kingX + "," + whitePlayer.kingY);
                if (targetX == whitePlayer.kingX && targetY == whitePlayer.kingY) {
                    result = true;
                }
            }
        }

        return result;
    }

    public ArrayList<String> generateLegalMoves(int x, int y) {
        ArrayList<String> legalMoves = new ArrayList<String>();
        int xTarget;
        int yTarget;
        String coords;

        switch (gameBoard.getPiece(y, x)) {
        case BLACKBISHOP:
            xTarget = -7 + x + y;
            yTarget = 7;
            while (xTarget != 8 && xTarget != -1 && yTarget != 8 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget--;
            }

            xTarget = 0;
            yTarget = y - x;
            while (xTarget != 8 && xTarget != -1 && yTarget != 8 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget++;
            }
            break;

        case WHITEBISHOP:
            xTarget = -7 + x + y;
            yTarget = 7;
            while (xTarget != 8 && xTarget != -1 && yTarget != 8 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget--;
            }

            xTarget = 0;
            yTarget = y - x;
            while (xTarget != 8 && xTarget != -1 && yTarget != 8 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget++;
            }
            break;

        case BLACKROOK:
            // x axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, i, y);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
            }

            // y axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, x, i);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
            }
            break;

        case WHITEROOK:
            // x axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, i, y);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
            }

            // y axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, x, i);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
            }
            break;

        case BLACKQUEEN:
            xTarget = -7 + x + y;
            yTarget = 7;
            while (xTarget != 8 && xTarget != -1 && yTarget != 8 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget--;
            }

            xTarget = 0;
            yTarget = y - x;
            while (xTarget != 8 && xTarget != -1 && yTarget != 8 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                System.out.println( x + y + " " + xTarget + " " + yTarget + " " + " pe " + coords);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget++;
            }

            // x axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, i, y);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
            }

            // y axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, x, i);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
            }

            break;

        case WHITEQUEEN:
            xTarget = -7 + x + y;
            yTarget = 7;
            while (xTarget != 8 && xTarget != -1 && yTarget != 8 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget--;
            }

            xTarget = 0;
            yTarget = y - x;
            while (xTarget != 8 && xTarget != -1 && yTarget != 8 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget++;
            }

            // x axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, i, y);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
            }

            // y axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, x, i);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
            }

            break;

        case BLACKPAWN:
            for (int i = -1; i <= 1; i++) {
                coords = convertToAlgebraic(x, y, x + i, y + 1);
                if (isMoveLegal(coords, "black")) {
                    legalMoves.add(coords);
                }
                // checks for 2 step move only when the pawn is at its initial y position.
                if (y == 1) {
                    coords = convertToAlgebraic(x, y, x + i, y + 2);
                    if (isMoveLegal(coords, "black")) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;
        case WHITEPAWN:
            for (int i = -1; i <= 1; i++) {
                coords = convertToAlgebraic(x, y, x + i, y - 1);
                if (isMoveLegal(coords, "white")) {
                    legalMoves.add(coords);
                }
                // checks for 2 step move only when the pawn is at its initial y position.
                if (y == 6) {
                    coords = convertToAlgebraic(x, y, x + i, y - 2);
                    if (isMoveLegal(coords, "white")) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;

        case BLACKKNIGHT:
            int[] arrayOfCoordinatesBlack = { -2, -1, 1, 2 };
            for (int i = 0; i < arrayOfCoordinatesBlack.length; i++) {
                for (int j = 0; j < arrayOfCoordinatesBlack.length; j++) {
                    coords = convertToAlgebraic(x, y, x + arrayOfCoordinatesBlack[i], y + arrayOfCoordinatesBlack[j]);
                    if (isMoveLegal(coords, "black")) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;
        case WHITEKNIGHT:
            int[] arrayOfCoordinatesWhite = { -2, -1, 1, 2 };
            for (int i = 0; i < arrayOfCoordinatesWhite.length; i++) {
                for (int j = 0; j < arrayOfCoordinatesWhite.length; j++) {
                    coords = convertToAlgebraic(x, y, x + arrayOfCoordinatesWhite[i], y + arrayOfCoordinatesWhite[j]);
                    if (isMoveLegal(coords, "white")) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;

        case BLACKKING:
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    coords = convertToAlgebraic(x, y, x + i, y + j);
                    if (isMoveLegal(coords, "black")) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;
        case WHITEKING:
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    coords = convertToAlgebraic(x, y, x + i, y + j);
                    if (isMoveLegal(coords, "white")) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;
        }

        return legalMoves;
    }

    public void updateMoveSets() {
        // generates legal moves for the current player
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isWhite(gameBoard.getPiece(j, i))) {

                    // debug code
                    
                    System.out.println(gameBoard.getPiece(j, i) + " piece " + generateLegalMoves(i, j).size());

                    for (int k = 0; k < generateLegalMoves(i, j).size(); k++) {
                        System.out.println(generateLegalMoves(i, j).get(k));
                    }
                    
                    whitePlayer.moveSet.addAll(generateLegalMoves(i, j));

                } else if (isBlack(gameBoard.getPiece(j, i))) {
                    // debug code
                    
                    System.out.println(gameBoard.getPiece(j, i) + " piece " + generateLegalMoves(i, j).size());

                    for (int k = 0; k < generateLegalMoves(i, j).size(); k++) {
                        System.out.println(generateLegalMoves(i, j).get(k));
                    }
                    
                    blackPlayer.moveSet.addAll(generateLegalMoves(i, j));
                }
            }
        }


        

    }
}