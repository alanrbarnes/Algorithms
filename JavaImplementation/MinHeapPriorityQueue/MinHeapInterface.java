//Written by: Alan Barnes
//Written on: September 19, 2020
//Written for: CSC 482, Assignment 1

public interface MinHeapInterface<T extends Comparable<T>> {

  //add a new element into the heap
  public void add(T newEntry);

  //remove and return the smallest element from the heap
  public T removeMin();

  //get the smallest element from the heap
  public T getMin();

  //find out if the heap is empty
  public boolean isEmpty();

  //return the size of the heap
  public int getSize();

  //remove all items from the heap
  public void clear();
}
