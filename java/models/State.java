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
    public Row getRow(int row) throws IndexOutOfBoundsException{
	if( !(0<=row&&row<size) ) throw new IndexOutOfBoundsException("Row index must belong to [0, "+size+").");
	return new Row(row);
    }
    public Column getColumn(int column) throws IndexOutOfBoundsException{
	if( !(0<=column&&column<size) ) throw new IndexOutOfBoundsException("Column index must belong to [0, "+size+").");
	return new Column(column);
    }
    public Block getBlock(int row, int column) throws IndexOutOfBoundsException{
	if( !(0<=row&&row<rank&&0<=column&&column<rank) )
	    throw new IndexOutOfBoundsException("Block indexes must belong to [0, "+rank+")x[0, "+rank+").");
	return new Block(row, column);
    }
    public void print(){ // TODO Better print
	System.err.println("Rank = " + rank);
	state.print();
    }
    public void randomload(){
	for(int i = 0; i < size; i++){
	    for(int j = 0; j < size; j++){
		int value = ThreadLocalRandom.current().nextInt(0, size+1); // [0, size+1)
		state.set(i, j, value);
	    }
	}
    }    

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
	s.print();
	s.randomload();
	s.print();
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

	System.err.println("n = "+n);
	
	data = new Vector<Vector<Integer>>(n);
	for(int i = 0; i < n; i++){
	    Vector<Integer> v = new Vector<Integer>(n);
	    for(int j = 0; j < n; j++) v.add(0);
	    data.add(v);
	}
	
	this.n = n;
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
    public void print(){ // TODO add param, better print
	for(int i = 0; i < n; i++){
	    for(int j = 0; j < n; j++){
		System.err.format("%4d", get(i, j));
	    }
	    System.err.println();
	}
    }
}
