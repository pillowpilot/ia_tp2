#include <bits/stdc++.h>

#include "state.hpp"

using namespace std;

int main(int argc, char* argv[]){
  const int seed = 2017;
  default_random_engine gen(seed);

  const int rank = 3;
  State state(rank, seed);
  state.print();

  const int emptyEntries = 10;
  uniform_int_distribution<int> dist(0, rank*rank-1);
  for(int i = 0; i < emptyEntries; i++){
    int row, column;
    do{
      row = dist(gen);
      column = dist(gen);
    }while( state.isEmpty(row, column) );
    state.get(row, column) = 0;
  }
  state.print();

  
  return 0;
}
