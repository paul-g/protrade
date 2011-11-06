package org.ic.tennistrader.utils;

public class Pair<T, K> {
  T i;
  K j;
  
  public Pair(T i, K j){
    this.i = i;
    this.j = j;
  }
  
  public T getI(){
    return this.i;
  }
  
  public K getJ(){
    return this.j;
  }
}
