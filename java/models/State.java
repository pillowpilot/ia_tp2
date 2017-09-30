package models;

import java.lang.*;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;
import java.util.Vector;
import java.util.Iterator;

public class State implements Serializable{
    public static final int EMPTY = 0;    
    private SquareMatrix state;
    private int rank;
    private int size;

    State(int rank) throws IllegalArgumentException {
        if (rank < 1) {
            throw new IllegalArgumentException("Rank must be positive.");
        }

        this.rank = rank;
        size = rank * rank;
        state = new SquareMatrix(size);
    }

    State(State other) {
        this.rank = other.rank;
        this.size = other.size;
        this.state = new SquareMatrix(other.state);
    }

    public static State fromString(String s) throws IllegalArgumentException{
	final String[] values = s.split(" ");
	final int rank = (int) Math.sqrt(Math.sqrt(values.length));
	if( rank*rank*rank*rank != values.length )
	    throw new IllegalArgumentException("String provided is not a valid state. There is not integer rank such that rank^4 = tokens.");

	State state = new State(rank);
	int row, column;
	row = column = 0;
	for(String token: values){
	    int value;
	    if( token.equals("_") ) value = 0;
	    else value = Integer.parseInt(token);
	    state.set(row, column, value);
	    column++;
	    if( column == state.size ){
		column = 0;
		row++;
	    }
	}
	return state;
    }
    public int getRank(){ return rank; }
    public int getMaxValue(){ return rank*rank; }

    public int get(int row, int column) throws IndexOutOfBoundsException {
        if (!(0 <= row && row < size && 0 <= column && column < size)) {
            throw new IndexOutOfBoundsException("Indexes must belong to [0, " + size + ")x[0, " + size + ").");
        }
        return state.get(row, column);
    }

    public void set(int row, int column, int value) throws IndexOutOfBoundsException {
        if (!(0 <= row && row < size && 0 <= column && column < size)) {
            throw new IndexOutOfBoundsException("Indexes must belong to [0, " + size + ")x[0, " + size + ").");
        }
        state.set(row, column, value);
    }

    public Row getRow(int row) throws IndexOutOfBoundsException {
        if (!(0 <= row && row < size)) {
            throw new IndexOutOfBoundsException("Row index must belong to [0, " + size + ").");
        }
        return new Row(row);
    }

    public Column getColumn(int column) throws IndexOutOfBoundsException {
        if (!(0 <= column && column < size)) {
            throw new IndexOutOfBoundsException("Column index must belong to [0, " + size + ").");
        }
        return new Column(column);
    }

    public Block getBlock(int row, int column) throws IndexOutOfBoundsException {
        if (!(0 <= row && row < rank && 0 <= column && column < rank)) {
            throw new IndexOutOfBoundsException("Block indexes must belong to [0, " + rank + ")x[0, " + rank + ").");
        }
        return new Block(row, column);
    }

    public String toString(){
	StringBuilder output = new StringBuilder();

	output.append(String.format("Rank = %d%n", rank));
	final int width = (int)Math.log10(size*size)+2;
	final String formatValue = "%"+width+"d";
	final String formatEmpty = "%"+width+"c";
	for(int i = 0; i < size; i++){
	    if( i % rank == 0 )
		output.append(horizontalLine(width));
	    for(int j = 0; j < size; j++){
		if( j % rank == 0 )
		    output.append("|");
		final int value = get(i, j);
		if( value == 0 )
		    output.append(String.format(formatEmpty, '_'));
		else
		    output.append(String.format(formatValue, get(i, j)));
	    }
	    output.append(String.format("|%n"));
	}
	output.append(horizontalLine(width));
	return output.toString();
    }
    public void print(){
	System.out.println(this);
    }

