import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class IOstatic{

    /**
     * Gets a user input with a constrained set of permitable values.
     * @param prompt
     *      String to display to the user to prompt them for an input
     * @param permitables
     *      The legal strings that their response can be 
     * @return String input by the user, must be contained in permitables
     */
    public static String getStringInput(String prompt, ArrayList<String> permitables){
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(prompt);
        String userInput = null;
        while(!permitables.contains(userInput)){
            try{
                userInput = input.readLine();
                if (!permitables.contains(userInput)){
                    throw new IllegalArgumentException("Invalid!");
                }
            }
            catch(IOException e){
                System.out.println("IOExeption" + e);
            }
            catch(IllegalArgumentException e){
                System.out.println("Please input a valid String " + permitables + ".");
            }
        }
        return userInput;
    }
}