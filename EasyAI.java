import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Initial idea for an ai past playing only randomly, inherits from the player class.
 */
public class EasyAI extends Player {

    private char opColour;

    public EasyAI(char colour, char opColour){
        name = "Easy AI";
        this.colour = colour;
        this.opColour = opColour;
    }

    /**
     * Finds a move for the AI to play, via the following strategy:
     * First checks if any of its possible plays can win, 
     * if not then checks if the human can win and blocks them,
     * if not again, then finally just plays randomly.
     * @param gamestate
     *      The current gamestate of the runtime program
     * @return int representing the column of the chosen move 
     */
    public int getInput(Gamestate gamestate){
        Random r = new Random();
        List<Integer> columnsSelection;

        List<Integer> aiWinable = getWinableColumns(gamestate, colour);
        List<Integer> humanWinable = getWinableColumns(gamestate, opColour);


        if(aiWinable.size() > 0){
            columnsSelection = aiWinable;
        } else if (humanWinable.size() > 0){
            columnsSelection = humanWinable;
        } else {
            columnsSelection = gamestate.getPossibleColumns();
        }

        int move = columnsSelection.get(r.nextInt(columnsSelection.size()));
        System.out.printf("Ai played %d.\n", (move+1));
        return move;
    }

    /**
     * Checks if playing the given colour in each possible column will win
     * @param gs
     *      The current gamestate of the runtime program
     * @param colour
     *      The char that is being checked if it can win
     * @return the columns in which playing the given colour will lead to a victory
     */
    private List<Integer> getWinableColumns(Gamestate gs, char colour){
        List<Integer> possibleColumns = gs.getPossibleColumns();
        List<Integer> winableColumns = new ArrayList<>();

        for (int col : possibleColumns){
            gs.placeCounter(col, colour);
            if (gs.isWin(colour)){
                winableColumns.add(col);
            }
            gs.unplaceCounter(col);
        }
        return winableColumns;
    }
}
