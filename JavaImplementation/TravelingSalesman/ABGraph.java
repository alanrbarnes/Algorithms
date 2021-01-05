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


public class ABGraph<T> {
  private T V;  //Collection of nodes
  private T E;  //Collection of edges
  private T e; //2 element set of verices defines e = {u, v}
  private T u; //individual node/ vertex that has been processed
  private T v; //individual node/ vertex that has not been processed
  private int adjListSize;  //counts the number of items added to adjacency list
  private Map<String, ABGraphNode> adj;  //adjacency list implementation
                               //Dictionary of nodes stores the location of the node in the array list
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
  private int adjArraySize; //counts the number of items added to the array representation of the adjacency list
  private ABGraphNode[] VertexList; //adjacency list implementation
                   //Array of linked lists
                   //index array by vertices
                   //label vertices from 0 to V-1
                   //each vertex or index of array stores the vertex neighbors
                   //the vertex represented by the index and one neighbor represents and edge e=(u,v)
                   //
  private String[] fileContents;  //array to hold the contents of the file read in.
  public int numOfVertices;
  private boolean weighted;
  private boolean directed;
  private int[] undirectional;
                   
                 
  
  //Constructor for ABGraph class
  public ABGraph() {
    adj = new Hashtable<>();
    VertexList = new ABGraphNode[100];
    numOfVertices = 0;
    //G = (V, E), n = |V|, m = |E|
    weighted = false;
    directed = false;
  }
  
       
  //Method for adding nodes to the graph
  public void addNode(ABGraphNode node) {
      VertexList[numOfVertices] = node;
      adj.put(node.GetNodeName(), node);
      numOfVertices++;
  }
  
  //Method for adding an edge to the graph
  public void addEdge(String from, String to) {
        //Check to see if the node exists
        String[] arr = new String[] {from.toString(), to.toString()};
        
        //Add an edge if array exists
        if(adj.containsKey(from)) {
            //add information to the node
            ABGraphNode node = adj.get(from);
            node.addNodeEdge(arr);
        }
        else {  //This is a new node
            //Create a new node
            ABGraphNode node = new ABGraphNode(arr);
            //Get name of node
            String nodeName = node.GetNodeName().toString();
            //Add new node to array
            VertexList[numOfVertices] = node;
            numOfVertices = numOfVertices + 1;
            //Add new node to adjacency list
            //use node name for key and node for value
            adj.put(nodeName, node);
  
      }
  }
  
  //Method for adding a weighted edge to a graph
  public void addEdge(String from, String to, float cost) {
        //Check to see if the node exists
        String[] arr = new String[] {from.toString(), to.toString(), Float.toString(cost)};
        
        //Add an edge if array exists
        if(adj.containsKey(from)) {
            //add information to the node
            ABGraphNode node = adj.get(from);
            node.addNodeEdge(arr);
        }
        else {  //This is a new node
            //Create a new node
            ABGraphNode node = new ABGraphNode(arr);
            //Get name of node
            String nodeName = node.GetNodeName().toString();
            //Add new node to array
            VertexList[numOfVertices] = node;
            numOfVertices = numOfVertices + 1;
            //Add new node to adjacency list
            //use node name for key and node for value
            adj.put(nodeName, node);
  
      }
  
  }
  
  //Method for adding a vertex to a graph
  public void addVertex(T vertex) {
      boolean result = true;
      //Check to see if the node exists
      if(!adj.containsKey(vertex)) {
          ABGraphNode node = new ABGraphNode(vertex);
          
          //Get name of node
          String nodeName = node.GetNodeName().toString();
          adjArraySize++;
          adjListSize++;
          
          //Add node to adjacency list
          adj.put(nodeName, node);
          numOfVertices = numOfVertices + 1;
      }
      else {
          result = false;
      }
      

  }
  
  //Get method for returning edges in a graph from a specific vertex, Returns adjacency list of vertex
  public String[] getEdges(T vertex) {
      ABGraphNode node = adj.get(vertex);
      String[] edges = node.getAllEdges();
      return edges;

  }
  
