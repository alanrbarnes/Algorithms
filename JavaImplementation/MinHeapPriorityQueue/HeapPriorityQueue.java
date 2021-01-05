//Written By: Alan Barnes
//Written on: September 19, 2020
//Written for: CSC 482, Assignment 1
import java.util.*;
import java.io.*;
import java.lang.*;

public class HeapPriorityQueue<T extends Comparable<T>> {

    /////////////
    //Attributes
    ////////////
    //Array to store the elements contained in the heap
    //array starts at index 1
    private heapNode[] heapset;
    //private MaxHeapInterface<T> pq;
    public int length; //number of elements in the heap
    private int capacityOfHeap; //capacity of the heap
    //hashtable for finding keys
    //hastable<key, value>, Key is item, value is index in array
    private Dictionary queDictionary;
    private boolean empty;
    private heapNode firstNode;
    private heapNode lastNode;



    //min



    //constructor for class
    //1. with capacity of 1 
    public HeapPriorityQueue() {
      clear(1);
    }

    //constructor for class
    //2. with a predetermined capacityOfHeap
    public HeapPriorityQueue(int N) {
      clear(N);
    }
    
    //constructor for class
    //3. With specific elements



 


    ////////////////////////////////////////
    //Implemented Methods from Interface
    ////////////////////////////////////////
    //add a new item to the end of the heap
    //Left child = 2i
    //Right child = 2i + 1
    //parent = i/2
    
    //remove all items from the heap
    public void clear(int capacity) {
      length = 0;
      //add 1 to capacity because of 1 based array
      capacityOfHeap = capacity + 1;
      empty = true;
      queDictionary = new Hashtable();
      heapset = new heapNode[capacityOfHeap];
      
    }
    
    
    
    public void add(T newEntry) {
      Insert(newEntry, length + 1);
    }
    
    /*

    //remove and return the smallest element from the heap
    public T removeMin() {
      ExtractMin();
    }

    //get the smallest element from the heap
    public T getMin() {
      return FindMin();
      
    }

    //find out if the heap is empty
    public boolean isEmpty() {
      if (empty) 
      {return true;}
      else 
      {return false;}
    }

    //return the size of the heap
    public int getSize() {
      return length;
    }

    
    public Comparable peek() {
      return FindMin();
      
    }
    
    public void remove() {
    
    }

*/
    ///////////////////////////////////////
    //Implementation Detail methods
    ///////////////////////////////////////

    //move an element in a lower position whose value is lower than its parent
    //up into a higher position in the heap
    public void Heapify_UP(int index) {
        //System.out.print("***Heapify_UP***");
        int parentIndex = index/2;
        //if node is the first node return
        if(parentIndex < 1) {
          return;
        }
        
        int parentPriority = heapset[parentIndex].priorityValue;
        //System.out.print(parentPriority + "_");
        int selfPriority = heapset[index].priorityValue;
        //System.out.print(selfPriority + "_");
        
        if (selfPriority < parentPriority) {
          heapNode node = heapset[parentIndex];
          heapset[parentIndex] = heapset[index];
          //Add element to dictionary
          queDictionary.put(heapset[index], parentIndex);
          
          heapset[index] = node;
          //Add element to dictionary
          queDictionary.put(node, index);
          
          index = parentIndex;
          Heapify_UP(index);
          //Add elements to dictionary
          //queDictionary.put(heapset[index], index);
          //queDictionary.put(heapset[parentIndex], parentIndex);
        }
        
    }
    

    //move an element in a higher position whose value is greater than its children
    //down into a lower position in the heap
    public void Heapify_Down(int index) {
        System.out.print("***Heapify_Down***");
        //return if the index is null
        if(heapset[index] == null)
        {return;}
        
        //get priority of left child node
        boolean left = true;
        boolean right = true;
        int leftChildPriority =  -1;
        int rightChildPriority = -1;
        
        //get priority of left child node
        if (heapset[index*2] != null)
        {leftChildPriority = heapset[index*2].priorityValue;}
        else 
        {left = false;}
        
        //get priority of right child node
        if (heapset[index*2] != null)
        {rightChildPriority = heapset[(index * 2) + 1].priorityValue;}
        else 
        {right = false;}
        
        //priority of current index.
        int selfPriority = heapset[index].priorityValue;
        
        //base case, return if the heap is in proper order or there are no children
        if (((selfPriority < leftChildPriority) || left == false) && ((selfPriority < rightChildPriority) || right == false))
        {return;}
        
        int smallestChild = -1;
        //find out which child is the smallest node
        //left child = 1, right child = 2
        if (leftChildPriority < rightChildPriority)
        {smallestChild = 1;}
        else
        {smallestChild = 2;}
        
        
        //recurse the method untill the tree is traversed
        if ((smallestChild == 1) && (leftChildPriority < selfPriority) && (heapset[index*2] != null) && (heapset[index] != null)) {
            //swap left child and current node
            heapNode node = heapset[index*2];
            heapset[index*2] = heapset[index];
            //Add element to dictionary
            queDictionary.put(heapset[index], index*2);
            
            heapset[index] = node;
            //Add element to dictionary
            queDictionary.put(node, index);
            
            index = index*2;
            
            //recursivly call heapify down()
            Heapify_Down(index);
            return;
        }
        if ((rightChildPriority < selfPriority) && (heapset[index*2 + 1] != null) && (heapset[index] != null)) {
            //swap right child and current node
            heapNode node = heapset[index*2 + 1];
            heapset[index*2 + 1] = heapset[index];
            //Add element to dictionary
            queDictionary.put(heapset[index], index*2 + 1);
            
            heapset[index] = node;
            //Add element to dictionary
            queDictionary.put(node, index);
            
            index = index*2 + 1;
            
            //recursivly call heapify down()
            Heapify_Down(index);
            return;
        }
        
    }
    

