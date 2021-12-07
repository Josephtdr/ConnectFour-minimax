import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.lang.IndexOutOfBoundsException;

public class Human extends Player{
    private BufferedReader input;

    public Human(char colour){
        this.colour = colour;
        name = "You";
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    public int getInput(Gamestate gamestate){
        ArrayList<Integer> posCols = gamestate.getPlayableColumns();
		String userInput = null;
        int move = 0;
		
        while(move < 1 || move > 7){
            try{
                userInput = input.readLine();
                move = Integer.parseInt(userInput);
                
                if (move < 1 || move > 7){
                    throw new IndexOutOfBoundsException("Index out of range");
                }
                else if (!posCols.contains(Integer.valueOf(move-1))){
                    throw new IllegalArgumentException("Invalid!");
                }
            }
            catch(IOException e){
                System.out.println("Please don't error me (IO e).");
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
		return move-1;
	}
}