  //Method for getting all verticies in a graph
  public ABGraphNode[] getVertices() {
      ABGraphNode[] result = new ABGraphNode[numOfVertices];
      int n = numOfVertices;
      for(int i = 0; i < numOfVertices; i++) {
          result[i] = VertexList[i];
      }
      return result;
  }
  
  //Method for Getting all of the vertice names in a graph
  public String[] getVerticeNames() {
      String[] result = new String[numOfVertices];
      int n = numOfVertices;
      for(int i = 0; i < numOfVertices; i++) {
          
          ABGraphNode node = VertexList[i];
          result[i] = node.GetNodeName();
      }
      return result;
  }
  
  //Method for getting the number of vertices in a graph
  public int getNumOfVertices() {
      return numOfVertices;
  }
  
  //Method for getting a specific vertice from the vertice name
  public ABGraphNode getVertice(String VertexName) {
      return adj.get(VertexName);
  }
  
  //Method for getting the adjacent nodes in a graph, returns ABGraphNode
  public ABGraphNode[] getAdjacentNodes(ABGraphNode node) {
      String[] result = node.getAllEdges();
      ABGraphNode[] nodes = new ABGraphNode[result.length];
      for(int i = 0; i < result.length; i++) {
          nodes[i] = getVertice(result[i]);
      }
      return nodes;
  }
  
  ////////////////////////////
  //Utility functions
  ////////////////////////////
  //Method for printing out vertices
  public void printAllVertices() {
      /*System.out.println("Array length = " + VertexList.length);
      System.out.println("Length of vertices = " + numOfVertices);
      System.out.println("Array count = " + adjArraySize);
      System.out.println("List count = " + adjListSize);
      System.out.println("Hashtable size = " + adj.size()); */
      System.out.println(GraphType);
      int i = 0;
      int e = 0;
      while(i < numOfVertices) {
          if (VertexList[e] == null) {
              System.out.println("Null Value");
              e = e + 1;
          }
          else {
              System.out.println(list(VertexList[e]));
              i = i + 1;
              e = e + 1;
          }
      
      }
      
  }
      //Method for listing all vertices, utility for printing vertices
      public String list(ABGraphNode node) {

      //node = adjList.get(node);
      String[] edges = node.getAllEdges();
      String result = node.GetNodeName() + ": [(";
      int i = 0;
      result = result + edges[i];
       if(graphType == 1 || graphType == 3) {
           result = result + ", " + node.getNodeWeight(edges[i]);
       }
      i++;
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
      //temporary array to store the order of the nodes
      String[] temp = new String[100];
      int tempCount = 0;
      
      //read in list of nodes to array
      //ABGraphReadFile ab = new ABGraphReadFile();
      String fileString = readFile();
      
      fileContents = fileString.split("\\r?\\n");
      //System.out.println("Initial array size: " + fileContents.length);  
      
      //Initialize graphType
      int gtype = getGraphType();
  
      //Create Graph nodes from array
      for(int i = 0; i < fileContents.length - 1; i++) {
        //convert string to array
        //first index of array is type of graph, start at index 1
        String str = fileContents[i + 1];  
        String[] arr = str.split("=");
        
        //load a temporary array with the order of the nodes in the order they are read in the file
        if (tempCount == 0) {
            temp[tempCount] = arr[0];
            tempCount++;
        }
        else if(!temp[tempCount - 1].equals(arr[0])) {
            temp[tempCount] = arr[0];
            tempCount++;
        }
        
        //Check to see if the node exists
        if(adj.containsKey(arr[0])) {
            //add information to the node
            ABGraphNode node = adj.get(arr[0]);
            node.addNodeEdge(arr);
        }
        else {  //This is a new node
            //Create a new node
            ABGraphNode node = new ABGraphNode(arr);
            adjArraySize++;
            adjListSize++;
            //Get name of node
            String nodeName = node.GetNodeName().toString();
            
            //Add new node to array
            VertexList[numOfVertices] = node;
            numOfVertices = numOfVertices + 1;
                        
            //Add new node to adjacency list
            //use node name for key and node for value
            adj.put(nodeName, node);
            
        }
        
        //Check all of the nodes to see if they need to have directions added
        if (graphType == 3) {           
            //add edge to graph for Weighted undirected graph
            addEdge(arr[1], arr[0], Float.parseFloat(arr[2]));
        }
        else if(graphType == 4) {
            //add edge to graph for unweighted undirected graph
            addEdge(arr[1], arr[0]);
        }
            //graphType;  //1: Weighted directed
                          //2: Unweighted directed
                          //3: Weighted undirected
                          //4: Unweighted undirected 
       }
        
//         int i = 0;
//         while (temp[i] != null) {
//         adj.put(VertexList[i]);
//         i++;
        // }


    }
    
