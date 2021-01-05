//Written By: Alan Barnes
//Written on: September 19, 2020
//Written for: CSC 482, Assignment 1
import java.util.*;
import java.io.*;
import java.lang.*;

public class HeapPriorityQueue<T> {

    /////////////
    //Attributes
    ////////////
    //Array to store the elements contained in the heap
    //array starts at index 1
    private heapNode<T>[] heapset;
    //private MaxHeapInterface<T> pq;
    public int length; //number of elements in the heap
    private int capacityOfHeap; //capacity of the heap
    //hashtable for finding keys
    //hastable<key, value>, Key is item, value is index in array
    private Map<heapNode<T>, Integer> queDictionary;
    private Map<T, heapNode<T>> StringToNode;
    private boolean empty;
    private heapNode<T> firstNode;
    private heapNode<T> lastNode;




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
      StringToNode = new Hashtable();
      heapset = new heapNode[capacityOfHeap];
      
    }
    
    
    
    public void add(T newEntry) {
      Insert(newEntry, length + 1);
    }
    
 

    //remove and return the smallest element from the heap
    public T removeMin() {
      return ExtractMin();
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

    
    public T peek() {
      return FindMin();
      
    }
    
    public void remove() {
    
    }
    
    public boolean contains(T item) {
        boolean result = false;
 //        for(int i = 0; i < length; i++) {
//             if(heapset[i] == item)
//             {result = true;}
//         }
         if (StringToNode.containsKey(item))
         {result = true;}
        return result;
    }
    


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
        
        float parentPriority = heapset[parentIndex].priorityValue;
        //System.out.print(parentPriority + "_");
        float selfPriority = heapset[index].priorityValue;
        //System.out.print(selfPriority + "_");
        
        if (selfPriority < parentPriority) {
          //Store parent node in variable
          heapNode<T> node = heapset[parentIndex];
          //put  child node in parent index
          heapset[parentIndex] = heapset[index];
          //Add new parent element to node dictionary
          StringToNode.put(heapset[index].getValue(), heapset[index]);
          //Add new parent element to index dictionary
          queDictionary.put(heapset[index], parentIndex);
          
          //Store parent node in index of child
          heapset[index] = node;
          //Add new chile element to node dictionary
          StringToNode.put(node.getValue(), heapset[index]);
          //Add new child element to index dictionary
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
        //System.out.print("***Heapify_Down***");
        //return if the index is null
        if(heapset[index] == null)
        {return;}
        
        //get priority of left child node
        boolean left = true;
        boolean right = true;
        float leftChildPriority =  -1;
        float rightChildPriority = -1;
        
        //Check to see if the array needs to be resized
        CheckCapacity();
      
        //get priority of left child node
        if (heapset[index*2] != null)
        {leftChildPriority = heapset[index*2].priorityValue;}
        else 
        {left = false;}
        
        
        //get priority of right child node
        if (heapset[index*2 + 1] != null)
        {rightChildPriority = heapset[(index * 2) + 1].priorityValue;}
        else 
        {right = false;}
        
        //priority of current index.
        float selfPriority = heapset[index].priorityValue;
        
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
            //Store child node to variable
            heapNode<T> node = heapset[index*2];
            //put parent node to child index
            heapset[index*2] = heapset[index];
            //Add new child element to node dictionary
            StringToNode.put(heapset[index].getValue(), heapset[index]);
            //Add new child element to index dictionary
            queDictionary.put(heapset[index], index*2);
            
            //put child node into parent index
            heapset[index] = node;
            //Add new parent element to node dictionary
            StringToNode.put(node.getValue(), node);
            //Add new parent element to index dictionary
            queDictionary.put(node, index);
            
            index = index*2;
            
            //recursivly call heapify down()
            Heapify_Down(index);
            return;
        }
        if ((rightChildPriority < selfPriority) && (heapset[index*2 + 1] != null) && (heapset[index] != null)) {
            //swap right child and current node
            //Store child node to variable
            heapNode<T> node = heapset[index*2 + 1];
            //put parent node to child index
            heapset[index*2 + 1] = heapset[index];
            
            //Add new child element to node dictionary
            StringToNode.put(heapset[index].getValue(), heapset[index]);
            //Add new child element to index dictionary
            queDictionary.put(heapset[index], index*2 + 1);
            
            heapset[index] = node;
            //Add new parent element to node dictionary
            StringToNode.put(node.getValue(), node);
            //Add new parent element to index dictionary
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
    public void Insert(T item, float priority) {
      //Create a new node to add to the heap
      heapNode<T> node = new heapNode(item, priority);
      
      //Check to see if the array needs to be resized
      CheckCapacity();
      //Add the node to the array
      heapset[length + 1] = node;
      //Add node to node dictionary
      StringToNode.put(node.getValue(), node);
      //Add node to index dictionary
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
     
      //printQue();
      //Add element to dictionary
      // queDictionary.put(node, index);
      firstNode = heapset[1];
    }
    

    


    //find the minimum element in the heap
    public T FindMin() {
       if (empty) {
         return null;
       } 
       T value = heapset[1].dataValue;
       return value;
    }
    
        

    //delete an element at a speicfied index
    public void Delete(int index) {
      if (heapset[length] == null)
      {System.out.println("Null Value");}
      
      heapNode<T> node = heapset[index];
      
      //move last element in list to index of deleted element
      heapset[index] = heapset[length];
      
      //set index of last element of list to null
      heapset[length] = null;
      
      //remove the node from the dictionary
      queDictionary.remove(node);
      //StringToNode.put(node, null);
      
      //delete 1 from the length of the list
      length = length - 1;
      
      //call Heapify_Down() to fix heap from the top down
      Heapify_Down(index);
      
      
 //     Integer e = (Integer)queDictionary.get(node);
//       index = e.intValue();
      
      //find the index of the last node that is not null
      
      
      //call Heapify_UP() to fix heap from the index of the swapped node
      Heapify_UP(length);
      

      
      firstNode = heapset[1];
    }
    
    

    //identify and delete the element with the minimum key value
    public T ExtractMin() {
      T value  = heapset[1].getValue();
      Delete(1);
      firstNode = heapset[1];
      return value;
    }

    //delete the element item from the heap
    public void Delete(T item) {
      //get node from node name
      heapNode<T> node = StringToNode.get(item);
      //get index of node in list
      Integer e = (Integer)queDictionary.get(node);
      int index = e.intValue();
      //delete the node
      Delete(index);
      StringToNode.remove(item);
    }

    //Change a key value of element v to new value w
    public void ChangeKey(T item, float newValue) {
      //get node from node name
      heapNode<T> node = StringToNode.get(item);
      //get index of node in list
      Integer e = (Integer)queDictionary.get(node);
      int index = e.intValue();
      
      //heapNode node = heapset[index];
      node.setPriority(newValue);

      //node.dataValue = newValue;
      //node put in dictionary in heapify function
      if(length > 1) {
        Heapify_Down(index);
        Heapify_UP(index);
      }
    }
    
        //Change a key value of element v to new value w
    public void ChangeKey(String item, float newValue) {
      //get node from node name
      heapNode<T> node = StringToNode.get(item);
      //get index of node in list
      Integer e = (Integer)queDictionary.get(node);
      int index = e.intValue();
      
      //heapNode node = heapset[index];
      node.setPriority(newValue);

      //node.dataValue = newValue;
      //node put in dictionary in heapify function
      Heapify_Down(index);
      Heapify_UP(index);
    }
    
    public void AddCostAndParent(T value, T nParent, float nCost) {
        heapNode<T> node = StringToNode.get(value);
        node.setParent(nParent);
        node.setCost(nCost);
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
    
    public void printPriority(T value) {
        heapNode<T> node = StringToNode.get(value);
        System.out.println(node.getPriority());
    }
    
     public heapNode<T> getNode(T value) {
         heapNode<T> node = StringToNode.get(value);
         return node;
     }
    
    
    
    public class heapNode<T> {
    
        private T dataValue;
        public float priorityValue;
        private T parent;  //reference to the nodes parent
        private float cost; //cost of attaching to the parent node

        public heapNode(T value, float priority){
        dataValue = value;
        priorityValue = priority;
        }
        
        public T getValue() {
            return dataValue;
        }
        
        public void setValue(T value) {
            dataValue = value;
        }
        
        public float getPriority() {
            return priorityValue;
        }
        
        public void setPriority(float value) {
            priorityValue = value;
        }
        
        public void setParent(T value) {
            parent = value;
        }
        
        public T getParent() {
            return parent;
        }
        
        public void setCost(float value) {
            cost = value;
        }
        
        public float getCost() {
            return cost;
        }

    }
}
