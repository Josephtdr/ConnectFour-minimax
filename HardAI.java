import java.util.List;
import java.util.stream.Collectors;

/**
 * More advanced idea for an ai implementing minimax with alpha/beta pruning 
 * and shallow search move sorting to complement alpha/beta pruning.
 * 
 * Rewards function favours in order:
 * winning, not losing, 3 in a row, playing in the middle column, 
 * two in a row, preventing opponent 3 in a row.
 */
public class HardAI extends Player{
    private char opColour;
    private int maxDepth;

    public HardAI(char colour, char opColour){
        name = "Hard AI";
        this.colour = colour;
        this.opColour = opColour;
    }

    /**
     * Finds the best move for the AI to play, via the minimax algorithm
     * 
     * @param gs
     *      The current gamestate of the runtime program
     * 
     * @return int representing the column of the best move
     */
    public int getInput(Gamestate gs){
        maxDepth = 10;
        float startTime = System.nanoTime();
        int bestMove = minimax(gs, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true)[1];
        float totalTime = (System.nanoTime() - startTime)/1000000000;
        System.out.printf("Ai played %d, from a depth of %d in ", (bestMove+1), maxDepth);
        System.out.print(totalTime + "seconds\n");
        return bestMove;
    }

    /**
     * The minimax algorithm. Iterates recursivly through the game,
     * until a terminal node is reached or the maximum provided depth is reached, 
     * One 'player' (the ai) aims to maximise the scores returned for each of 
     * the possible moves on their turn. The other 'player' (the opponent) aims to 
     * minimise the score on their turn.
     * 
     * Uses alpha/beta pruning along with a shallow search sorting algorithm
     * for performance speedups.  
     * 
     * @param gs
     *      The current gamestate of the minimax algorithm
     * @param depth
     *      The number of nodes remaing to traverse before stopping
     * @param alpha
     *      
     * @param beta
     *      
     * @param isMaximiser
     *      Indicates Whether the current 'player' is the maximiser or minimiser 
     * 
     * @return If a terminal node or maximum depth are reached, returns the 
     *         score of the current gamestate along with a dummy column.
     *         Otherwise, if maximising returns the maximum score along with 
     *         the respective best column, of the possible moves from the current gamestate. 
     *         Or, if minimising, the minimum score along with a dummy column.
     */
    private int[] minimax(Gamestate gs, int depth, int alpha, int beta, boolean isMaximiser){
        if (isTerminal(gs) || depth == 0){
            return new int[] {getScore(gs, depth), -1};
        }

        if (isMaximiser){
            Integer maxEval = Integer.MIN_VALUE;
            int bestMove = -1;
            for (Move move : getShallowSearchColumns(gs, isMaximiser)){
                int column = move.getColumn();

                gs.placeCounter(column, colour);
                int eval = minimax(gs, depth-1, alpha, beta, false)[0];
                gs.unplaceCounter(column);

                if (eval > maxEval){
                    maxEval = eval;
                    bestMove = move.getColumn();
                }

                alpha = Math.max(alpha, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return new int[] {maxEval, bestMove};
        }
        else {
            Integer maxEval = Integer.MAX_VALUE;
            for (Move move : getShallowSearchColumns(gs, isMaximiser)){
                int column = move.getColumn();

                gs.placeCounter(column, opColour);
                int eval = minimax(gs, depth-1, alpha, beta, true)[0];
                gs.unplaceCounter(column);
                maxEval = Math.min(maxEval, eval);

                beta = Math.min(beta, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return new int[] {maxEval, -1};
        }
    }

    /**
     * gets current playable moves and sorts them via a shallow search
     * to help maximise the amount of pruning in alpha beta pruning in minimax
     *
     * @param gs
     *      The current gamestate of the minimax algorithm
     * @param isMaximiser
     *      Indicates Whether the current 'player' is the maximiser or minimiser
     * 
     * @return An ArrayList of the sorted possible moves.
     */
    private List<Move> getShallowSearchColumns(Gamestate gs, boolean isMaximiser){
        char curColour = isMaximiser ? colour : opColour;
        
        return gs.getPossibleMoves().stream()
            .peek(i -> {
                gs.placeCounter(i.getColumn(), curColour);
                i.setScore( getScore(gs, 0) * (isMaximiser ? 1 : -1) ); 
                gs.unplaceCounter(i.getColumn());    
            }).sorted()
            .collect(Collectors.toList());
    }

    /**
     * 
     * @param gs
     *      The current gamestate of the minimax algorithm
     * @return true if either player has won or the board is full, false otherwise
     */
    private boolean isTerminal(Gamestate gs){
        return gs.isWin(colour) || gs.isWin(opColour) || gs.isFull();
    }

    /**
     * Calculates the score representation for a given gamestate (board),
     * offset by depth to favour winning sooner.
     * @param gs
     *      The current gamestate
     * @param depthRemaining
     *      The remaining depth in the minimax function until it stops searching
     * @return int representing the score of the current board
     */
    private int getScore(Gamestate gs, int depthRemaining){
        int score = 0;
        char[][] board = gs.getBoard();
        int depth = this.maxDepth - depthRemaining;

        if (gs.isWin(colour)){
            score = Integer.MAX_VALUE - depth;
        }
        else if (gs.isWin(opColour)){
            score = Integer.MIN_VALUE + depth;
        }
        else if (gs.isFull()){
            score = 0;
        }
        else {
            score += scoreMiddleColumn(board, colour); //favour playing in the centre
            score += scoreHorizontal(board, colour);
            score += scoreVertical(board, colour);
            score += scoreDiaganol(board, colour);

            score -= depth;
        }
        return score;
    }

    /**
     * Used to make the ai favour playing in the middle column.
     * By increasing the score for each of the given colour found
     * in the middle column.
     * @param board
     *      The current board for which to score
     * @param colour
     *      The colour that is being scored
     * @return int for the score given to the passed paramaters
     */
    private int scoreMiddleColumn(char[][] board, char colour){
        int score = 0;
        for(int row=0; row<board[0].length; row++){
            if (board[3][row] == colour){
                score += 3;
            }
        }
        return score;
    }

    /**
     * Loops through all possible horizontal blocks of four on the board, 
     * numerates the number of each colour found within said block and 
     * and accumulates the horizontal score via evauateScore
     * @param board
     *      The current board to score
     * @param colour
     *      The colour to score positively
     * @return int representing the score of the given board in favour of the given colour
     */
    private int scoreHorizontal(char[][] board, char colour){
        int score = 0;

        for(int row=0; row<board[0].length; row++){
            for (int col=0; col<board.length-3; col++){ 
                int colourCount = 0;
                int emptyCount = 0;
                for (int i=0; i<4; i++){
                    if (board[col+i][row] == colour){
                        colourCount++;
                    } else if (board[col+i][row] == ' '){
                        emptyCount++;
                    }
                }
                score += evaluateScore(colourCount, emptyCount);
            }
        }
        return score;
    }

    /**
     * Loops through all possible vertical blocks of four on the board, 
     * numerates the number of each colour found within said block and 
     * and accumulates the vertical score via evauateScore
     * @param board
     *      The current board to score
     * @param colour
     *      The colour to score positively
     * @return int representing the score of the given board in favour of the given colour
     */
    private int scoreVertical(char[][] board, char colour){
        int score = 0;

        for(int row=0; row<board[0].length-3; row++){
            for (int col=0; col<board.length; col++){
                int colourCount = 0;
                int emptyCount = 0;
                for (int i=0; i<4; i++){
                    if (board[col][row+i] == colour){
                        colourCount++;
                    } else if (board[col][row+i] == ' '){
                        emptyCount++;
                    }
                }
                score += evaluateScore(colourCount, emptyCount);
            }
        }
        return score;
    }

    /**
     * Loops through all possible diaganol blocks of four on the board, 
     * numerates the number of each colour found within said block and 
     * and accumulates the diaganol score via evauateScore
     * @param board
     *      The current board to score
     * @param colour
     *      The colour to score positively
     * @return int representing the score of the given board in favour of the given colour
     */
    private int scoreDiaganol(char[][] board, char colour){
        int score = 0;

        for(int row=0; row<board[0].length-3; row++){
            for (int col=0; col<board.length-3; col++){
                int colourCount = 0;
                int emptyCount = 0;
                for (int i=0; i<4; i++){
                    if (board[col+i][row+i] == colour){
                        colourCount++;
                    } else if (board[col+i][row+i] == ' '){
                        emptyCount++;
                    }
                }
                score += evaluateScore(colourCount, emptyCount);
            }
        }
        for(int row=0; row<board[0].length-3; row++){
            for (int col=0; col<board.length-3; col++){
                int colourCount = 0;
                int emptyCount = 0;
                for (int i=0; i<4; i++){
                    if (board[col+i][row+3-i] == colour){
                        colourCount++;
                    } else if (board[col+i][row+3-i] == ' '){
                        emptyCount++;
                    }
                }
                score += evaluateScore(colourCount, emptyCount);
            }
        }
        return score;
    }

    /**
     * Scores a given block of four spaces on the board, via the counts of each 
     * colour within that block passed to it.
     * @param colourCount
     *      The number of the positive scoring colour found in a given block of four
     * @param emptyCount
     *      The number of empty spaces found in a given block of four
     * @return int representing the score depending upon the numbers of each colour found
     */
    private int evaluateScore(int colourCount, int emptyCount){
        int score = 0;
        if (colourCount==3 && emptyCount==1){
            score += 5;
        }
        else if (colourCount==2 && emptyCount==2){
            score += 2;
        }
        else if (colourCount==0 && emptyCount==1){
            score -= 0;
        }
        return score;
    }
}
