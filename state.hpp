#ifndef __STATE_HPP__
#define __STATE_HPP__

#include <vector>
#include <stdexcept>
#include <random>

#include <iostream>
#include <iomanip>
#include <cmath>

typedef std::vector< std::vector<int> > BoardType;

class Column {
private:
  BoardType& board;
  int column;
public:
  template <class T> class ColumnIterator{
  private:
    BoardType& board;
    int column, index;
  public:
    ColumnIterator(BoardType& board, int column, int index=0): board(board), column(column), index(index) {}
    ColumnIterator(const ColumnIterator& other): board(other.board), column(other.column), index(other.index) {}
    T& operator*() const { return board[index][column]; }
    ColumnIterator<T> operator++(){ ColumnIterator<T> t(*this); index++; return t; }
    ColumnIterator<T> operator++(int){ index++; return *this; }
    bool operator==(const ColumnIterator<T>& rhs) const { return areEqual(rhs); }
    bool operator!=(const ColumnIterator<T>& rhs) const { return !areEqual(rhs); }

    std::vector<int> getValidValues() const {
      const int size = board.size();
      std::vector<bool> used(size+1, false);      
      for(auto&& value: Column(board, column)){
	used[value] = true;
      }
      std::vector<int> validValues;
      for(int value = 1; value <= size; value++) if( !used[value] ) validValues.push_back(value);
      return validValues;
    }
  private:
    bool areEqual(const ColumnIterator<T>& rhs) const { return board==rhs.board && column==rhs.column && index==rhs.index; }
  };

  using iterator = ColumnIterator<int>;
  using const_iterator = ColumnIterator<const int>;

  Column(BoardType& board, int column): board(board), column(column) {
    if( !(0<=column&&column<board.size()) )
      throw std::invalid_argument("Column " + std::to_string(column) + " must belong to [0, " +
			     std::to_string(board.size()) + ").");
  }
  Column(const Column& other): board(other.board), column(other.column) { }

  iterator begin() { return iterator(board, column); }
  iterator end() { return iterator(board, column, board.size()); }
  const_iterator begin() const { return const_iterator(board, column); }
  const_iterator end() const { return const_iterator(board, column, board.size()); }
};

class Row{
private:
  BoardType& board;
  int row;
public:
  template <class T> class RowIterator {
  private:
    BoardType& board;
    int row, index;
  public:
    RowIterator(BoardType& board, int row, int index=0): board(board), row(row), index(index) {}
    RowIterator(const RowIterator& other): board(other.board), row(other.row), index(other.index) {}
    RowIterator<T> operator++(){ RowIterator t(*this); index++; return t; }
    RowIterator<T> operator++(int){ index++; return *this; }
    T& operator*(){ return board[row][index]; }
    bool operator==(const RowIterator& rhs) const { return areEqual(rhs); }
    bool operator!=(const RowIterator& rhs) const { return !areEqual(rhs); }

    std::vector<int> getValidValues() const {
      const int size = board.size();
      std::vector<bool> used(size+1, false);      
      for(auto&& value: Row(board, row)){
	used[value] = true;
      }
      std::vector<int> validValues;
      for(int value = 1; value <= size; value++) if( !used[value] ) validValues.push_back(value);
      return validValues;
    }
  private:
    bool areEqual(const RowIterator<T>& rhs) const { return board==rhs.board && row==rhs.row && index==rhs.index; }
  };

  using iterator = RowIterator<int>;
  using const_iterator = RowIterator<const int>;

  Row(BoardType& board, int row): board(board), row(row) {
    if( !(0<=row&&row<board.size()) )
      throw std::invalid_argument("Row " + std::to_string(row) + " must belong to [0, " +
			     std::to_string(board.size()) + ").");
  }
  Row(const Row& other): board(other.board), row(other.row) { }
  iterator begin() { return iterator(board, row); }
  iterator end() { return iterator(board, row, board[row].size()); }
  const_iterator begin() const { return const_iterator(board, row); }
  const_iterator end() const { return const_iterator(board, row, board[row].size()); }
};

class Block {
private:
  BoardType& board;
  int rank;
  int pivot_row, pivot_column;
public:
  template <class T> class BlockIterator {
  private:
    BoardType& board;
    int rank;
    int pivot_row, pivot_column;
    int row, column;
  public:
    BlockIterator(BoardType& board, int rank, int pivot_row, int pivot_column, int row=0, int column=0):
      board(board), rank(rank), pivot_row(pivot_row), pivot_column(pivot_column), row(row), column(column) {}
    BlockIterator(const BlockIterator& other): board(other.board), pivot_row(other.pivot_row), pivot_column(other.pivot_column) {}
    BlockIterator<T> operator++(){ BlockIterator<T> t(*this); advance(); return t; }
    BlockIterator<T> operator++(int){ advance(); return *this; }
    T& operator*(){ return board[pivot_row+row][pivot_column+column]; }
    bool operator==(const BlockIterator<T>& rhs) const { return areEqual(rhs); }
    bool operator!=(const BlockIterator<T>& rhs) const { return !areEqual(rhs); }

    std::vector<int> getValidValues() const {
      const int size = board.size();
      std::vector<bool> used(size+1, false);      
      for(auto&& value: Block(board, rank, std::make_pair(pivot_row, pivot_column))){
	used[value] = true;
      }
      std::vector<int> validValues;
      for(int value = 1; value <= size; value++) if( !used[value] ) validValues.push_back(value);
      return validValues;
    }
  private:
    bool areEqual(const BlockIterator<T>& rhs) const {
      return board==rhs.board && pivot_row==rhs.pivot_row && pivot_column==rhs.pivot_column && row==rhs.row && column==rhs.column;
    }
    void advance(){
      column++;
      if( column == rank ){
	column = 0;
	row++;
      }
    }
  };