    //Method for printing out an array for testing purposes
    public void printarray(String[] arr) {
      for (int i = 0; i < arr.length; i++) {
      System.out.print(arr[i] + ", ");
      }
      System.out.println("");
    }
  
    //Method for getting the graph Type
    public int getGraphType() {
      graphType = 0;
      if (fileContents[0] != null) {
        String result = fileContents[0].toLowerCase();  //1: Weighted directed
                          //2: Unweighted directed
                          //3: Weighted undirected
                          //4: Unweighted undirected
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
        FileNameExtensionFilter glfilter = new FileNameExtensionFilter("Graph Document", "gl");
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
public class ABGraphNode<T> {
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
  T parent;  //parent of this node
  float cost;  //cost of attaching this node to the parent
  
  private T datavalue2;
  
  //Constructor for Node Class, input string array
  public ABGraphNode(String[] arr) {
      //Copy array to adjacency list
      //First element is the name of the node
      //second element is the element
      adj = new String[20];
      edgeWeight = new Hashtable<String, String>();    
      adj[0] = arr[1].toString(); //Add adjacent node to adjacency list
      degree = 1;
      nodeName = arr[0];
      addInfo(arr);
  }
  
  //Constructor for class, Input individual node
  public ABGraphNode(T newNode) {
      adj = new String[20];
      edgeWeight = new Hashtable<>();
      degree = 0;
      nodeName = newNode.toString();
  }
  
  //Method for adding info to a node
  private void addInfo(String[] arr) {
      if(graphType == 1)
      {edgeWeight.put(arr[1], arr[2]);}
      else if(graphType == 2) {}
      else if (graphType == 3) {
          //Store edgeweight for adjacent node
          edgeWeight.put(arr[1], arr[2]);
          directional = 1;
      }
      else if (graphType == 4)
      {directional = 1;}
  }
  
  
  //Method for getting the name of a node
  public String GetNodeName() {
      return nodeName;
  }
  
  //Method for Getting the adjacency list in the node
  public String[] getAllEdges() {
      String[] value = new String[degree];
      for(int i = 0; i < degree; i++) {
          value[i] = adj[i];
      }
      return value;
  }
  
  //Method for Getting the Weight of an adjacent node, input nodeName, output node edge weight as string
  public String getNodeWeight(String key) {
      String w = edgeWeight.get(key);
      return w;
  }
  
  //Method for adding an edge to a node, adds a connected vertex to a graph
  public void addNodeEdge(String[] arr) {
      adj[degree] = arr[1].toString(); //Add adjacent node to adjacency list
      degree = degree + 1;
      addInfo(arr);
  }
  
  //Method for getting the number of adjacent nodes to the vertex
   public int getDegree() {
       return degree;
   }
   
   //Method for setting the parent of the vertex
   public void setParent(T value) {
       parent = value;
   }
   
   //Method for getting the parent of the vertex
   public T getParent() {
       return parent;
   }
   
   //Method for setting the cost of the vertex
   public void setCost(float value) {
       cost = value;
   }
   
   //Method for getting the cost of the vertex
   public float getCost() {
       return cost;
   }
  

}

}

