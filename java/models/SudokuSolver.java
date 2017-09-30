package models;

public interface SudokuSolver {
    
    public State solve(State state, boolean findAllSolutions);
    public long getExecutionTime();
    public long getVisitedNodes();
    
}
