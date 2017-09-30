
package models;

import java.lang.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;
import java.util.Vector;
import java.util.Iterator;

public class State{
    private SquareMatrix state;
    private int rank;
    private int size;

    State(int rank) throws IllegalArgumentException{
	if( rank<1 ) throw new IllegalArgumentException("Rank must be positive.");

	this.rank = rank;
	size = rank*rank;
	state = new SquareMatrix(size);
    }
    State(State other){
	this.rank = other.rank;
	this.size = other.size;
	this.state = new SquareMatrix(other.state);
    }
    public int getRank(){ return rank; }
    public int getMaxValue(){ return rank*rank+1; }

    public int get(int row, int column) throws IndexOutOfBoundsException{
	if( !(0<=row&&row<size&&0<=column&&column<size) )
	    throw new IndexOutOfBoundsException("Indexes must belong to [0, "+size+")x[0, "+size+").");
	return state.get(row, column);
    }
    public void set(int row, int column, int value) throws IndexOutOfBoundsException{
	if( !(0<=row&&row<size&&0<=column&&column<size) )
	    throw new IndexOutOfBoundsException("Indexes must belong to [0, "+size+")x[0, "+size+").");
	state.set(row, column, value);
    }

    public Row getRow(int row) throws IndexOutOfBoundsException{
	if( !(0<=row&&row<size) )
	    throw new IndexOutOfBoundsException("Row index must belong to [0, "+size+").");
	return new Row(row);
    }
    public Column getColumn(int column) throws IndexOutOfBoundsException{
	if( !(0<=column&&column<size) )
	    throw new IndexOutOfBoundsException("Column index must belong to [0, "+size+").");
	return new Column(column);
    }
    public Block getBlock(int row, int column) throws IndexOutOfBoundsException{
	if( !(0<=row&&row<rank&&0<=column&&column<rank) )
	    throw new IndexOutOfBoundsException("Block indexes must belong to [0, "+rank+")x[0, "+rank+").");
	return new Block(row, column);
    }
    public Vector<Position> getEmptyValues(){
	Vector<Position> emptyValues = new Vector<Position>();
	for(int row = 0; row < size; row++){
	    for(int column = 0; column < size; column++){
		if( state.get(row, column) == 0 )
		    emptyValues.add(new Position(row, column));
	    }
	}
	return emptyValues;
    }
    public boolean isValid(){
	for(int i = 0; i < rank; i++){
	    for(int j = 0; j < rank; j++){
		if( !checkBlock(i, j) ) return false;
	    }
	}
	for(int i = 0; i < size; i++)
	    if( !checkRow(i) ) return false;
	    else if( !checkColumn(i) ) return false;
	return true;
    }
    public String toString(){
	StringBuilder output = new StringBuilder();

	output.append(String.format("Rank = %d%n", rank));
	final int width = (int)Math.log10(size*size)+2;
	final String format = "%"+width+"d";
	for(int i = 0; i < size; i++){
	    if( i % rank == 0 )
		output.append(horizontalLine(width));
	    for(int j = 0; j < size; j++){
		if( j % rank == 0 )
		    output.append("|");
		output.append(String.format(format, get(i, j)));
	    }
	    output.append(String.format("|%n"));
	}
	output.append(horizontalLine(width));
	return output.toString();
    }
    public void print(){
	System.err.println("Rank = " + rank);

	final int width = (int)Math.log10(size*size)+2;
	final String format = "%"+width+"d";
	for(int i = 0; i < size; i++){
	    if( i % rank == 0 ) printHorizontalLine(width);
	    for(int j = 0; j < size; j++){
		if( j % rank == 0 )
		    System.err.print("|");
		System.err.format(format, get(i, j));
	    }
	    System.err.println("|");
	}
	printHorizontalLine(width);
    }
    private String horizontalLine(int width){
	StringBuilder output = new StringBuilder();
	final int times = width*size+size/rank;
	for(int i = 0; i < times; i++){
	    output.append("-");
	}
	output.append(String.format("%n"));
	return output.toString();
    }
    private void printHorizontalLine(int width){
	final int times = width*size+size/rank;
	for(int i = 0; i < times; i++){
	    System.err.print("-");
	}
	System.err.println();
    }
    public void randomLoad(int emptyValues){
	for(int i = 0; i < size; i++){
	    for(int j = 0; j < size; j++){
		int value = ThreadLocalRandom.current().nextInt(1, size+1); // [1, size+1)
		state.set(i, j, value);
	    }
	}
	for(int i = 0; i < emptyValues; i++){
	    int row, column;
	    do{
		row = ThreadLocalRandom.current().nextInt(0, size);
		column = ThreadLocalRandom.current().nextInt(0, size);
	    }while(get(row, column) != 0);
	    set(row, column, 0);
	}
    }
    public boolean checkVirtualLine(Iterable<Integer> virtualLine){
	boolean[] used = new boolean[getMaxValue()];
	for(Integer value: virtualLine)
	    if( used[value] ) return false;
	    else used[value] = true;
	return true;
    }
    public boolean checkRow(int row){
	return checkVirtualLine(getRow(row));
    }
    public boolean checkColumn(int column){
	return checkVirtualLine(getColumn(column));
    }
    public boolean checkBlock(int row, int column){
	return checkVirtualLine(getBlock(row, column));
    }