    public Vector<Position> getEmptyValues() {
        Vector<Position> emptyValues = new Vector<Position>();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (state.get(row, column) == 0) {
                    emptyValues.add(new Position(row, column));
                }
            }
        }
        return emptyValues;
    }
    public boolean isValid() {
        for (int i = 0; i < rank; i++) {
            for (int j = 0; j < rank; j++) {
                if (!checkBlock(i, j)) {
                    return false;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            if (!checkRow(i)) {
                return false;
            } else if (!checkColumn(i)) {
                return false;
            }
        }
        return true;
    }

    public void randomLoad(int emptyValues){
	for(int i = 0; i < size; i++){
	    for(int j = 0; j < size; j++){
		int value = ThreadLocalRandom.current().nextInt(1, getMaxValue()+1); // [1, size+1)
		state.set(i, j, value);
	    }
	}
	for(int i = 0; i < emptyValues; i++){
	    int row, column;
	    do{
		row = ThreadLocalRandom.current().nextInt(0, size);
		column = ThreadLocalRandom.current().nextInt(0, size);
	    }while(get(row, column) == EMPTY);
	    set(row, column, EMPTY);
	}
    }
    private String horizontalLine(int width) {
        StringBuilder output = new StringBuilder();
        final int times = width * size + size / rank;
        for (int i = 0; i < times; i++) {
            output.append("-");
        }
        output.append(String.format("%n"));
        return output.toString();
    }
    public boolean checkVirtualLine(Iterable<Integer> virtualLine) {
        boolean[] used = new boolean[getMaxValue()];
        for (Integer value : virtualLine) {
            if(value != EMPTY){
                if (used[value - 1]) {
                    return false;
                } else {
                    used[value - 1] = true;
                }
            }
        }
        return true;
    }

    public boolean checkRow(int row) {
        return checkVirtualLine(getRow(row));
    }

    public boolean checkColumn(int column) {
        return checkVirtualLine(getColumn(column));
    }

    public boolean checkBlock(int row, int column) {
        return checkVirtualLine(getBlock(row, column));
    }

    // Inner classes
    class Row implements Iterable<Integer> {

        private int row;

        Row(int row) {
            this.row = row;
        }

        public Iterator<Integer> iterator() {
            return new RowIterator();
        }

        class RowIterator implements Iterator<Integer> {

            private int index;

            public boolean hasNext() {
                return index < state.size();
            }

            public Integer next() {
                return state.get(row, index++);
            }

            public void remove() {
                throw new UnsupportedOperationException("Items cannot be removed.");
            }
        }
    }

    class Column implements Iterable<Integer> {

        private int column;

        Column(int column) {
            this.column = column;
        }

        public Iterator<Integer> iterator() {
            return new ColumnIterator();
        }

        class ColumnIterator implements Iterator<Integer> {

            private int index;

            public boolean hasNext() {
                return index < state.size();
            }

            public Integer next() {
                return state.get(index++, column);
            }

            public void remove() {
                throw new UnsupportedOperationException("Items cannot be removed.");
            }
        }
    }

    class Block implements Iterable<Integer> {

        private int row, column;

        Block(int row, int column) {
            this.row = row * rank;
            this.column = column * rank;
        }

        public Iterator<Integer> iterator() {
            return new BlockIterator();
        }

        class BlockIterator implements Iterator<Integer> {

            private int irow, icolumn;

            public boolean hasNext() {
                return irow < rank;
            }

            public Integer next() {
                Integer v = state.get(row + irow, column + icolumn);
                advance();
                return v;
            }

            public void remove() {
                throw new UnsupportedOperationException("Items cannot be removed.");
            }

            private void advance() {
                icolumn++;
                if (icolumn == rank) {
                    irow++;
                    icolumn = 0;
                }
            }
        }
    }
    
    public State clone(){ // TODO Use copy constructor...
        State s = new State(this.rank);
        for(int i = 0; i < s.size; i++){
            for(int j = 0; j < s.size; j++){
                s.set(i, j, this.get(i, j));
            }
        }
        return s;
    }

    public static void main(String[] args) {
        test(2);
        test(3);
        test(4);
    }

    private static void test(int rank) {
        State s = new State(rank);
        System.out.println(s);
        s.randomLoad(rank);
        System.out.println(s);
        for (Integer v : s.getRow(0)) {
            System.out.print(v + " ");
        }
        System.out.println();
        for (int v : s.getColumn(rank - 1)) {
            System.out.print(v + " ");
        }
        System.out.println();

        for (int v : s.getBlock(rank - 1, rank - 1)) {
            System.out.print(v + " ");
        }
        System.out.println();
    }
}

class SquareMatrix implements Serializable{
    private Vector<Vector<Integer>> data;
    private final int n;

    SquareMatrix(int n) throws IllegalArgumentException {
        if (!(0 < n)) {
            throw new IllegalArgumentException("Matrix dimentions must be positive.");
        }

        data = new Vector<Vector<Integer>>(n);
        for (int i = 0; i < n; i++) {
            Vector<Integer> v = new Vector<Integer>(n);
            for (int j = 0; j < n; j++) {
                v.add(0);
            }
            data.add(v);
        }

        this.n = n;
    }

    SquareMatrix(SquareMatrix other) {
        this.n = other.n;
        this.data = new Vector<Vector<Integer>>();
        for (Vector<Integer> other_row : other.data) {
            Vector<Integer> row = new Vector<Integer>(other_row);
            this.data.add(row);
        }
    }

    public Integer get(int row, int column) throws IndexOutOfBoundsException {
        return data.get(row).get(column);
    }

    public void set(int row, int column, int value) throws IndexOutOfBoundsException {
        data.get(row).set(column, value);
    }

    public int size() {
        return this.n;
    }
}
