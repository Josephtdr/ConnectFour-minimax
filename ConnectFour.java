import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ConnectFour {
    public static void main(String[] args) {
        int humanPlayerTurn;
        char difficulty;
        if (args.length>=1 && (Integer.valueOf(args[0]) == 1 || Integer.valueOf(args[0]) == 0)){
            humanPlayerTurn = Integer.valueOf(args[0]);
        } else {
            Random r = new Random();
            humanPlayerTurn = r.nextInt(2);
        }
        if (args.length>=2 && (args[1].charAt(0) == 'e' || args[1].charAt(0) == 'h')){
            difficulty = args[1].charAt(0);
        } else {
            difficulty = 'h';
        }

        char[] colours = new char[] {'◌', '●'};
        ConnectFour game;
        String response;
        do{
            game = new ConnectFour(colours, humanPlayerTurn, difficulty);
            game.playGame();
            response = IOstatic.getStringInput("Play Again? [y/n]",
                new ArrayList<String>(Arrays.asList("y", "n")));
        } while (!response.equals("n"));
    }
    
    private Player[] players = new Player[2]; //polymorphism
    private Gamestate gamestate;

    public ConnectFour(char[] colours, int humanPlayer, char difficulty){
        gamestate = new Gamestate();
        players[0] = new Human(colours[0]); //polymorphism

        if (difficulty=='h'){
            players[1] = new HardAI(colours[1], colours[0]); //polymorphism
        }
        else {
            players[1] = new EasyAI(colours[1], colours[0]); //polymorphism
        }

        printIntro();

        //Performs an AI turn to offset the playing of the first turn if the ai is to be first
        if (humanPlayer==1){
            playerTurn(players[1]);
        }
	}

    public void playGame(){
        boolean gameOver = false;
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
        int move = player.getInput(gamestate);  //polymorphism
        gamestate.placeCounter(move, player.getColour());
        return gamestate.isWin(player.getColour()) || gamestate.isFull();
    }

    private void printIntro(){
        System.out.println("Welcome to Connect 4");
		System.out.println("There are 2 players Black and White");
		System.out.println("The first player is assigned randomly");
		System.out.println("To play the game type in the number of the column you want to drop you counter in");
		System.out.println("A player wins by connecting 4 counters in a row - vertically, horizontally or diagonally\n");
    }
}
