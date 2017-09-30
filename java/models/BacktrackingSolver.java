package models;

public class BacktrackingSolver implements SudokuSolver{
    
    private long visitedNodes = 0;
    private long startTime = 0;
    private long endTime = 0;
    private State state;
    
    @Override
    public State solve(State state, boolean findAllSolutions){
        this.state = state.clone();
        
        visitedNodes = 0;
        startTime = System.nanoTime();
        backtracking(this.state, findAllSolutions);
        endTime = System.nanoTime();
        
        return this.state;
    }
    
    @Override
    public long getExecutionTime(){
        return endTime - startTime;
    }
    
    @Override
    public long getVisitedNodes(){
        return visitedNodes;
    }
    
    private long backtracking(State state, boolean findAllSolutions){
        visitedNodes++;
        if(!state.isValid()){
            return 0;
        }else if(isComplete(state)){
            return 1;
        }
        
        long totalSolutionNumber = 0;
        int n = state.getMaxValue();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(state.get(i, j) == State.EMPTY){
                    for(int k = 1; k <= n; k++){
                        state.set(i, j, k);
                        long solutionNumber = backtracking(state, findAllSolutions);
                        totalSolutionNumber += solutionNumber;
                        if(solutionNumber > 0 && !findAllSolutions){
                            return 1;
                        }
                        state.set(i, j, State.EMPTY);
                    }
                    return totalSolutionNumber;
                }
            }
        }

        return totalSolutionNumber;
    }
    
    private boolean isComplete(State state){
        int n = state.getMaxValue();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(state.get(i, j) == State.EMPTY){
                    return false;
                }
            }
        }
        return true;
    }
    
    /*private boolean areRepeatingNumbers(int numbers[]){
        int n = numbers.length;
        int digits[] = new int[n];
        for(int i = 0; i < n; i++){
            if(numbers[i] > 0){
                digits[numbers[i] - 1]++;
                if(digits[numbers[i] - 1] > 1){
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isValid(int board[][]){
        int n = board.length;
        int sn = (int) Math.sqrt(n);

        int numbers[] = new int[n];
        
        for(int i = 0; i < n ; i++){ 
            for(int j = 0; j < n; j++){
                numbers[j] = board[i][j];
            }
            if(areRepeatingNumbers(numbers)){
                return false;
            }
        }

        for(int j = 0; j < n; j++){
            for(int i = 0; i < n ; i++){
                numbers[i] = board[i][j];
            }
            if(areRepeatingNumbers(numbers)){
                return false;
            }
        }

        
        for(int i = 0; i < n ; i++){
            for(int j = 0; j < n; j++){
                numbers[j] = board[(i * sn) % n + (j) % sn][(i / sn) * sn + (j / sn)];
            }
            if(areRepeatingNumbers(numbers)){
                return false;
            }
        }
        
        return true;
    }*/
}
