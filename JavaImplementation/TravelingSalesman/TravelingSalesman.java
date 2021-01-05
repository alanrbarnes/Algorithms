//Written By: Alan Barnes
//Written on: November 27, 2020
//Written for: CSC 482, Assignment 5

import java.util.*;
import java.io.*;
import java.lang.*;

import java.util.Scanner;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.HashSet; 

import java.io.File;
import java.nio.file.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.ByteBuffer;
import javax.swing.plaf.FileChooserUI;
import static java.nio.file.StandardOpenOption.*;



public class TravelingSalesman {
    private ABGraph.ABGraphNode[] minPath;
    private float minCost;
    private int vertexCount;
    private ABGraph G;
    public int numvisited;
    


    //Constructor for class
    public TravelingSalesman() {
    
    }
    
    //Method to initialize environment and call recursive function
    public void runTSP() {
        G = new ABGraph();
        minCost = 999999.0f;
        vertexCount = 0;
        numvisited = 0;
        
        //initialize graph from file
        loadGraphFile(G);
        printGraph(G);
        //minPath is 1 node longer than number of vertices because it ends on the first node, first node occurs 2X
        minPath = new ABGraph.ABGraphNode[G.numOfVertices + 1];
        vertexCount = G.numOfVertices;
        
        //Get first vertice
        ABGraph.ABGraphNode[] vertices = G.getVertices();
        HashSet<ABGraph.ABGraphNode> visited = new HashSet<ABGraph.ABGraphNode>();
        //path is 1 node longer than number of vertices because it ends on the first node, first node occurs 2X
        ABGraph.ABGraphNode[] path = new ABGraph.ABGraphNode[G.numOfVertices + 1];
        path[0] = vertices[0];
        numvisited++;
        //record in map
        visited.add(vertices[0]);
        float cost = 0.0f;
        //Add all vertices to visited and set to false
        TSP(G, vertices[0], path, cost, visited);
        printTSP();
    }
    
    //Method for printing out the solution to the Traveling Salesman Problem
    public void printTSP() {
        System.out.print("Minimum Path = " + minPath[0].GetNodeName());
        for(int i = 1; i < minPath.length; i++) {
            System.out.print(", " + minPath[i].GetNodeName());
        }
        System.out.println();
        System.out.println("Minimum Cost = " + minCost);
    }
    
            
    public void TSP(ABGraph graph, ABGraph.ABGraphNode vertice, ABGraph.ABGraphNode[] path, float cost, HashSet<ABGraph.ABGraphNode> visited) {
        
        //Base Case, If we have a path that contains every vertice exactly once
        if(numvisited >= vertexCount) {
            //attach vertice edge to start node and update cost
            float costToGoHome = Float.parseFloat(vertice.getNodeWeight(path[0].GetNodeName()));
            cost = cost + costToGoHome; //Cost to get to start node
            
            //add edge to path
            path[numvisited] = path[0]; //add start node to path
            
            //if cost < minimum cost, check to see if this is a better path
            if(cost < minCost) {
                //set minimum cost to path
                minCost = cost;
                
                //set minimum path to path
                for(int i = 0; i < path.length; i++) {
                    minPath[i] = path[i];
                }

                //remove start node from path
                path[numvisited] = null;
                //remove the cost to go home
                cost = cost - costToGoHome; //Cost to get to start node

                
            }//end if
            else {
                //remove start node from path
                path[numvisited] = null;
                //remove the cost to go home
                cost = cost - costToGoHome; //Cost to get to start node
            }
        }//end if
        
        //else
        else {
            String[] edges = vertice.getAllEdges();
            //for each edge that vertice has
            for(int i = 0; i < edges.length; i++) {
                ABGraph.ABGraphNode v = graph.getVertice(edges[i]);
                //if we haven't visited node at other end of edge
                if(!visited.contains(v)) {
                    //add edge to path
                    path[numvisited] = v;
                    
                    //record in map
                    visited.add(v);
                    
                    //update the size of the path
                    numvisited++;
                    float weight = Float.parseFloat(vertice.getNodeWeight(edges[i]));
                    cost = cost + weight;
            
                    //recursively call TSP(graph, end node, path, cost + weight, visited)
                    TSP(graph, v, path, cost, visited);
                    
                    //update the size of the path
                    numvisited--;
                    cost = cost - weight;
                    
                    //remove edge from path (to try other edges attached to vertice
                    path[numvisited] = null;
                    
                    //remove from map
                    visited.remove(v);
                    
                }//end if
            }//end for
        }//end else              
    }//end TSP



    public void loadGraphFile(ABGraph graph) {
        //load the input graph from a file
        graph.makeGraphFromFile();
        vertexCount = graph.getNumOfVertices();
    }
    
    public void printGraph(ABGraph graph) {
        graph.printAllVertices();
    }


    /////////////////////////////
    //method for writing a file//
    /////////////////////////////
			public void writeFile(String s) 	
			{
				String path = openSaveDialog(0);
				Path file = Paths.get(path);
				try
				{
					OutputStream output = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
					writer.write(s);
					writer.close();	
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, e);
				}
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

}