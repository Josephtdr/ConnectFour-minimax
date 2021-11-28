import java.util.ArrayList;
import java.util.Arrays;

public class ConnectFour {
    public static void main(String[] args) {
        char[] colours = new char[2];
        if (args.length>=2 && args[0].length() == 1 && args[1].length() == 1){
            colours[0] = args[0].charAt(0);
            colours[1] = args[1].charAt(0);
        } else {
            colours[0] = 'r';
            colours[1] = 'y';
        }

        ConnectFour game;
        String response;
        do{
            game = new ConnectFour(colours);
            game.playGame();
            response = IOstatic.getStringInput("Play Again? [y/n]",
                new ArrayList<String>(Arrays.asList("y", "n")));
        } while (!response.equals("n"));
    }
    
    private Player[] players = new Player[2];
    private Gamestate gamestate;

    public ConnectFour(char[] colours){
        gamestate = new Gamestate();
        players[0] = new Human(colours[0]);
        players[1] = new AI(colours[1], colours[0]);      
	}

    public void playGame(){
        boolean gameOver = false;
        printIntro();
        gamestate.printBoard();
        do{
            for (Player p : players){
                //Evaluates to true if game ends.
                if(playerTurn(p)){
                    System.out.println(gamestate.getGameEndString(p));
                    gameOver = true;
                    break;
                }
            }
            gamestate.printBoard();
        } while(!gameOver);
    }

    private boolean playerTurn(Player player){
        int move = player.getInput(gamestate);
        gamestate.placeCounter(player.getColour(),move);
        return gamestate.isGameEnd(player.getColour());
    }

    private void printIntro(){
        System.out.println("Welcome to Connect 4");
		System.out.println("There are 2 players red and yellow");
		System.out.println("Player 1 is Red, Player 2 is Yellow");
		System.out.println("To play the game type in the number of the column you want to drop you counter in");
		System.out.println("A player wins by connecting 4 counters in a row - vertically, horizontally or diagonally");
		System.out.println("");
    }
}
