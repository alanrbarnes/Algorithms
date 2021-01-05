//Written By: Alan Barnes
//Written on: September 29, 2020
//Written for: CSC 482, Assignment 2

//read file in for data
//pass file contents to constructor
//separate file contents into array
//array at index 0 is for graph type
//other indexes of array are for vertice description


import java.lang.*;
import java.util.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Hashtable;

import java.io.*;
import java.io.File;
import java.nio.file.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ABGraph<T extends Comparable<T>> {
  private T V;  //Collection of Edges
  private T E;  //Collection of nodes
  private T e; //2 element set of verices defines e = {u, v}
  private T u; //individual node/ vertex that has been processed
  private T v; //individual node/ vertex that has not been processed
  private Map<String, ABGraphNode> adjList;  //adjacency list implementation
                               //Dictionary of nodes
                               //name of node is key in key value pair
                               //node is value in key Value pair
                               //label vertices from 0 to V-1
                               //each vertex or index of array stores the vertex neighbors
                               //the vertex represented by the index and one neighbor represents and edge e=(u,v)
  private T currentNode;  //used for tracking the 
  private T rootNode;     //used for starting point in tree
  
  private int graphType;  //1: Weighted directed
                          //2: Unweighted directed
                          //3: Weighted undirected
                          //4: Unweighted undirected
  private String GraphType;
  private ABGraphNode[] adj; //adjacency list implementation
                   //Array of linked lists
                   //index array by vertices
                   //label vertices from 0 to V-1
                   //each vertex or index of array stores the vertex neighbors
                   //the vertex represented by the index and one neighbor represents and edge e=(u,v)
                   //
  private String[] fileContents;  //array to hold the contents of the file read in.
  private int numOfVertices;
  private boolean weighted;
  private boolean directed;
  private int[] undirectional;
                   
                 
  
  //sum of the degrees in the graph p88
  //sum of the edges of all the nodes
  //set of all edges adjacent to a node in the graph
  //the set of visited nodes in the BFS and DFS
  //
  
  //Constructor for ABGraph class
  public ABGraph() {
    adjList = new Hashtable<>();
    numOfVertices = 0;
    //G = (V, E), n = |V|, m = |E|
    weighted = false;
    directed = false;
    
  }
  
  //2. constructor that takes the number of nodes and the number of edges
//   public ABGraph(T nodes, T edges, String GraphType) {
//     adjList = new Hashtable<>();
//     numOfVertices = 0;
//     //G = (V, E), n = |V|, m = |E|
//     weighted = false;
//     directed = false;
    
//  }
  

  //GraphType is set from the file read in
  //3. constructor for undirected Graph
  
  //4. constructor for directed Graph
  
  //5. constructor for weighted Graph
  
       
  
  public void addEdge(String from, String to) {
        //Check to see if the node exists
        String[] arr = new String[] {from.toString(), to.toString()};
        
        //Add an edge if array exists
        if(adjList.containsKey(from)) {
            //add information to the node
            //System.out.println("This node exists");
            ABGraphNode node = adjList.get(from);
            node.addNodeEdge(arr);
        }
        else {  //This is a new node
            //Create a new node
            ABGraphNode node = new ABGraphNode(arr);
            //Get name of node
            String nodeName = node.GetNodeName().toString();
            //System.out.println(nodeName);
            //Add new node to array
            adj[numOfVertices + 1] = node;
            numOfVertices = numOfVertices + 1;
            //Add new node to adjacency list
            //use node name for key and node for value
            adjList.put(nodeName, node);
  
      }
  }
  
  
  public void addEdge(String from, String to, int cost) {
        //Check to see if the node exists
        String[] arr = new String[] {from.toString(), to.toString(), Integer.toString(cost)};
        
        //Add an edge if array exists
        if(adjList.containsKey(from)) {
            //add information to the node
            //System.out.println("This node exists");
            ABGraphNode node = adjList.get(from);
            node.addNodeEdge(arr);
        }
        else {  //This is a new node
            //Create a new node
            ABGraphNode node = new ABGraphNode(arr);
            //Get name of node
            String nodeName = node.GetNodeName().toString();
            //System.out.println(nodeName);
            //Add new node to array
            adj[numOfVertices + 1] = node;
            numOfVertices = numOfVertices + 1;
            //Add new node to adjacency list
            //use node name for key and node for value
            adjList.put(nodeName, node);
  
      }
  
  }
  
  public void addVertex(T vertex) {
      ABGraphNode node = new ABGraphNode(vertex);
      //addNodeVertex(vertex);
  }
  
  public String[] getEdges(T vertex) {
      ABGraphNode node = adjList.get(vertex);
      String[] edges = node.getAllEdges();
      return edges;

  }
  
  public String[] getVertices() {
      String[] result = new String[adj.length];
      for(int i = 0; i < adj.length; i++) {
          ABGraphNode node = adj[i];
          result[i] = node.GetNodeName();
      }
      return result;
  }
  
  ////////////////////////////
  //Utility functions
  ////////////////////////////

  public void printAllVertices() {
      System.out.println(GraphType);
      int i = 1;
      int e = 1;
      while(i <= numOfVertices) {
          if (adj[e] == null) {
              e = e + 1;
          }
          else {
              System.out.println(list(adj[e]));
              i = i + 1;
              e = e + 1;
          }
      
      }
      
  }
  
      public String list(ABGraphNode node) {

      //node = adjList.get(node);
      String[] edges = node.getAllEdges();
      String result = node.GetNodeName() + ": [(";
      //System.out.println(node.GetNodeName());
      int i = 0;
      result = result + edges[i];
       if(graphType == 1 || graphType == 3) {
           result = result + ", " + node.getNodeWeight(edges[i]);
       }
      
      //itterate thru Undirected Unweighted
      for(; i < edges.length; i++) {
          if(edges[i] != null) {
              result = result + "), (";
              result = result + edges[i];
              
              if(graphType == 1 || graphType == 3) {
                  result = result + ", " + node.getNodeWeight(edges[i]);
              }
              
          }
      }
      result = result + ")]";
      return result;
  }
 
  
  
    //Method to initialize the graph from a file   
    public void makeGraphFromFile() {
      //read in list of nodes to array
      ABGraphReadFile ab = new ABGraphReadFile();
      String fileString = ab.readFile();
      
      fileContents = fileString.split("\\r?\\n");
      //System.out.println(fileString);
      //Initialize graphType
      int gtype = getGraphType();
      //System.out.println(gtype);
  

      adj = new ABGraphNode[fileContents.length + 1];
      //Create Graph nodes from array
      for(int i = 1; i < fileContents.length; i++) {
        //convert string to array
        String str = fileContents[i];
        String[] arr = str.split("=");
        //System.out.println(arr.length);
        
        //Check to see if the node exists
        if(adjList.containsKey(arr[0])) {
            //add information to the node
            //System.out.println("This node exists" + adjList.get(arr[0]).GetNodeName());
            ABGraphNode node = adjList.get(arr[0]);
            node.addNodeEdge(arr);
        }
        else {  //This is a new node

            //Create a new node
            ABGraphNode node = new ABGraphNode(arr);
            //Get name of node
            String nodeName = node.GetNodeName().toString();
            //System.out.println(nodeName);
            //Add new node to array
            adj[i] = node;
            //Add new node to adjacency list
            //use node name for key and node for value
            adjList.put(nodeName, node);
            numOfVertices = numOfVertices + 1;
        }
        
        
//         if (graphType == 3) {
//             //addEdge(arr[1], arr[0], Integer.parseInt(arr[2]));
//         }
//         
//         if (graphType == 4) {
//             //addEdge(arr[1], arr[0]);
//         }
       }
       
       if (graphType == 3 || graphType == 4) {
           makeDirectional();
       }

    }
    
    public void makeDirectional() {
          int i = 1;
          int e = 1;
              while(i <= numOfVertices) {
          if (adj[e] == null) {
              e = e + 1;
          }
          else {
              //add reverse direction
              i = i + 1;
              e = e + 1;
          }
      
      }
    }
  
    
    public int getGraphType() {
      graphType = 0;
      if (fileContents[0] != null) {
        String result = fileContents[0].toLowerCase();  //1: Weighted directed
                          //2: Unweighted directed
                          //3: Weighted undirected
                          //4: Unweighted undirected
        //System.out.println(fileContents[0]);
        if(result.contains("undirected")) 
        {directed = false;}
        else
        {directed = true;}
        
        if(result.contains("unweighted"))
        {weighted = false;}
        else
        {weighted = true;}
        
        if(weighted && directed)
        {
            graphType = 1;
            GraphType = "Directed Weighted";
        }
        else if(!weighted && directed)
        {
            graphType = 2;
            GraphType = "Directed Unweighted";
        }
        else if(weighted && !directed)
        {
            graphType = 3;
            GraphType = "Undirected Weighted";
        }
        else if(!weighted && !directed)
        {
            graphType = 4;
            GraphType = "Undirected Unweighted";
        }
      }
      return graphType;
  }
 
  
  
    /////////////////////////////
    //method for opening a file//
    /////////////////////////////
    public String readFile() {
	     String path = openSaveDialog(1);
		  Path file = Paths.get(path);
		  String s = "";
        String outputString = "";
		  try
		  {
		      InputStream input = new BufferedInputStream(Files.newInputStream(file));
		      BufferedReader reader  = new BufferedReader(new InputStreamReader(input));
			   s = reader.readLine();
				while(s != null)
			   {
				    outputString = outputString + s + "\n";
					 s = reader.readLine();
			   }
				reader.close();
					
        }
		  catch(Exception e)
		  {
		      JOptionPane.showMessageDialog(null, e);
		  }
        return outputString;
    }
    
    ///////////////////////////////
	 //Open and save dialog method//
	 ///////////////////////////////
	 public static String openSaveDialog(int dialogFunc) 
	 {
	     JFileChooser chooser = new JFileChooser();
		  FileNameExtensionFilter txtfilter = new FileNameExtensionFilter("Text Document", "txt");
        FileNameExtensionFilter glfilter = new FileNameExtensionFilter("Text Document", "gl");
        chooser.addChoosableFileFilter(txtfilter);
        chooser.addChoosableFileFilter(glfilter);
        chooser.setFileFilter(glfilter);
	    
		  if(dialogFunc == 0)
		  {chooser.showSaveDialog(null);}
		  else if(dialogFunc == 1)
		  {chooser.showOpenDialog(null);}
		
		  File f = chooser.getSelectedFile();
		  String ff = chooser.getSelectedFile().toString();
		  String ex = ff.substring(ff.length() - 4);
	     ex = ex.substring(0,1);
        String extention = chooser.getFileFilter().toString();
		  //JOptionPane.showMessageDialog(null, ex);
        
        if(extention.equalsIgnoreCase("txt") && dialogFunc == 0 && !ex.equals("."))
		  {f = new File(chooser.getSelectedFile() + ".txt");}
        else if(extention.equalsIgnoreCase("gl") && dialogFunc == 0 && !ex.equals("."))
		  {f = new File(chooser.getSelectedFile() + ".gl");}

		  String fileName = f.getAbsolutePath();
		  return fileName;
	}
   
   
/////////////////////////////////////////////////
//node class for creating a graph node///////////
/////////////////////////////////////////////////   
public class ABGraphNode<T extends Comparable<T>> {
  //adjacency list, array that holds list of nodes that are adjacent to the node
  private String[] adj;
  public int directional;
  private int numOfAdj;
  private Map<String, String> edgeWeight;
  //private T[] weight;
  private String nodeName;
            //graphType;  //1: Weighted directed
                          //2: Unweighted directed
                          //3: Weighted undirected
                          //4: Unweighted undirected
  
  //degree n sub v number of edges that the array has
  private int degree;
  
  private T datavalue2;
  
  public ABGraphNode(String[] arr) {
      //Copy array to adjacency list
      //First element is the name of the node
      //second element is the element
      adj = new String[20];
      edgeWeight = new Hashtable<String, String>();
      // = Arrays.copyOfRange(arr, 1, 1);
      
      adj[0] = arr[1].toString(); //Add adjacent node to adjacency list
      ////edgeWeight.put(nodeName, adj[o]);
      //System.out.println(arr[1]);
      degree = 1;
      nodeName = arr[0];
      addInfo(arr);
      
  }
  
  public ABGraphNode(T newNode) {
      adj = new String[20];
      edgeWeight = new Hashtable<>();
      degree = 0;
      nodeName = newNode.toString();
  }
  
  private void addInfo(String[] arr) {
      if(graphType == 1) {

          edgeWeight.put(arr[1], arr[2]);
          //System.out.println("Weighted directed" + adj[1] + ", " + adj[2]); //+ ", " + adj[3]);
      }
      else if(graphType == 2) {
          //System.out.println("Unweighted directed" + adj[1]);
      }
      else if (graphType == 3) {
          //System.out.println("Weighted undirected" + adj[1] + ", " + adj[2]);
          edgeWeight.put(arr[1], arr[2]);
          directional = 1;
      }
      else if (graphType == 4) {
          //System.out.println("Unweighted undirected" + adj[1]);
          directional = 1;
      }
  }
  
  
  public String GetNodeName() {
      return nodeName;
  }
  
  public String[] getAllEdges() {
      return adj;
  }
  
  public String getNodeWeight(String key) {
      return edgeWeight.get(key);
  }
  
  public void addNodeEdge(String[] arr) {
      //System.out.println("Here:  " + arr.length);
      //System.out.println(degree + ", " + adj.length);
      adj[degree] = arr[1].toString(); //Add adjacent node to adjacency list
      degree = degree + 1;
      addInfo(arr);
  }
  
  /*
  public T changeWeight(T nodeName, T edgeWeight) {
      edgeWeight.replace(nodeName, edgeWeight);
  }
  */
   public int getDegree() {
       return degree;
   }
  

}

}

