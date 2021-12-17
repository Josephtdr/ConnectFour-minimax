/**
 * Using an abstract superclass class such that common functionality 
 * between the ai's and human player can be inherited. 
 * Additionally they Instantiate their own form of the getInput function, allowing for 
 * the use of subtype polymorphism when creating a player list in ConnectFour.java
 */
abstract class Player {
    
    protected char colour;
    protected String name;

    /**
     * Instantiated by subclasses, used to get the input for a given player
     * @param gamestate
     *      The current gamestate of the runtime program
     * @return int representing the column chosen by the player 
     */
    public abstract int getInput(Gamestate gamestate);

    public String getName(){
        return name;
    }

    public char getColour(){
        return colour;
    }
}
