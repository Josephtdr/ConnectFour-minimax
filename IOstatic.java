import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class IOstatic{

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
                System.out.println("Please don't error me (IO e).");
            }
            catch(IllegalArgumentException e){
                //TODO: Print permitables
                System.out.println("Please input a valid String.");
            }
        }
        return userInput;
    }
}