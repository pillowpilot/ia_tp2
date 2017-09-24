#include <bits/stdc++.h>
#include "state.hpp"
#include "pprint.hpp"

int main(int argc, char* argv[]){
  using namespace std;

  { 
    State state(3);
    state.print();

    for(auto&& i: state.getRow(0)){
      cout << i << " ";
    }
    cout << endl;

    for(auto&& i: state.getRow(1)){
      cout << i << " ";
    }
    cout << endl;

    for(auto&& i: state.getColumn(0)){
      cout << i << " ";
    }
    cout << endl;

    for(auto&& i: state.getColumn(5)){
      cout << i << " ";
    }
    cout << endl;

    // BLock
    for(auto&& i: state.getBlock(0, 0)){
      cout << i << " ";
    }
    cout << endl;

    for(auto&& i: state.getBlock(2, 2)){
      cout << i << " ";
    }
    cout << endl;
  }

  {
    State state(4);
    state.print();

        for(auto&& i: state.getRow(0)){
      cout << i << " ";
    }
    cout << endl;

    for(auto&& i: state.getRow(1)){
      cout << i << " ";
    }
    cout << endl;

    for(auto&& i: state.getColumn(0)){
      cout << i << " ";
    }
    cout << endl;

    for(auto&& i: state.getColumn(5)){
      cout << i << " ";
    }
    cout << endl;

    // BLock
    for(auto&& i: state.getBlock(0, 0)){
      cout << i << " ";
    }
    cout << endl;

    for(auto&& i: state.getBlock(3, 3)){
      cout << i << " ";
    }
    cout << endl;

    try{
      for(auto&& i: state.getBlock(4, 4)){
	cout << i << " ";
      }
      cout << endl;
    }catch(const invalid_argument& ia){
      cout << ia.what() << endl;
    }
  }

  {
    State state(3);
    for(int row = 0; row < 3*3; row++){
      for(auto&& value: state.getRow(row)){
	value = 0;
      }
    }
    state.print();
    {
      Block b(state.getBlock(0, 0));
      for(auto iter = b.begin(); iter != b.end(); iter++){      
	cout << "Valid values: " << iter.getValidValues() << endl;
      }
    }
    state.randomload();
    state.print();
    {
      Block b(state.getBlock(0, 0));
      for(auto iter = b.begin(); iter != b.end(); iter++){      
	cout << "Valid values: " << iter.getValidValues() << endl;
      }
    }
  }
  
  
  //state.print();
  return 0;
}
