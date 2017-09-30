package models;

public class Position{
    public final int row, column;
    Position(int row, int column){
	this.row = row;
	this.column = column;
    }
    public boolean equals(Object other){
	if( other instanceof Position ){
	    Position p = (Position)other;
	    if( row == p.row && column == p.column )
		return true;
	    else
		return false;
	}else return false;
    }
    public String toString(){
	return "(" + row + ", " + column + ")";
    }
}