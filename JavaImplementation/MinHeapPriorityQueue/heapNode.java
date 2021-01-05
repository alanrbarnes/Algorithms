//Written By: Alan Barnes
//Written on: September 19, 2020
//Written for: CSC 482, Assignment 1

import java.util.*;

public class heapNode<T extends Comparable<T>> {
  public T dataValue;
  public int priorityValue;

  public heapNode(T value, int priority){
    dataValue = value;
    priorityValue = priority;
  }

}