    // Inner classes
    class Row implements Iterable<Integer>{
	private int row;

	Row(int row){
	    this.row = row;
	}
	public Iterator<Integer> iterator(){ return new RowIterator(); }

	class RowIterator implements Iterator<Integer>{
	    private int index;
	    public boolean hasNext(){ return index < state.size(); }
	    public Integer next(){ return state.get(row, index++); }
	    public void remove(){ throw new UnsupportedOperationException("Items cannot be removed."); }
	}
    }

    class Column implements Iterable<Integer>{
	private int column;

	Column(int column){
	    this.column = column;
	}
	public Iterator<Integer> iterator(){ return new ColumnIterator(); }

	class ColumnIterator implements Iterator<Integer>{
	    private int index;
	    public boolean hasNext(){ return index < state.size(); }
	    public Integer next(){ return state.get(index++, column); }
	    public void remove(){ throw new UnsupportedOperationException("Items cannot be removed."); }
	}
    }

    class Block implements Iterable<Integer>{
	private int row, column;
	Block(int row, int column){
	    this.row = row*rank;
	    this.column = column*rank;
	}
	public Iterator<Integer> iterator(){ return new BlockIterator(); }

	class BlockIterator implements Iterator<Integer>{
	    private int irow, icolumn;
	    public boolean hasNext(){ return irow < rank; }
	    public Integer next(){ Integer v = state.get(row+irow, column+icolumn); advance(); return v; }
	    public void remove(){ throw new UnsupportedOperationException("Items cannot be removed."); }
	    private void advance(){
		icolumn++;
		if( icolumn == rank ){
		    irow++;
		    icolumn = 0;
		}
	    }
	}
    }

    public static void main(String[] args){
	test(1);
	test(3);
	test(4);
    }
    private static void test(int rank){
	State s = new State(rank);
	System.out.println(s);
	s.randomLoad(5);
	System.out.println(s);
	for(Integer v: s.getRow(0)){
	    System.out.print(v + " ");
	}
	System.out.println();
	for(int v: s.getColumn(rank-1)){
	    System.out.print(v + " ");
	}
	System.out.println();

	for(int v: s.getBlock(rank-1, rank-1)){
	    System.out.print(v + " ");
	}
	System.out.println();
    }
}

class SquareMatrix{
    private Vector<Vector<Integer>> data;
    private final int n;

    SquareMatrix(int n) throws IllegalArgumentException{
	if( !(0<n) ) throw new IllegalArgumentException("Matrix dimentions must be positive.");

	data = new Vector<Vector<Integer>>(n);
	for(int i = 0; i < n; i++){
	    Vector<Integer> v = new Vector<Integer>(n);
	    for(int j = 0; j < n; j++) v.add(0);
	    data.add(v);
	}

	this.n = n;
    }
    SquareMatrix(SquareMatrix other){
	this.n = other.n;
	this.data = new Vector<Vector<Integer>>();
	for(Vector<Integer> other_row: other.data){
	    Vector<Integer> row = new Vector<Integer>(other_row);
	    this.data.add(row);
	}	
    }

    public Integer get(int row, int column) throws IndexOutOfBoundsException{
	return data.get(row).get(column);
    }
    public void set(int row, int column, int value) throws IndexOutOfBoundsException{
	data.get(row).set(column, value);
    }
    public int size(){
	return this.n;
    }
}
