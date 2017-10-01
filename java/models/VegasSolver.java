/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 *
 * @author Tadashi
 */
public class VegasSolver implements SudokuSolver{
    
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
                    int kk[] = new int[n];
                    for(int k = 0; k < n; k++){
                        kk[k] = k + 1;
                    }
                    for(int k = 0; k < n; k++){
                        int r = (int)(Math.random() * n);
                        int aux = kk[k];
                        kk[k] = kk[r];
                        kk[r] = aux;
                    }
                    
                    for(int k = 0; k < n; k++){
                        state.set(i, j, kk[k]);
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
