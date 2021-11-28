import java.util.Random;
import java.util.ArrayList;

public class AI extends Player {
    private char opColour;

    public AI(char colour, char opColour){
        name = "The AI has";
        this.colour = colour;
        this.opColour = opColour;
    }

    public int getInput(Gamestate gamestate){
        Random r = new Random();
        ArrayList<Integer> colSelectionFrom;
        ArrayList<Integer> posCols = gamestate.getPlayableColumns();
        ArrayList<Integer> aiWinable = getWinableColumns(colour,
            posCols, gamestate.getPointers(), gamestate.getBoard());
        ArrayList<Integer> humanWinable = getWinableColumns(opColour,
            posCols, gamestate.getPointers(), gamestate.getBoard());


        if(aiWinable.size() > 0){
            colSelectionFrom = aiWinable;
        } else if (humanWinable.size() > 0){
            colSelectionFrom = humanWinable;
        } else {
            colSelectionFrom = posCols;
        }

        int move = colSelectionFrom.get(r.nextInt(colSelectionFrom.size()));
        System.out.printf("Ai Chose %d.\n", (move+1));
        return move;
    }

    private ArrayList<Integer> getWinableColumns(
        char colour, ArrayList<Integer> posCols, 
        int[] pointers, char[][] board)
        {
        ArrayList<Integer> winCols = new ArrayList<>();
        for (int col : posCols){
            if (posHorizontalWins(col, colour, pointers[col], board) ||
                posVerticleWins(col, colour, pointers[col], board)   ||
                posDiaganolWins(col, colour, pointers[col], board)){
                    winCols.add(col);
                }
        }
        return winCols;
    }

    private boolean posHorizontalWins(int col, char colour,
        int row, char[][] board)
        {
        int[] offset = {-3, -2, -1, 1, 2, 3};
        int start = col < 3 ? 3 - col : 0;
        int end = col > 3 ? 7 - col : 4;

        for (int i = start; i < end; i++){
            if (board[col + offset[i]][row]==colour &&
                board[col + offset[i+1]][row]==colour &&
                board[col + offset[i+2]][row]==colour){
                    return true;
            }
        }
        return false;
    }

    private boolean posVerticleWins(int col, char colour,
        int row, char[][] board)
        {
        if (row <= 2 &&
            board[col][row + 1]==colour &&
            board[col][row + 2]==colour &&
            board[col][row + 3]==colour){
                return true;
        }
        return false;
    }

    private boolean posDiaganolWins(int col, char colour,
        int row, char[][] board)
        {
        int[] offset = {-3, -2, -1, 1, 2, 3};
        int start = (col > 2 && row < 3) ? 0 : 3 - Math.min(col, 5 - row);
        int end = (col < 3 && row > 2) ? 4 : 7 - Math.max(col, 6 - row);
        for (int i = start; i < end; i++){
            if (board[col + offset[i]][row - offset[i]]==colour &&
                board[col + offset[i+1]][row - offset[i+1]]==colour &&
                board[col + offset[i+2]][row - offset[i+2]]==colour){
                return true;
            }
        }

        start = (col > 2 && row > 2) ? 0 : 3 - Math.min(col, row) ;
        end = (col < 3 && row < 3) ? 4 : 7 - Math.max(col, 1 + row);
        for (int i = start; i < end; i++){
            if (board[col + offset[i]][row + offset[i]]==colour &&
                board[col + offset[i+1]][row + offset[i+1]]==colour &&
                board[col + offset[i+2]][row + offset[i+2]]==colour){
                return true;
            }
        }
        return false;
    }
}
