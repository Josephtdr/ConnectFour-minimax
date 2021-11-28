public class Player {
    protected char colour;
    protected String name;

    public int getInput(Gamestate gamestate){
        return gamestate.getPlayableColumns().get(0);
    }

    public String getName(){
        return name;
    }

    public char getColour(){
        return colour;
    }
}
