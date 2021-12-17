import java.lang.Comparable;

/**
 * Move class, allows for ordering of moves based on scores give to them
 * as it implements the comparable class and Overrides compareTo
 */
public class Move implements Comparable<Move>{
    private int column; //target column
    private int score;

    public Move(int column){
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
    public void setColumn(int column) {
        this.column = column;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    
    /**
     * Orders in Descending order
     * @param compareMove
     *      The comparison move
     * @return int of comparison moves score minus this moves score
     */
    @Override
    public int compareTo(Move compareMove){
        int compareScore = ((Move)compareMove).getScore();
        return compareScore - this.getScore();
    }

    @Override
    public String toString() {
        return "col: " + column + ", score: " + score + ".";
    }
}