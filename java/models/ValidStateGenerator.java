package models;

import java.lang.*;
import java.util.concurrent.ThreadLocalRandom;

public class ValidStateGenerator{
    private int rank, size, maxValue;
    public State state;
    private int[][][] validValues;
    private boolean[][] usedRow;
    private boolean[][] usedColumn;
    private boolean[][][] usedBlock;

    ValidStateGenerator(int rank){
	this.rank = rank;
	maxValue = size = rank*rank;
	state = new State(rank);

	initializeDataStructures();
    }
    public State get(){
	if( bt(0, 0) )
	    return new State(state);
	else
	    return null;
    }
    public static State get(int rank){
	return new ValidStateGenerator(rank).get();
    }
    private void initializeDataStructures(){
	validValues = new int[size][size][size];
	for(int i = 0; i < size; i++){
	    for(int j = 0; j < size; j++){
		validValues[i][j] = getRandomPermutation();
	    }
	}

	usedRow = new boolean[size][size+1];
	usedColumn = new boolean[size][size+1];
	for(int i = 0; i < size; i++){
	    for(int j = 0; j < size+1; j++){
		usedRow[i][j] = false;
		usedColumn[i][j] = false;
	    }
	}

	usedBlock = new boolean[rank][rank][size+1];
	for(int i = 0; i < rank; i++){
	    for(int j = 0; j < rank; j++){
		for(int k = 0; k < size+1; k++){
		    usedBlock[i][j][k] = false;
		}
	    }
	}
    }
    private boolean bt(int row, int column){
	if( row == size ){
	    return true; // solved
	}else{
	    int[] values = validValues[row][column];
	    boolean solved = false;
	    int index = 0;
	    while( index < values.length && !solved ){
		int value = values[index];
		if( !conflicts(row, column, value) ){
		    state.set(row, column, value);
		    usedRow[row][value] = true;
		    usedColumn[column][value] = true;
		    usedBlock[row/rank][column/rank][value] = true;

		    int nrow = row;
		    int ncolumn = column + 1;
		    if( ncolumn == size ){
			ncolumn = 0;
			nrow++;
		    }
		    solved = bt(nrow, ncolumn);
		    if( solved ) return true;
		    else{
			state.set(row, column, 0);
			usedRow[row][value] = false;
			usedColumn[column][value] = false;
			usedBlock[row/rank][column/rank][value] = false;
		    }
		}
		index++;
	    }
	    return false;
	}
    }
    private boolean conflicts(int row, int column, int value){
	if( usedRow[row][value] == true ) return true;
	if( usedColumn[column][value] == true ) return true;
	if( usedBlock[row/rank][column/rank][value] == true ) return true;
	return false;
    }
    private int[] getRandomPermutation(){
	int[] permutation = new int[size];
	for(int value = 1; value <= size; value++)
	    permutation[value-1] = value;
	shuffle(permutation);
	return permutation;
    }
    private void shuffle(int[] v){
	for(int i = 0; i < v.length; i++){
	    int j = ThreadLocalRandom.current().nextInt(i, v.length);

	    int t = v[i];
	    v[i] = v[j];
	    v[j] = t;
	}
    }
    
    public static void main(String[] args){
	test(2);
	test(3);
	ValidStateGenerator.get(4).print();
	// for rank=5 may take a while...
    }
    private static void test(int rank){
	ValidStateGenerator gen = new ValidStateGenerator(rank);	
	gen.get().print();
    }
}
