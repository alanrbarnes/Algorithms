//Written By: Alan Barnes
//Written on: September 29, 2020
//Written for: CSC 482, Assignment 2

import java.util.*;
import java.lang.*;
import java.io.*;


public class ABGraphController {

  public static void main(String[] args) {
    ABGraph g = new ABGraph();
    g.makeGraphFromFile();
    //String[] s = g.getVertices();
    g.printAllVertices();
    
  }

  
}