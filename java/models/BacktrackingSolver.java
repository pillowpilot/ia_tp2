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
}