  using iterator = BlockIterator<int>;
  using const_iterator = BlockIterator<const int>;

  Block(BoardType& board, int rank, std::pair<int, int> pivot): board(board), rank(rank), pivot_row(pivot.first), pivot_column(pivot.second) { }
  Block(const Block& other): board(other.board), rank(other.rank), pivot_row(other.pivot_row), pivot_column(other.pivot_column) { }
  iterator begin() { return iterator(board, rank, pivot_row, pivot_column); }
  iterator end() { const auto endState = getEndState(); return iterator(board, rank, pivot_row, pivot_column, endState.first, endState.second); }
  const_iterator begin() const { return const_iterator(board, rank, pivot_row, pivot_column); }
  const_iterator end() const { const auto endState = getEndState(); return const_iterator(board, rank, pivot_row, pivot_column, endState.first, endState.second); }
private:
  std::pair<int, int> getEndState() const {
    return std::make_pair(rank, 0); // row = rank, column = 0
  }
};

class State{
private:
  int seed;
  std::default_random_engine generator;
  std::uniform_int_distribution<int> distribution;
  int rank, size;
  BoardType board;

public:
  State(int rank, int seed=2017): rank(rank), seed(seed), generator(seed){
    if( 0 < rank ){
      size = rank*rank;
      board.assign(size, std::vector<int>(size, 0));
      distribution = std::uniform_int_distribution<int>(1, getMaxValue());
      
      randomload();
    }else throw std::invalid_argument("Rank must be positive.");
  }

  int getMaxValue() const { return rank*rank; }

  bool isEmpty(int row, int column) const {
    if( !(0<=row&&row<size) || !(0<=column&&column<size) )
      throw std::invalid_argument("Row column (" + std::to_string(row) + ", " + std::to_string(column) +
				  ") must belong to [0, " + std::to_string(size) + ")x[0, " + std::to_string(size) + "). ");
    return board[row][column] == 0;
  }

  int& get(int row, int column) {
    if( !(0<=row&&row<size) || !(0<=column&&column<size) )
      throw std::invalid_argument("Row column (" + std::to_string(row) + ", " + std::to_string(column) +
				  ") must belong to [0, " + std::to_string(size) + ")x[0, " + std::to_string(size) + "). ");
    return board[row][column];
  }

  Row getRow(int row) { // TODO Valid domain for row? Currently [0, size)
    if( !(0<=row&&row<size) )
      throw std::invalid_argument("Row " + std::to_string(row) + " must belong to [0, " + std::to_string(size) + ").");
    return Row(board, row);
  }

  Column getColumn(int column) { // TODO Valid domain for col? Currently [0, size)
    if( !(0<=column&&column<size) )
      throw std::invalid_argument("Column " + std::to_string(column) + " must belong to [0, " + std::to_string(size) + ").");
    return Column(board, column);
  }

  Block getBlock(int row, int column) { // TODO Valid domain..?? [0, rank)x[0, rank)
    if( !(0<=row&&row<rank) || !(0<=column&&column<rank) )
      throw std::invalid_argument("Block id (" + std::to_string(row) + ", " + std::to_string(column) +
				  ") must belong to [0, " + std::to_string(rank) + ")x[0, " + std::to_string(rank) + "). ");
    return Block(board, rank, getBlockPivot(row, column));
  }

  void randomload(){
    for(int row = 0; row < size; row++){
      for(int column = 0; column < size; column++){
	int value = distribution(generator);
	board[row][column] = value;
      }
    }
  }

  void print() const {
    using namespace std;
    const int width = (int)(log10(size*size)+2);

    printHorizontalLine(width);
    for(int i = 0; i < board.size(); i++){
      for(int j = 0; j < board[i].size(); j++){
	if( j%rank == 0 ) cout << "|";
	if( board[i][j] == 0 )
	  cout << fixed << setw(width) << "_";
	else
	  cout << fixed << setw(width) << board[i][j];
      }
      cout << "|";
      cout << endl;
      if( (i+1)%rank == 0 ) printHorizontalLine(width);
    }
    cout << endl;
  }

  void dummyload(){
    int v = 1;
    for(int i = 0; i < board.size(); i++){
      for(int j = 0; j < board[i].size(); j++){
	board[i][j] = v;
	v++;
      }
    }
  }

private:
  std::pair<int, int> getBlockPivot(int block_row, int block_column) const {
    return std::make_pair(block_row*rank, block_column*rank);
  }
  void printHorizontalLine(int width) const {
    for(int j = 0; j < width*size+size/rank; j++){
      std::cout << "-";
    }
    std::cout << std::endl;    
  }
};
#endif
