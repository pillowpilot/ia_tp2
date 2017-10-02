package models;

import java.lang.*;
import java.util.*;

import models.SudokuSolver;

public class HeuristicSolver implements SudokuSolver{
    private long visited, startTime, endTime;
    private int size, rank;
    private boolean findAll;
    private State state;
    private Vector<State> solutions;

    @Override
    public State solve(State initialState, boolean findAll){
	solutions = new Vector<>();
	this.state = new State(initialState);
	rank = state.getRank();
	size = rank*rank;
	this.findAll = findAll;
	startTime = System.nanoTime();
	bt(state);
	endTime = System.nanoTime();
	if( solutions.size() == 0 )
	    return null;
	else
	    return solutions.get(0);
    }
    @Override
    public long getExecutionTime(){ return endTime - startTime; }
    @Override
    public long getVisitedNodes(){ return visited; }

    private boolean bt(State parent){
	visited++;
	
	State node = new State(parent);
	node = fillNakedSimples(node);
	if( isComplete(node) ){
	    solutions.add(new State(node));
	    if( findAll ) return false;
	    else return true;
	}else{
	    Map<Position, HashSet<Integer>> validValues = getValidValues(node);
	    Vector<Variable> variables = new Vector<>();
	    for(Map.Entry<Position, HashSet<Integer>> entry: validValues.entrySet()){
		Position position = entry.getKey();
		if( node.get(position.row, position.column) == State.EMPTY )
		    variables.add(new Variable(entry.getKey(), entry.getValue().size()));
	    }
	    Collections.sort(variables);

	    /*for(Variable variable: variables){
		Position position = variable.position;
		for(int value: validValues.get(position)){
		    node.set(position.row, position.column, value);
		    if( bt(node) ) return true;
		}
		node.set(position.row, position.column, 0);
		}*/
	    
	    Position position = variables.get(0).position;
	    for(int value: validValues.get(position)){
		node.set(position.row, position.column, value);
		if( bt(node) ) return true;
	    }
		
	    return false;
	}
    }
    private State fillNakedSimples(State initial){	
	State filled = new State(initial);

	Map<Position, HashSet<Integer>> validValues;
	boolean found;       
	do{
	    validValues = getValidValues(filled);
	    found = false;
	    for(Map.Entry<Position, HashSet<Integer>> entry: validValues.entrySet()){
		Position position = entry.getKey();
		if( entry.getValue().size() == 1 && filled.get(position.row, position.column) == State.EMPTY){
		    final int value = entry.getValue().iterator().next();
		    filled.set(position.row, position.column, value);
		    found = true;

		    visited++;
		}
	    }
	    
	}while(found);

	return filled;
    }
    private Map<Position, HashSet<Integer>>  getValidValues(State s){
	Map<Position, HashSet<Integer>> validValues = new TreeMap<>();

	ArrayList<Integer> values = new ArrayList<>();
	for(int value = 1; value <= s.getMaxValue(); value++)
	    values.add(value);

	for(int row = 0; row < size; row++){ // O(rank^6)
	    for(int column = 0; column < size; column++){
		validValues.put(new Position(row, column), new HashSet<>(values));
	    }
	}
	
	for(int row = 0; row < size; row++){
	    for(int column = 0; column < size; column++){
		final int value = s.get(row, column);
		if( value != State.EMPTY ){
		    for(int i = 0; i < size; i++){
			validValues.get(new Position(row, i)).remove(value);
			validValues.get(new Position(i, column)).remove(value);
		    }
		    final int prow = rank*(row/rank);
		    final int pcolumn = rank*(column/rank);
		    for(int i = 0; i < rank; i++){
			for(int j = 0; j < rank; j++){
			    validValues.get(new Position(prow+i, pcolumn+j))
				.remove(value);
			}
		    }
		    validValues.get(new Position(row, column)).clear();
		    validValues.get(new Position(row, column)).add(value);
		}		
	    }
	}

	return validValues;
    }
    private boolean isComplete(State state){
	for(int row = 0; row < size; row++)
	    for(int value: state.getRow(row))
		if( value == State.EMPTY )
		    return false;
	return true;
    }
    class Variable implements Comparable<Variable>{
	private final Position position;
	private final int cardinality;
	Variable(Position position, int cardinality){
	    this.position = position;
	    this.cardinality = cardinality;
	}
	@Override public int compareTo(Variable other){
	    if( cardinality == other.cardinality )
		return position.compareTo(other.position);
	    if( cardinality < other.cardinality ) return -1;
	    else return 1;
	}
    }
    public static void main(String[] args){
	final String data1 =
	    "_ _ _ _ 6 _ 9 _ _ " +
	    "_ _ 8 1 _ _ 3 6 _ " +
	    "9 6 7 3 _ _ _ _ _ " +
	    "_ _ _ 9 _ _ _ _ _ " +
	    "2 1 9 _ 8 7 4 _ _ " +
	    "_ _ 4 _ _ 2 _ _ 8 " +
	    "6 _ _ _ _ _ 8 _ _ " +
	    "4 _ _ 8 9 6 _ 7 _ " +
	    "8 _ _ _ _ 4 _ 5 3 ";
	final String data2 =
	    "_ _ 9 4 _ 2 3 5 _ " +
	    "_ 4 2 _ _ _ 8 _ _ " +
	    "_ _ 6 3 8 _ _ _ 4 " +
	    "_ _ _ _ _ _ 6 _ 7 " +
	    "7 _ _ 2 _ _ 9 _ _ " +
	    "_ _ 5 8 _ _ _ 3 _ " +
	    "2 3 _ _ _ 6 4 _ 1 " +
	    "_ _ _ _ _ _ 5 6 3 " +
	    "_ _ _ 7 _ 3 _ 9 _ ";
	final String data3 =
	    "_ _ 1 3 8 _ _ 4 _ " +
	    "5 4 6 _ _ 1 _ 2 _ " +
	    "_ _ _ _ _ _ _ _ _ " +
	    "6 _ _ _ _ _ 4 9 _ " +
	    "4 _ 5 _ 3 _ 8 _ 1 " +
	    "_ 3 9 _ _ _ _ _ 6 " +
	    "_ _ _ _ _ _ _ _ _ " +
	    "_ 7 _ 8 _ _ 6 3 4 " +
	    "_ 6 _ _ 4 3 7 _ _ ";
	final String data4 =
	    "_ _ _ _ _ 5 2 _ _ " +
	    "_ 5 _ _ _ _ _ _ _ " +
	    "_ _ _ 1 3 _ _ _ 7 " +
	    "_ _ 4 _ _ _ _ _ 6 " +
	    "_ _ _ 5 _ _ _ _ _ " +
	    "_ _ _ _ _ 3 _ _ _ " +
	    "_ _ _ 3 _ _ _ _ _ " +
	    "_ _ 2 _ 9 _ _ 3 _ " +
	    "_ _ _ _ 2 4 _ 8 _ ";
	    

	test(data1);
	test(data2);
	test(data3);
	test(data4);
    }
    private static void test(String data){
	State state = State.fromString(data);
	System.out.println(state);

	HeuristicSolver hs = new HeuristicSolver();
	System.out.println(hs.solve(state, false));

	System.out.println("Time: " + hs.getExecutionTime());
	System.out.println("Expanded Nodes: " + hs.getVisitedNodes());
    }
}
