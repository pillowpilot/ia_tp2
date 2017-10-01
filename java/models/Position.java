package models;

import java.lang.*;

public class Position implements Comparable<Position>{
    public final int row, column;
    Position(int row, int column){
	this.row = row;
	this.column = column;
    }
    @Override
    public boolean equals(Object other){
	if( other instanceof Position ){
	    Position p = (Position)other;
	    if( row == p.row && column == p.column )
		return true;
	    else
		return false;
	}else return false;
    }
    @Override
    public int hashCode(){
	int result = 2017;
	result = 37*result + row;
	result = 37*result + column;
	return result;
    }
    @Override
    public int compareTo(Position other){
	if( row < other.row || (row == other.row && column < other.column) ) return -1;
	if( row == other.row && column == other.column ) return 0;
	else return 1;
    }
    public String toString(){
	return "(" + row + ", " + column + ")";
    }
}
