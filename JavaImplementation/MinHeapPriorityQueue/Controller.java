//Written By: Alan Barnes
//Written on: September 23, 2020
//Written for: CSC 482, Assignment 1
//testing environment for HeapPriorityQue
import java.util.*;
import java.io.*;
import java.lang.*;

public class Controller {
    public static void main(String[] args) {
      HeapPriorityQueue PQ = new HeapPriorityQueue();
            //System.out.println(PQ.FindMin());
      //add to end of priorityQue
      PQ.add(2);
      PQ.add(4);
      PQ.add(6);
      PQ.add(10);
      PQ.add(2);
      //Insert with priority
      PQ.Insert(5,2);
      PQ.Insert(4,10);
      PQ.Insert(3,1);
      PQ.Insert(2,4);
      PQ.Insert(7,5);
      PQ.Insert(8,3);
      //System.out.print(PQ.FindMin());
      PQ.printQue();
      System.out.println(PQ.length);
      PQ.Delete(4);
      System.out.println(PQ.length);
      PQ.printQue();
      
      
    }
    
}  


