import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.lang.IndexOutOfBoundsException;

/**
 * The human subclass, inherited from the player superclass, used to get player input.
 */
public class Human extends Player{
    private BufferedReader input;

    public Human(char colour){
        this.colour = colour;
        name = "You";
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Gets the user input for the column they with to play, via a
     * while loop and four exceptions for when the input is incorrect in varying ways. 
     * @param gamestate
     *      The current gamestate of the runtime program
     * @return int representing the column the user wishes to play in
     */
    public int getInput(Gamestate gamestate){
        ArrayList<Integer> possibleColumns = gamestate.getPossibleColumns();
		String userInput = null;
        int move = 0;
		
        while(move < 1 || 7 < move){
            try{
                System.out.print("Please Enter Column: ");
                userInput = input.readLine();
                move = Integer.parseInt(userInput);
                
                if (move < 1 || move > 7){
                    throw new IndexOutOfBoundsException("Index out of range");
                }
                else if (!possibleColumns.contains(Integer.valueOf(move-1))){
                    throw new IllegalArgumentException("Invalid!");
                }
            }
            catch(IOException e){
                System.out.println("IOExeption" + e);
            }
            catch(NumberFormatException e){
                System.out.println("Please input an integer.");
            }
            catch(IndexOutOfBoundsException e){
                System.out.println("Please input an integer between 1 and 7.");
            }
            catch(IllegalArgumentException e){
                System.out.printf("Column %d is full, please input a different column.\n", move);
                move = 0;
            }
        }
		return move - 1;
	}
}