    //initialize a new empty heap
    //with a capacity of N Nodes
    public void StartHeap(int N) {
        clear(N);
    }

    
    //insert an item with an ordering value into the heap at index 0
    //the call Heapify_Down to fix the heap
    public void Insert(T item, int priority) {
      //Create a new node to add to the heap
      heapNode node = new heapNode(item, priority);
      
      //Check to see if the array needs to be resized
      CheckCapacity();
      //Add the node to the array
      heapset[length + 1] = node;
      //add the node to the dictionary
      queDictionary.put(node, length + 1);
      
      
      //If the list is empty, define the node to be first and last
      if (empty) {
        firstNode = node;
        lastNode = node;
        //list is not empty 
        empty = false;
      }
      //If the list is not empty, define the node as only the last
      else {
        lastNode = node;
      }
      
      //increment the list by one to reflect the node added
      length = length +1;
      
      //find location of element in array;
      Heapify_UP(length);
     //  int index = Heapify_UP(length);
      
      //Add element to dictionary
      // queDictionary.put(node, index);
    }
    

    


    //find the minimum element in the heap
    public Comparable FindMin() {
       if (empty) {
         return null;
       } 
       
       Comparable value = firstNode.dataValue;
        return value;
    }
    
        

    //delete an element at a speicfied index
    public void Delete(int index) {
      if (heapset[length] == null)
      {System.out.println("Null Value");}
      
      heapNode node = heapset[index];
      
      //move last element in list to index of deleted element
      heapset[index] = heapset[length];
      
      //set index of last element of list to null
      heapset[length] = null;
      
      //call Heapify_Down() to fix heap from the top down
      Heapify_Down(index);
      
      Integer e = (Integer)queDictionary.get(node);
      index = e.intValue();
      
      //call Heapify_UP() to fix heap from the bottom up
      Heapify_UP(index);
      
      //delete 1 from the length of the list
      length = length - 1;
    }
    
    

    //identify and delete the element with the minimum key value
    public T ExtractMin() {
      Comparable value  = firstNode.dataValue;
      Delete(1);
      return (T)value;
    }

    //delete the element item from the heap
    public void Delete(T item) {
      Integer e = (Integer)queDictionary.get(item);
      int index = e.intValue();
      Delete(index);
    }

    //Change a key value of element v to new value w
    public void ChangeKey(T item, T newValue) {
      Integer e = (Integer)queDictionary.get(item);
      int index = e.intValue();
      heapNode node = heapset[index];
      node.dataValue = newValue;
      Heapify_Down(index);
      Heapify_UP(index);
    }
    

    

    ///////////////////////////////////////
    //Helper Methods for implementation
    ///////////////////////////////////////
    public void CheckCapacity() {
      //check to see if the heap is >= 50% of the capacity
      if (length >= (capacityOfHeap / 2)) {
        resizeHeap();
      }
    }

    public void resizeHeap() {
      //System.out.println("resize");
      //if heap is >= 50% of the size of the capacity then double the size
      //create a temporary array to hold the values
      heapNode[] tempArray = heapset;
      capacityOfHeap = capacityOfHeap * 2;
      heapset = new heapNode[capacityOfHeap];
      System.arraycopy(tempArray, 1, heapset, 1, length);
    }

    public void printQue() {
      System.out.println("*******************************");
      if (empty)
      {System.out.println("que is empty");}
      else {
        for(int i = 1; i <= length; i++) {
        System.out.print("*" + heapset[i].dataValue + "," + heapset[i].priorityValue + "*");
        }
      }
      System.out.println("");
      System.out.println("*******************************");
    }
}
