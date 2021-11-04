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
            } else {
                currentPlayer = blackPlayer;
            }

            // update the available pseudo legal moves.
            updateMoveSet(gameBoard);

            // check if current player is in check
            currentPlayer.isInCheck = isInCheck(currentPlayer);

            // restrict moves in check
            restrictPseudoLegalMoves(currentPlayer, gameBoard);

            // win condition.
            String loser = checkGameOver(currentPlayer);
            switch (loser.charAt(0)) {
            case 'w':
                gameBoard.printBoard();
                System.out.println(BLACKWINS_MSG);
                done = true;
                break;
            case 'b':
                gameBoard.printBoard();
                System.out.println(WHITEWINS_MSG);
                done = true;
                break;
            case 's':
                gameBoard.printBoard();
                System.out.println("Stalemate");
                done = true;
                break;

            }

            // takes user input
            if (currentPlayer.colour.equals("white")) {
                System.out.println(WHITEPLAYS_MSG);
            } else {
                System.out.println(BLACKPLAYS_MSG);
            }

            String pos1 = reader.nextLine();
            if (pos1.equals("quit")) {
                done = true;
                break;
            }
            String pos2 = reader.nextLine();

            // makes the user input independent of capitalisations
            String coordinates = pos1 + pos2;
            coordinates = coordinates.toLowerCase();

            for (int i = 0; i < currentPlayer.moveSet.size(); i++) {
                if (coordinates.equals(currentPlayer.moveSet.get(i))) {
                    isMoveLegal = true;
                }
            }

            // executes the move
            if (isMoveLegal) {
                movePiece(coordinates, gameBoard);
                turnCount++;
            } else {
                System.out.println(ILLEGALMOVE_MSG);
            }

        }

        reader.close();
    }

    public String convertToAlgebraic(int initialPositionX, int initialPositionY, int targetPositionX,
            int targetPositionY) {
        // this takes integer arguments and creates a result that fits the algebraic
        // notation of representing chess moves ex: a4b5.

        String result = "" + (char) (initialPositionX + 'a') + (char) (8 - initialPositionY + '0')
                + (char) (targetPositionX + 'a') + (char) (8 - targetPositionY + '0');
        return result;
    }

    public boolean isMoveLegal(String coordinates, String currentPlayer, BoardEx board) {
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
        switch (board.getPiece(initialPositionY, initialPositionX)) {
        case BLACKBISHOP:
            // checks paths
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board);
            isRuleBound = (Math.abs(targetPositionY - initialPositionY) == Math
                    .abs(targetPositionX - initialPositionX));
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEBISHOP:
            // checks path
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board);
            isRuleBound = (Math.abs(targetPositionY - initialPositionY) == Math
                    .abs(targetPositionX - initialPositionX));
            isColourCorrect = currentPlayer.equals("white");
            break;
        case BLACKROOK:
            // checks path
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board);
            isRuleBound = (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEROOK:
            // checks paths
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board);
            isRuleBound = (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
            isColourCorrect = currentPlayer.equals("white");
            break;
        case BLACKPAWN:
            isPathValid = true; // bypasses the pathchecking

            // diagonal capture if the target is white while the main piece is black.
            if (Math.abs(targetPositionX - initialPositionX) == 1 && targetPositionY == initialPositionY + 1) {
                isRuleBound = isWhite(board.getPiece(targetPositionY, targetPositionX));
            }
            // if the target is empty, only allow the pawn to move 1 square down (because
            // its black).
            else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY + 1) {
                isRuleBound = (board.getPiece(targetPositionY, targetPositionX) == '.');
            }
            // if the initialPositionY is the initial position of the pawn, then it also
            // allows you to move down 2 squares
            else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY + 2) {
                isRuleBound = (board.getPiece(targetPositionY, targetPositionX) == '.') && initialPositionY == 1;
            }
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEPAWN:
            isPathValid = true;// bypasses the pathchecking

            // diagonal capture if the target is white while the main piece is black.
            if (Math.abs(targetPositionX - initialPositionX) == 1 && targetPositionY == initialPositionY - 1) {
                isRuleBound = isBlack(board.getPiece(targetPositionY, targetPositionX));
            }
            // if the target is empty, only allow the pawn to move 1 square up (because its
            // white).
            else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY - 1) {
                isRuleBound = (board.getPiece(targetPositionY, targetPositionX) == '.');
            }
            // if the initialPositionY is the initial position of the pawn, then it also
            // allows you to move up 2 squares
            else if (targetPositionX == initialPositionX && targetPositionY == initialPositionY - 2) {
                isRuleBound = (board.getPiece(targetPositionY, targetPositionX) == '.') && initialPositionY == 6;
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
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board);

            // combines the rules for bishop and rook
            isRuleBound = (Math.abs(targetPositionY - initialPositionY) == Math.abs(targetPositionX - initialPositionX))
                    || (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
            isColourCorrect = currentPlayer.equals("black");
            break;
        case WHITEQUEEN:
            // checks path
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board);

            // combines the rules for bishop and rook
            isRuleBound = (Math.abs(targetPositionY - initialPositionY) == Math.abs(targetPositionX - initialPositionX))
                    || (targetPositionX == initialPositionX || targetPositionY == initialPositionY);
            isColourCorrect = currentPlayer.equals("white");
            break;
        case BLACKKING:
            // checks paths
            isPathValid = true;
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
        if (currentPlayer.equals("black") && isWhite(board.getPiece(targetPositionY, targetPositionX))) {
            isTargetValid = true;
        } else if (currentPlayer.equals("white") && isBlack(board.getPiece(targetPositionY, targetPositionX))) {
            isTargetValid = true;
        } else if (board.getPiece(targetPositionY, targetPositionX) == '.') {
            isTargetValid = true;
        }

        // debug code
        // System.out.println(isPathValid + ": " + isRuleBound + ": " + isColourCorrect
        // + ": " + isTargetValid);
        boolean isMoveValid = isPathValid && isRuleBound && isColourCorrect && isTargetValid;
        return isMoveValid;
    }

    public void movePiece(String coordinates, BoardEx board) {
        // converts integer string into ints
        int initialPositionX = (int) coordinates.charAt(0) - 'a';
        int initialPositionY = 8 - ((int) coordinates.charAt(1) - '0');
        int targetPositionX = (int) coordinates.charAt(2) - 'a';
        int targetPositionY = 8 - ((int) coordinates.charAt(3) - '0');

        // tracks position of the king in a field.
        if (board.getPiece(initialPositionY, initialPositionX) == 'k') {
            blackPlayer.kingX = targetPositionX;
            blackPlayer.kingY = targetPositionY;
        }

        // tracks position of the king in a field.
        if (board.getPiece(initialPositionY, initialPositionX) == 'K') {
            whitePlayer.kingX = targetPositionX;
            whitePlayer.kingY = targetPositionY;
        }

        // actual swap
        char buffer = board.getPiece(initialPositionY, initialPositionX);
        board.setPiece(targetPositionY, targetPositionX, buffer);
        board.setPiece(initialPositionY, initialPositionX, FREE);

    }

    public String checkGameOver(PlayerEx currentPlayer) {
        if (currentPlayer.moveSet.size() == 0 && currentPlayer.isInCheck == true) {
            return currentPlayer.colour;
        } else if (currentPlayer.moveSet.size() == 0 && currentPlayer.isInCheck == false) {
            return "stalemate";
        }

        return "none";
    }

    public boolean checkPath(int x1, int y1, int x2, int y2, BoardEx board) {
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
            if (board.getPiece(j, i) != '.') {
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

    public boolean isInCheck(PlayerEx currentPlayer) {
        boolean result = false;

        PlayerEx otherPlayer = new PlayerEx();
        if (currentPlayer.colour.equals("white")) {
            otherPlayer = blackPlayer;
        } else {
            otherPlayer = whitePlayer;
        }

        // iterates through other player's moveset and finds a move that can capture the
        // king
        for (int i = 0; i < otherPlayer.moveSet.size(); i++) {
            String move = otherPlayer.moveSet.get(i);
            int targetX = move.charAt(2) - 'a';
            int targetY = 8 - (move.charAt(3) - '0');

            if (targetX == currentPlayer.kingX && targetY == currentPlayer.kingY) {
                result = true;
            }
        }

        return result;
    }

    public ArrayList<String> generateLegalMoves(int x, int y, BoardEx board) {
        ArrayList<String> legalMoves = new ArrayList<String>();
        int xTarget;
        int yTarget;
        String coords;

        switch (board.getPiece(y, x)) {
        case BLACKBISHOP:
            xTarget = x;
            yTarget = y;

            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget++;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                xTarget--;
                yTarget--;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget--;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                xTarget--;
                yTarget++;
            }
            break;

        case WHITEBISHOP:
            xTarget = x;
            yTarget = y;

            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget++;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                xTarget--;
                yTarget--;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget--;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                xTarget--;
                yTarget++;
            }
            break;

        case BLACKROOK:
            // x axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, i, y);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
            }

            // y axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, x, i);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
            }
            break;

        case WHITEROOK:
            // x axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, i, y);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
            }

            // y axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, x, i);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
            }
            break;

        case BLACKQUEEN:
            xTarget = x;
            yTarget = y;

            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget++;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                xTarget--;
                yTarget--;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget--;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                xTarget--;
                yTarget++;
            }

            // x axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, i, y);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
            }

            // y axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, x, i);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
            }

            break;

        case WHITEQUEEN:
            xTarget = x;
            yTarget = y;

            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget++;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                xTarget--;
                yTarget--;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                xTarget++;
                yTarget--;
            }

            xTarget = x;
            yTarget = y;
            while (xTarget != 8 && yTarget != 8 && xTarget != -1 && yTarget != -1) {
                coords = convertToAlgebraic(x, y, xTarget, yTarget);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                xTarget--;
                yTarget++;
            }

            // x axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, i, y);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
            }

            // y axis
            for (int i = 0; i < 8; i++) {
                coords = convertToAlgebraic(x, y, x, i);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
            }

            break;

        case BLACKPAWN:
            for (int i = -1; i <= 1; i++) {
                coords = convertToAlgebraic(x, y, x + i, y + 1);
                if (isMoveLegal(coords, "black", board)) {
                    legalMoves.add(coords);
                }
                // checks for 2 step move only when the pawn is at its initial y position.
                if (y == 1) {
                    coords = convertToAlgebraic(x, y, x + i, y + 2);
                    if (isMoveLegal(coords, "black", board)) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;
        case WHITEPAWN:
            for (int i = -1; i <= 1; i++) {
                coords = convertToAlgebraic(x, y, x + i, y - 1);
                if (isMoveLegal(coords, "white", board)) {
                    legalMoves.add(coords);
                }
                // checks for 2 step move only when the pawn is at its initial y position.
                if (y == 6) {
                    coords = convertToAlgebraic(x, y, x + i, y - 2);
                    if (isMoveLegal(coords, "white", board)) {
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
                    if (isMoveLegal(coords, "black", board)) {
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
                    if (isMoveLegal(coords, "white", board)) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;

        case BLACKKING:
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    coords = convertToAlgebraic(x, y, x + i, y + j);
                    if (isMoveLegal(coords, "black", board)) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;
        case WHITEKING:
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    coords = convertToAlgebraic(x, y, x + i, y + j);
                    if (isMoveLegal(coords, "white", board)) {
                        legalMoves.add(coords);
                    }
                }
            }
            break;
        }

        return legalMoves;
    }

    public void updateMoveSet(BoardEx board) {
        // generates legal moves for the current player
        whitePlayer.moveSet.clear();
        blackPlayer.moveSet.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isWhite(board.getPiece(j, i))) {

                    // debug code
                    /*
                     * System.out.println(board.getPiece(j, i) + " piece " + generateLegalMoves(i,
                     * j).size());
                     * 
                     * for (int k = 0; k < generateLegalMoves(i, j).size(); k++) {
                     * System.out.println(generateLegalMoves(i, j).get(k)); }
                     * 
                     */
                    whitePlayer.moveSet.addAll(generateLegalMoves(i, j, board));

                } else if (isBlack(board.getPiece(j, i))) {
                    // debug code
                    /*
                     * System.out.println(board.getPiece(j, i) + " piece " + generateLegalMoves(i,
                     * j).size());
                     * 
                     * for (int k = 0; k < generateLegalMoves(i, j).size(); k++) {
                     * System.out.println(generateLegalMoves(i, j).get(k)); }
                     * 
                     */
                    blackPlayer.moveSet.addAll(generateLegalMoves(i, j, board));

                }
            }
        }
    }
    /*
     * public void updateMoveSet(BoardEx board, ArrayList<String> moveSetBlack,
     * ArrayList<String> moveSetWhite) { //overloaded function to make simulation of
     * moves simpler // generates legal moves for the current player
     * moveSetWhite.clear(); moveSetBlack.clear(); for (int i = 0; i < 8; i++) { for
     * (int j = 0; j < 8; j++) { if (isWhite(board.getPiece(j, i))) {
     * 
     * // debug code
     * 
     * System.out.println(board.getPiece(j, i) + " piece " + generateLegalMoves(i,
     * j, board).size());
     * 
     * for (int k = 0; k < generateLegalMoves(i, j, board).size(); k++) {
     * System.out.println(generateLegalMoves(i, j, board).get(k)); }
     * 
     * 
     * moveSetWhite.addAll(generateLegalMoves(i, j, board));
     * 
     * } else if (isBlack(board.getPiece(j, i))) { // debug code
     * 
     * System.out.println(board.getPiece(j, i) + " piece " + generateLegalMoves(i,
     * j, board).size());
     * 
     * for (int k = 0; k < generateLegalMoves(i, j, board).size(); k++) {
     * System.out.println(generateLegalMoves(i, j, board).get(k)); }
     * 
     * 
     * moveSetBlack.addAll(generateLegalMoves(i, j, board)); } } } }
     */

    public void restrictPseudoLegalMoves(PlayerEx currentPlayer, BoardEx board) {
        ArrayList<String> result = new ArrayList<String>();
        BoardEx simulatedBoard = new BoardEx();

        for (int i = 0; i < currentPlayer.moveSet.size(); i++) {
            simulatedBoard.setBoard(board.getBoard());
            String move = currentPlayer.moveSet.get(i);

            // buffers to protect original values of these variables from move simulation
            ArrayList<String> legalMovesB = new ArrayList<String>(blackPlayer.moveSet);
            ArrayList<String> legalMovesW = new ArrayList<String>(whitePlayer.moveSet);
            int originalWhiteKingX = whitePlayer.kingX;
            int originalWhiteKingY = whitePlayer.kingY;
            int originalBlackKingX = blackPlayer.kingX;
            int originalBlackKingY = blackPlayer.kingY;

            movePiece(move, simulatedBoard);

            updateMoveSet(simulatedBoard);
            /*
             * System.out.println(move + " : : "); for (int j = 0; j <
             * blackPlayer.moveSet.size(); j++) {
             * System.out.print(blackPlayer.moveSet.get(j) + ", "); } System.out.println();
             */

            if (!isInCheck(currentPlayer)) {
                result.add(move);
            }

            // returning variables to pre simulation values
            blackPlayer.moveSet.clear();
            whitePlayer.moveSet.clear();
            blackPlayer.moveSet.addAll(legalMovesB);
            whitePlayer.moveSet.addAll(legalMovesW);
            whitePlayer.kingX = originalWhiteKingX;
            whitePlayer.kingY = originalWhiteKingY;
            blackPlayer.kingX = originalBlackKingX;
            blackPlayer.kingY = originalBlackKingY;

        }

        currentPlayer.moveSet = result;
    }
}