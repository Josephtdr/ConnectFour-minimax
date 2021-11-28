import java.util.ArrayList;

public class Gamestate {
    /* 
    Wholly contianed gamestate class, contains all rules and peramaters pertaining 
    to the state of play. 
    */


    private int[] pointers = new int[7];
    private char[][] board = new char[7][6]; //[col][row]

    public Gamestate(){
        for(int col = 0; col < board.length; col++){
            pointers[col] = board[0].length - 1;
            for(int row = 0; row < board[0].length; row++){
                board[col][row] = ' ';
            } 
        }
    }

    public boolean placeCounter(char colour, int column){
        if(pointers[column] < 0){ //Guarding column full
            return false;
        }
        else{
            board[column][pointers[column]] = colour;
            pointers[column]--;
            //Successfull operation
            return true;
        }
    }

    public boolean isGameEnd(char colour){
        //Checks all possible ways in which the game could end
        return  isFull() ||
                isVerticleWin(colour) ||
                isHorizontalWin(colour) ||
                isDiaganolWin(colour);
    }

    public String getGameEndString(Player p){
        //Only called when game has ended and only two possible end states
        if(isFull()){
            return "Board full, thus draw!";
        }
        else {
            return String.format("%s Won!!", p.getName());
        }
    }

    private boolean isFull(){
        //check if game board has any slots left
        for (int col = 0; col < board.length; col++){
            if(pointers[col] >= 0){
                return false;
            }
        }
        return true;
    }

    private boolean isVerticleWin(char colour){
        for(int row=0; row<board[0].length-3; row++){
            for (int col=0; col<board.length; col++){
                if (board[col][row]==colour &&
                    board[col][row+1]==colour &&
                    board[col][row+2]==colour &&
                    board[col][row+3]==colour){
                    return true;
                    //TODO: Return index of bottommost winning chip
                }
            }
        }
        return false;
    }

    private boolean isHorizontalWin(char colour){
        for(int row=0; row<board[0].length; row++){
            for (int col=0; col<board.length-3; col++){
                if (board[col][row]==colour &&
                    board[col+1][row]==colour &&
                    board[col+2][row]==colour &&
                    board[col+3][row]==colour){
                    return true;
                    //TODO: Return index of leftmost winning chip
                }
            }
        }
        return false;
    }

    private boolean isDiaganolWin(char colour){
        for(int row = board[0].length-1; row >= 3; row--){ 
            for (int col = 0; col < board.length - 3; col++){
                if (board[col][row]==colour &&
                    board[col+1][row-1]==colour &&
                    board[col+2][row-2]==colour &&
                    board[col+3][row-3]==colour){
                    return true;
                    //TODO: Return index of  winning chip
                }
            }
        }
        for(int row = 0; row < board[0].length - 3; row++){ 
            for (int col = 0; col < board.length - 3; col++){
                if (board[col][row]==colour &&
                    board[col+1][row+1]==colour &&
                    board[col+2][row+2]==colour &&
                    board[col+3][row+3]==colour){
                    return true;
                    //TODO: Return index of  winning chip
                }
            }
        }
        return false;
    }

    public void printBoard(){
        for(int row=0; row<board[0].length; row++){
            for (int col = 0; col < board.length; col++){
                System.out.printf("| %c ", board[col][row]);
            }
            System.out.println("|");
        }
        System.out.println("  1   2   3   4   5   6   7");
    }

    public ArrayList<Integer> getPlayableColumns(){
        ArrayList<Integer> list = new ArrayList<>();
        for (int col = 0; col < board.length; col++){
            if(pointers[col] >= 0){
                list.add(col);
            }
        }
        return list;
    }

    public char[][] getBoard(){
        return board;
    }

    public int[] getPointers(){
        return pointers;
    }
}