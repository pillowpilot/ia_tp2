package models;

import java.lang.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashSet;

import models.State;

public class NaiveBacktracking{
    private int rank, size;
    private State state;
    private boolean findAll;
    private int solutionsTotal;
    private Vector<State> solutions;
    private ArrayList<HashSet<Integer>> valuesRow;
    private ArrayList<HashSet<Integer>> valuesColumn;
    private ArrayList<ArrayList<HashSet<Integer>>> valuesBlock;
    
    NaiveBacktracking(State initialState){
	rank = initialState.getRank();
	size = rank*rank;
	state = new State(initialState);
	findAll = false;
	solutionsTotal = 1;
    }
    public NaiveBacktracking findAll(boolean value){
	findAll = value;
	return this;
    }
    public NaiveBacktracking findOnly(int counter) throws IllegalArgumentException{
	if( counter <= 0 )
	    throw new IllegalArgumentException("Solutions counter must be positive.");
	solutionsTotal = counter;
	return this;
    }
    public Vector<State> solve(){
	initializeDataStructures();
	bt(0, 0);
	return solutions;
    }
    private void initializeDataStructures(){
	solutions = new Vector<State>();
	valuesRow = new ArrayList<HashSet<Integer>>();
	valuesColumn = new ArrayList<HashSet<Integer>>();
	valuesBlock = new ArrayList<ArrayList<HashSet<Integer>>>();
	for(int i = 0; i < rank; i++){
	    valuesBlock.add(new ArrayList<HashSet<Integer>>());
	    for(int j = 0; j < rank; j++){
		valuesBlock.get(i).add(new HashSet<Integer>());
	    }
	}
	for(int i = 0; i < size; i++){
	    valuesRow.add(new HashSet<Integer>());
	    valuesColumn.add(new HashSet<Integer>());
	}
	for(int row = 0; row < size; row++){
	    for(int column = 0; column < size; column++){
		final int value = state.get(row, column);
		if( value != 0 ){
		    addValue(row, column, value);
		}
	    }
	}
    }
    private void addValue(int row, int column, int value){
	valuesRow.get(row).add(value);
	valuesColumn.get(column).add(value);
	valuesBlock.get(row/rank).get(column/rank).add(value);
    }
    private void removeValue(int row, int column, int value){
	valuesRow.get(row).remove(value);
	valuesColumn.get(column).remove(value);
	valuesBlock.get(row/rank).get(column/rank).remove(value);
    }
    private void addSolution(){	
	solutions.add(new State(state));
    }
    private boolean keepLooking(){
	if( findAll ) return true;
	if( solutions.size() == solutionsTotal ) return false;
	return true;
    }
    private boolean bt(int row, int column){
	if( row == size ){
	    addSolution();
	    return keepLooking();
	}else{
	    if( state.get(row, column) == 0 ){
		boolean keepSearch = true;
		int value = 1;		
		while( keepSearch && value <= size ){
		    if( !conflicts(row, column, value) ){
			state.set(row, column, value);
			addValue(row, column, value);
			int nrow = row;
			int ncolumn = column+1;
			if( ncolumn == size ){
			    ncolumn = 0;
			    nrow++;
			}
			keepSearch = bt(nrow, ncolumn);
			if( keepSearch ){
			    state.set(row, column, 0);
			    removeValue(row, column, value);
			}
		    }
		    value++;
		}
		state.set(row, column, 0); // To set state as original
	    }else{
		int nrow = row;
		int ncolumn = column+1;
		if( ncolumn == size ){
		    ncolumn = 0;
		    nrow++;
		}
		bt(nrow, ncolumn);		
	    }
	    return true;
	}
    }
    private boolean conflicts(int row, int column, int value){
	if( valuesRow.get(row).contains(value) ) return true;
	if( valuesColumn.get(column).contains(value) ) return true;
	if( valuesBlock.get(row/rank).get(column/rank).contains(value) ) return true;
	return false;
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
	
	test(data1);
	test(data2);
    }
    private static void test(String data){
	State state = State.fromString(data);
	System.out.println(state);

	NaiveBacktracking nbt = new NaiveBacktracking(state);
	Vector<State> solutions = nbt.solve();
	System.out.println(solutions.size()+" solutions found!");
	for(State solution: solutions){
	    System.out.println(solution);
	}
    }
}
