import java.util.ArrayList;

public class HardAI extends Player{
    private char opColour;

    public HardAI(char colour, char opColour){
        name = "Hard AI";
        this.colour = colour;
        this.opColour = opColour;
    }

    public int getInput(Gamestate gs){
        int bestScore = Integer.MIN_VALUE; 
        int score = 0;
        int move = 8;
        int depth = 8;
        for (int col : orderColumns(gs.getPlayableColumns())){
            
            gs.placeCounter(col, colour);
            score = minimax(gs, depth, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            gs.unplaceCounter(col);

            if (score > bestScore){
                bestScore = score;
                move = col;
            }
            
        }
        System.out.printf("Ai played %d.\n", (move+1));
        return move;
    }

    private int minimax(Gamestate gs, int maxDepth, int depth, int alpha, int beta, boolean isMaxim){
        if (isTerminal(gs) || depth == 0){
            return getRewards(gs, colour, opColour, maxDepth, depth);
        }

        if (isMaxim){
            Integer maxEval = Integer.MIN_VALUE;
            for (int col : orderColumns(gs.getPlayableColumns())){

                gs.placeCounter(col, colour);
                int eval = minimax(gs, maxDepth, depth-1, alpha, beta, false);
                gs.unplaceCounter(col);
                maxEval = Math.max(maxEval, eval);

                alpha = Math.max(alpha, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return maxEval;
        }
        else {
            Integer maxEval = Integer.MAX_VALUE;
            for (int col : orderColumns(gs.getPlayableColumns())){
                
                gs.placeCounter(col, opColour);
                int eval = minimax(gs, maxDepth, depth-1, alpha, beta, true);
                gs.unplaceCounter(col);
                maxEval = Math.min(maxEval, eval);

                beta = Math.min(beta, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return maxEval;
        }
    }

    private ArrayList<Integer> orderColumns(ArrayList<Integer> possibleCols){
        ArrayList<Integer> list = new ArrayList<>();
        if (possibleCols.contains(3)){
            list.add(3);
        }
        if (possibleCols.contains(2)){
            list.add(2);
        }
        if (possibleCols.contains(4)){
            list.add(4);
        }
        if (possibleCols.contains(1)){
            list.add(1);
        }
        if (possibleCols.contains(5)){
            list.add(5);
        }
        if (possibleCols.contains(0)){
            list.add(0);
        }
        if (possibleCols.contains(6)){
            list.add(6);
        }
        return list;
    }

    private boolean isTerminal(Gamestate gs){
        return gs.isWin(colour) || gs.isWin(opColour) || gs.isFull();
    }

    private int getRewards(Gamestate gs, char colour, char opColour, int maxdepth, int depth){
        int score = 0;
        char[][] board = gs.getBoard();
        if (gs.isWin(colour)){
            score = Integer.MAX_VALUE - (maxdepth - depth);
        }
        else if (gs.isWin(opColour)){
            score = Integer.MIN_VALUE + (maxdepth - depth);
        }
        else if (gs.isFull()){
            score = 0;
        }
        else{
            score += scoreMiddleColumn(board, colour);
            score += countHorizontal(board, colour);
            score += countVertical(board, colour);
            score += countDiaganol(board, colour);
        }

        return score - (maxdepth - depth);
        
    }

    private int evaluateScore(int colourCount, int emptyCount, int oppCount){
        int score = 0;
        if (colourCount==3 && emptyCount==1){
            score += 5;
        }
        else if (colourCount==2 && emptyCount==2){
            score += 2;
        }

        return score;
    }

    private int scoreMiddleColumn(char[][] board, char colour){
        int score = 0;
        for(int row=0; row<board[0].length; row++){
            if (board[3][row] == colour){
                score += 3;
            }
        }
        return score;
    }

    private int countHorizontal(char[][] board, char colour){
        int score = 0;

        for(int row=0; row<board[0].length; row++){
            for (int col=0; col<board.length-3; col++){ 
                int colourCount = 0;
                int emptyCount = 0;
                int oppCount = 0;
                for (int i=0; i<4; i++){
                    if (board[col+i][row] == colour){
                        colourCount++;
                    } else if (board[col+i][row] == ' '){
                        emptyCount++;
                    } else {
                        oppCount++;
                    }
                }
                score += evaluateScore(colourCount, emptyCount, oppCount);
            }
        }
        return score;
    }

    private int countVertical(char[][] board, char colour){
        int score = 0;

        for(int row=0; row<board[0].length-3; row++){
            for (int col=0; col<board.length; col++){
                int colourCount = 0;
                int emptyCount = 0;
                int oppCount = 0;
                for (int i=0; i<4; i++){
                    if (board[col][row+i] == colour){
                        colourCount++;
                    } else if (board[col][row+i] == ' '){
                        emptyCount++;
                    } else {
                        oppCount++;
                    }
                }
                score += evaluateScore(colourCount, emptyCount, oppCount);
            }
        }
        return score;
    }

    private int countDiaganol(char[][] board, char colour){
        int score = 0;

        for(int row=0; row<board[0].length-3; row++){
            for (int col=0; col<board.length-3; col++){
                int colourCount = 0;
                int emptyCount = 0;
                int oppCount = 0;
                for (int i=0; i<4; i++){
                    if (board[col+i][row+i] == colour){
                        colourCount++;
                    } else if (board[col+i][row+i] == ' '){
                        emptyCount++;
                    } else {
                        oppCount++;
                    }
                }
                score += evaluateScore(colourCount, emptyCount, oppCount);
            }
        }
        for(int row=0; row<board[0].length-3; row++){
            for (int col=0; col<board.length-3; col++){
                int colourCount = 0;
                int emptyCount = 0;
                int oppCount = 0;
                for (int i=0; i<4; i++){
                    if (board[col+i][row+3-i] == colour){
                        colourCount++;
                    } else if (board[col+i][row+3-i] == ' '){
                        emptyCount++;
                    } else {
                        oppCount++;
                    }
                }
                score += evaluateScore(colourCount, emptyCount, oppCount);
            }
        }
        return score;
    }
}
