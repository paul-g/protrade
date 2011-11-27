package org.ic.tennistrader.utils;

public class Pair<T, K> {
  T first;
  K second;
  
  private Pair(T i, K j){
    this.first = i;
    this.second = j;
  }
  
  public T first(){
    return this.first;
  }
  
  public K second(){
    return this.second;
  }
  
  public static <T1,T2> Pair<T1,T2> pair(T1 first, T2 second){
      return new Pair<T1,T2>(first, second);
  }
  
  @Override
  public String toString(){
      return first + "," + second;
  }
}
