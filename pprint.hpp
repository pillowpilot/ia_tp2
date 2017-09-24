#ifndef __PPRINT_HPP__
#define __PPRINT_HPP__

#include <vector>
#include <string>
#include <iostream>

template <class T>
std::ostream& operator<<(std::ostream& os, const std::vector<T> v) {
  os << "[";
  if( !v.empty() ){
    auto iter = v.begin();
    os << *iter;
    iter++;
    while( iter != v.end() ){
      os << ", " << *iter;
      iter++;
    }
  }
  return os << "]";
}
template <class P, class Q>
std::ostream& operator<<(std::ostream& os, const std::pair<P, Q> p){
  return os << "(" << p.first << ", " << p.second << ")";
}

#endif
