abstract class Player {
    protected char colour;
    protected String name;

    public abstract int getInput(Gamestate gamestate);

    public String getName(){
        return name;
    }

    public char getColour(){
        return colour;
    }
}
