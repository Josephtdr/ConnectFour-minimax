import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Wholly contianed gamestate class, contains all rules and peramaters pertaining to the state of play
 */
public class Gamestate {
    private int[] pointers = new int[7]; //To represent the next playable row for each respective column
    private char[][] board = new char[7][6]; //[col][row]
    private int numColumns = board.length;
    private int numRows = board[0].length;

    /**
     * Initialises the board to be all empty space and the pointers to be starting at 6
     * indicating the bottom of each column 
     */
    public Gamestate(){
        for(int col = 0; col < numColumns; col++){
            pointers[col] = numRows - 1;
            for(int row = 0; row < numRows; row++){
                board[col][row] = ' ';
            } 
        }
    }

    /**
     * Places a given counter (char) in a given column, 
     * and updates the pointer for that respective column.
     * @param column
     *      the column to place the counter in.
     * @param colour
     *      the char to be placed on the board.
     */
    public void placeCounter(int column, char colour){
        board[column][pointers[column]] = colour;
        pointers[column]--;
    }

    /**
     * the reverse action of {@link placeCounter}
     * @param column
     *      the column to reset the top piece to empty in.
     */
    public void unplaceCounter(int column){
        pointers[column]++;
        board[column][pointers[column]] = ' ';
    }

    /**
     * @return true if there are no remaining possible columns to play, false otherwise
     */
    public boolean isFull(){
        if (getPossibleColumns().size()==0){
            return true;
        }
        return false;
    }

    /**
     * Used to check if a given colour has won the game. 
     * @param colour
     *      The char used for finding four in a row.
     * @return true if four in a row exists on the board for the given char, false otherwise
     */
    public boolean isWin(char colour){
        return  isVerticleWin(colour) || isHorizontalWin(colour) || isDiaganolWin(colour);
    }

    /**
     * @param colour
     *      The char used for finding four in a row.
     * @return true if four in a row exists vertically on the board for the given char, false otherwise
     */
    private boolean isVerticleWin(char colour){
        return IntStream.range(0, numColumns)
            .anyMatch(col -> IntStream.range(0, numRows - 3)
                            .anyMatch(row -> 
                                board[col][row]==colour &&
                                board[col][row+1]==colour &&
                                board[col][row+2]==colour &&
                                board[col][row+3]==colour));
    }

    /**
     * @param colour
     *      The char used for finding four in a row.
     * @return true if four in a row exists horizontally on the board for the given char, false otherwise
     */
    private boolean isHorizontalWin(char colour){
        return IntStream.range(0, numColumns - 3)
            .anyMatch(col -> IntStream.range(0, numRows)
                            .anyMatch(row -> 
                                board[col][row]==colour &&
                                board[col+1][row]==colour &&
                                board[col+2][row]==colour &&
                                board[col+3][row]==colour));
    }

    /**
     * @param colour
     *      The char used for finding four in a row.
     * @return true if four in a row exists diagonally on the board for the given char, false otherwise
     */
    private boolean isDiaganolWin(char colour){
        return IntStream.range(0, numColumns - 3)
            .anyMatch(col -> IntStream.range(0, numRows - 3)
                            .anyMatch(row -> 
                                ((board[col][row+3]==colour &&
                                board[col+1][row+2]==colour &&
                                board[col+2][row+1]==colour &&
                                board[col+3][row]==colour)
                                ||
                                (board[col][row]==colour &&
                                board[col+1][row+1]==colour &&
                                board[col+2][row+2]==colour &&
                                board[col+3][row+3]==colour))));
    }

    /**
     * Prints the current board to the console for the player to see
     */
    public void printBoard(){
        for(int row=0; row < numRows; row++){
            for (int col = 0; col < numColumns; col++){
                System.out.printf("| %c ", board[col][row]);
            }
            System.out.println("|");
        }
        System.out.println("  1   2   3   4   5   6   7");
    }

    /**
     * @return List of ints representing the non full columns in the current gamestate 
     */
    public List<Integer> getPossibleColumns(){
        return IntStream.range(0, numColumns)
            .filter(col -> pointers[col] >= 0)
            .boxed().toList();
    }

    /**
     * Move objects also contain a score peramater so can be used for sorting in the hard ai
     * @return List of Moves representing the not full columns in the current gamestate 
     */
    public List<Move> getPossibleMoves(){
        return IntStream.range(0, numColumns)
            .filter(col -> pointers[col] >= 0).boxed()
            .map(col -> new Move(col))
            .collect(Collectors.toList());
    }

    public char[][] getBoard() {
        return board;
    }
}
