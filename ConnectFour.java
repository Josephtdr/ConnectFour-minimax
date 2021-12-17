import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The 'main' class, used to instantiate the player list and gamestate
 * then iterate through until completion.
 * Can be run with two optional args, to designate if the human goes first,
 * and if the ai is easy or hard
 */
public class ConnectFour {
    private Player[] players = new Player[2]; //polymorphism
    private Gamestate gamestate;

    /**
     * Initiates a game by displaying the intro and setting up the players and gamestate.
     * @param colours
     *      The chars to represent the two players on the board within the game
     * @param humanHasFirstTurn
     *      indicates who is to play first, 't' if human, 'f' if AI
     * @param difficulty
     *      indicates the difficult of AI to be used, 'h' for hard, 'e' for easy
     */
    public ConnectFour(char[] colours, char humanHasFirstTurn, char difficulty){
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
        if (humanHasFirstTurn=='f'){
            playerTurn(players[1]);
        }
	}

    /**
     * Continually performs turns for each player, one after another,
     * printing the board etc,
     * until the game reaches a terminal state.
     */
    public void playGame(){
        boolean gameOver = false;
        gamestate.printBoard();
        do{
            for (Player p : players){
                gameOver = playerTurn(p);

                if(gameOver) {
                    if (gamestate.isWin(p.getColour())){
                        System.out.println(String.format("%s Won!!", p.getName()));
                    }
                    else{
                        System.out.println("Board full, thus draw!");
                    }
                    break;
                }
            }
            gamestate.printBoard();
        } while(!gameOver);
    }

    /**
     * Gets and performs the move of the current player (gets move via polymorphism 
     * as players can be of  varying subclass types under the player superclass).
     * @param player
     *      The player whos turn it is to perform
     * @return true if the game has ended from the players action, false otherwise
     */
    private boolean playerTurn(Player player){
        int move = player.getInput(gamestate);
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

    public static void main(String[] args) {
        char humanHasFirstTurn;
        char difficulty;
        if (args.length>=1 && (args[0].charAt(0) == 't' || args[0].charAt(0) == 'f')){
            humanHasFirstTurn = args[0].charAt(0);
        } else {
            Random r = new Random();
            humanHasFirstTurn = r.nextInt(2) == 0 ? 't' : 'f';
            
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
            game = new ConnectFour(colours, humanHasFirstTurn, difficulty);
            game.playGame();
            
            response = IOstatic.getStringInput("Play Again? [y/n]",
                new ArrayList<String>(Arrays.asList("y", "n")));
        } while (!response.equals("n"));
    }
}
