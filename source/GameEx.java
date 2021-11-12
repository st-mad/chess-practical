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
    private static String STALEMATE = "Stalemate!";
    private static String CHECK = " is in check!";
    private static String CHECKMATE = "Checkmate!";

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

        String game = new String();
        game = "Start";

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

            // win condition. loser = 'n' if game is still in progress.
            String loser = checkGameOver(currentPlayer);
            switch (loser.charAt(0)) {
            case 'w':
                System.out.println(CHECKMATE);
                System.out.println(BLACKWINS_MSG);
                done = true;
                break;
            case 'b':
                System.out.println(CHECKMATE);
                System.out.println(WHITEWINS_MSG);
                done = true;
                break;
            case 's':
                System.out.println(STALEMATE);
                done = true;
                break;

            }
            if (done) {
                System.out.println(game);
                break;
            }

            // takes user input

            // current player plays message
            if (currentPlayer.colour.equals("white")) {
                System.out.println(WHITEPLAYS_MSG);
            } else {
                System.out.println(BLACKPLAYS_MSG);
            }

            // displays check message
            if (currentPlayer.isInCheck) {
                System.out.println(currentPlayer.colour + CHECK);
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

            // This iterates through the set of legal moves that a player has. This allows
            // me to restrict moves based on check, instead of just validating moves as they
            // are entered.
            for (int i = 0; i < currentPlayer.moveSet.size(); i++) {
                if (coordinates.equals(currentPlayer.moveSet.get(i))) {
                    isMoveLegal = true;
                }
            }

            // executes the move
            if (isMoveLegal) {
                gameBoard.movePiece(coordinates, blackPlayer, whitePlayer);
                game = game + ", " + coordinates;
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
        boolean result = false;

        boolean isPathValid = false;
        boolean isColourCorrect = false;
        boolean isTargetValid = false;
        boolean isOutofBounds = false;

        // converts algebraic notation into 0-7 inclusive x,y coordinates
        int initialPositionX = (int) coordinates.charAt(0) - 'a';
        int initialPositionY = 8 - ((int) coordinates.charAt(1) - '0');
        int targetPositionX = (int) coordinates.charAt(2) - 'a';
        int targetPositionY = 8 - ((int) coordinates.charAt(3) - '0');

        isOutofBounds = (targetPositionX < 0 || targetPositionX >= 8) | (initialPositionX < 0 || initialPositionX >= 8)
                | (targetPositionY < 0 || targetPositionY >= 8) | (initialPositionY < 0 || initialPositionY >= 8);

        if (isOutofBounds) {
            return false;
        }

        if ('n' == board.getPiece(initialPositionY, initialPositionX) || 'N' == board.getPiece(initialPositionY, initialPositionX)) {
            isPathValid = true;
        } else {
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board);
        }
        
        if (currentPlayer.equals("white")) {
            isColourCorrect = currentPlayer.equals("white");
        } else {
            isColourCorrect = currentPlayer.equals("black");
        }

        // captures
        if (currentPlayer.equals("black") && isWhite(board.getPiece(targetPositionY, targetPositionX))) {
            isTargetValid = true;
        } else if (currentPlayer.equals("white") && isBlack(board.getPiece(targetPositionY, targetPositionX))) {
            isTargetValid = true;
        } else if (board.getPiece(targetPositionY, targetPositionX) == '.') {
            isTargetValid = true;
        }

        result = isPathValid && isColourCorrect && isTargetValid;
        return result;
    }


    public boolean isMoveLegalR(String coordinates, String currentPlayer, BoardEx board) {
        // this function could havee been reworked into the generateLegalMoves function,
        // but as I used this to implement the basic version of this practical, I chose
        // to keep this here.

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
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board); // bypasses the pathchecking

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
            isPathValid = checkPath(initialPositionX, initialPositionY, targetPositionX, targetPositionY, board);// bypasses the pathchecking

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

        boolean isMoveValid = isPathValid && isRuleBound && isColourCorrect && isTargetValid;
        return isMoveValid;
    }

   

    public String checkGameOver(PlayerEx currentPlayer) {
        // checks if the given player is in check, and if the player has no legal moves
        // left, they are checkmated.
        // this doesn't include additional stalemate conditions like stalemates by
        // repetition, etc.
        // if the player runs out of legal moves, and the kind isn't in check, the game
        // is a stalemate.
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
        if (x1 != x2) {
            incrementX = (x2 - x1) / (Math.abs(x2 - x1));
        }
        if (y1 != y2) {
            incrementY = (y2 - y1) / (Math.abs(y2 - y1));
        }
        // the algorithm starts one square in the direction of the target and checks if
        // the square is empty, before incrementing i,j with incrementx or increment y
        // respectively.
        // Each step the while loop moves closer to the target, until it is on the
        // target. If a square is not empty it returns false to indicate that the path
        // is not valid.
        int i = x1 + incrementX;
        int j = y1 + incrementY;
        while ((i != x2) || (j != y2)) {
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
        // This is a simple helper method that checks if the given piece is white.
        if (n <= 'Z' && n >= 'A') {
            return true;
        }
        return false;
    }

    public boolean isBlack(char n) {
        // This is a simple helper method that checks if the given piece is black.
        if (n <= 'z' && n >= 'a') {
            return true;
        }
        return false;
    }

    public boolean isInCheck(PlayerEx currentPlayer) {
        // this method iterates through the opposing players moveset, and checks if any
        // of their moves can capture the king's current position
        boolean result = false;

        PlayerEx otherPlayer = new PlayerEx();
        if (currentPlayer.colour.equals("white")) {
            otherPlayer = blackPlayer;
        } else {
            otherPlayer = whitePlayer;
        }

        // iterates through other player's moveset and finds a move that can capture the
        // current player's king's position.
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
        // I know array lists aren't covered in the course, but trying to use normal
        // arrays would've complicated it too much.
        ArrayList<String> legalMoves = new ArrayList<String>();
        int xTarget;
        int yTarget;
        String coords;

        // If I was not afraid of breaking my entire code, then I would remove
        // isMoveLegal here, and have this function handle move legality. But as I've
        // already written that function for the basic spec, I choose to do it this way.
        switch (board.getPiece(y, x)) {
        case BLACKBISHOP:
            // this code consists of basically 4 while loops that start from the position of
            // the bishop and in the 4 diagonal directions, checking if the bishop can move
            // there. I don't know if this is the correct term, but I visualise it as rays
            // being cast outward.

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
            // this code is identical to the one for the black bishop, except for the
            // "white" in the arguement for isMoveLegal.
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
            // This just checks all the values in the along the x axis in the same y
            // position as the ROOK, and then does the same for the y-axis.
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
            // this is just a copy of the rook and bishop code.
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
            // checks if the square infront of the pawn is legal. The definition of 'front'
            // is different for black and white, and is the eexplanation for the differences
            // in y - 1 and y + 1.
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
            // The moveset for knights contains the points that are offsets of + or - 1 and
            // 2 from the knight position. Like (1,2) , (2,1), (-2,1), etc.
            // To simplify this behaviour, I chose to use an array and loop through it twice
            // so that all the combinations of choosing and element are executed in each
            // iteration.
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
            // this uses a similar algorithm as the knight but the array is just -1,0,1, so
            // it can be done with only for loops and no array is required.
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    coords = convertToAlgebraic(x, y, x + i, y + j);
                    if (isMoveLegal(coords, "black", board)) {
                        legalMoves.add(coords);
                    }
                }
            }

            //castling
            if (blackPlayer.hasMovedKing == false) {
                if(board.getPiece(blackPlayer.kingY, blackPlayer.kingX - 1) == '.' && board.getPiece(blackPlayer.kingY, blackPlayer.kingX - 2) == '.' && board.getPiece(blackPlayer.kingY, blackPlayer.kingX - 3) == '.' && board.getPiece(blackPlayer.kingY, blackPlayer.kingX - 4) == BLACKROOK) {
                    coords = convertToAlgebraic(x, y, x - 2, y);
                    legalMoves.add(coords);
                }
                if(board.getPiece(blackPlayer.kingY, blackPlayer.kingX + 1) == '.' && board.getPiece(blackPlayer.kingY, blackPlayer.kingX + 2) == '.' && board.getPiece(blackPlayer.kingY, blackPlayer.kingX + 3) == BLACKROOK) {
                    coords = convertToAlgebraic(x, y, x + 2, y);
                    legalMoves.add(coords);
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

            //castling
            if (whitePlayer.hasMovedKing == false) {
                if(board.getPiece(whitePlayer.kingY, whitePlayer.kingX - 1) == '.' && board.getPiece(whitePlayer.kingY, whitePlayer.kingX - 2) == '.' && board.getPiece(whitePlayer.kingY, whitePlayer.kingX - 3) == '.' && board.getPiece(whitePlayer.kingY, whitePlayer.kingX - 4) == WHITEROOK) {
                    coords = convertToAlgebraic(x, y, x - 2, y);
                    legalMoves.add(coords);
                }
                if(board.getPiece(whitePlayer.kingY, whitePlayer.kingX + 1) == '.' && board.getPiece(whitePlayer.kingY, whitePlayer.kingX + 2) == '.' && board.getPiece(whitePlayer.kingY, whitePlayer.kingX + 3) == WHITEROOK) {
                    coords = convertToAlgebraic(x, y, x + 2, y);
                    legalMoves.add(coords);
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

                    // debug code: left in intentionally, as it was used to test the program
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
                    // debug code: left in intentionally, as it was used to test the program
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

    public void restrictPseudoLegalMoves(PlayerEx currentPlayer, BoardEx board) {
        // this function is also something I would rework.

        ArrayList<String> result = new ArrayList<String>();
        BoardEx simulatedBoard = new BoardEx();

        for (int i = 0; i < currentPlayer.moveSet.size(); i++) {
            // the act of simulating a move can resonably be put in a separate method in an
            // AI class, but as I have not yet implemented and AI, it is left here.

            // creates an independent copy of the gameboard, that can be used to simulate
            // moves.
            simulatedBoard.setBoard(board.getBoard());
            String move = currentPlayer.moveSet.get(i);

            // buffers to protect original values of these variables from move simulation
            ArrayList<String> legalMovesB = new ArrayList<String>(blackPlayer.moveSet);
            ArrayList<String> legalMovesW = new ArrayList<String>(whitePlayer.moveSet);
            int originalWhiteKingX = whitePlayer.kingX;
            int originalWhiteKingY = whitePlayer.kingY;
            int originalBlackKingX = blackPlayer.kingX;
            int originalBlackKingY = blackPlayer.kingY;

            // moves the piece on simulated board
            simulatedBoard.movePiece(move, blackPlayer, whitePlayer);

            // updates the move sets available to each player.
            updateMoveSet(simulatedBoard);

            // checks if the move leaves the player in check. if it does, that move is
            // illegal, as a player cannot move into check, or mmake a move such that they
            // are automatically in check for the next turn.
            if (!isInCheck(currentPlayer)) {
                result.add(move);
            }

            // returning variables to pre simulation values

            // returns the players pre-simulation moveset
            blackPlayer.moveSet.clear();
            whitePlayer.moveSet.clear();
            blackPlayer.moveSet.addAll(legalMovesB);
            whitePlayer.moveSet.addAll(legalMovesW);

            // returns the players king's to their original position
            whitePlayer.kingX = originalWhiteKingX;
            whitePlayer.kingY = originalWhiteKingY;
            blackPlayer.kingX = originalBlackKingX;
            blackPlayer.kingY = originalBlackKingY;

        }

        currentPlayer.moveSet = result;
    }
}
