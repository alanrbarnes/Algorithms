//Written By: Alan Barnes
//Written on: October 14, 2020
//Written for: CSC 482, Assignment 3
import java.util.*;
import java.io.*;
import java.lang.*;

import java.util.Scanner;
import java.util.Arrays;
import java.util.Hashtable;

import java.io.File;
import java.nio.file.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.ByteBuffer;
import javax.swing.plaf.FileChooserUI;


import static java.nio.file.StandardOpenOption.*;




public class primsAlgorithm<T extends Comparable<T>> {
//   private T V;  //Collection of nodes
//   private T E;  //Collection of edges
//   private T e; //2 element set of verices defines e = {u, v}
//   private T u; //individual node/ vertex that has been processed
//   private T v; //individual node/ vertex that has not been processed

//   int n //number of nodes in a graph
//   boolean visited  //array that tracks if a node has been visited
                      //implemented in the graph class
//   int m  //m = n-1 number of edges in the graph


    
    //Create one graph for the input data
    ABGraph inputGraph;
    //Create one graph for the MST
    ABGraph mst;
    //Heap Priority Que for V, Initially contains all the nodes in the graph 
    HeapPriorityQueue V;
    //Heap Priority Que for S, Contains all the nodes in the MST
    HeapPriorityQueue S;
    int vCount; //number of elements in initial que
    int sCount; //number of elements in new que
    int maxEdges; //max number of edges in the mst, e = n - 1
            //root node for graph
    ABGraph.ABGraphNode root;
    //hashtable for finding out if the node has been visited
    Map<ABGraph.ABGraphNode, Boolean> visited;
    
    public primsAlgorithm() {
        clear();
    }
    
    public void clear() {
        //Create one graph for the input data
        inputGraph = new ABGraph();
        //Create one graph for the MST
        mst = new ABGraph();
        //Create the heap priority que that holds all values for V, initially all the nodes
        V = new HeapPriorityQueue();
        //create the heap priority que that holds the MST
        S = new HeapPriorityQueue();
        //initially set the max number of edges to 0
        maxEdges = 0;
        visited = new Hashtable<>();
    }
    
    public ABGraph runPrimsAlgorithm() {  
        //load the input graph from a file
        loadFile();
        //get root node
        //root is the node marked as R  ********************
        root = inputGraph.getVertice("r");
        //add root to S
        //S.Insert(root, 99999);
        
        //1. Set attachment cost of designated root node to 0
        //S.ChangeKey(root, 0);
        
        //2.PriorityQueue contains all of the nodes, including the root, 
        //  where each non-root node has an attachment cost of +- infinity.        
        //  (or an alternative, very large positive number).
        
        //Add all nodes to V, the inital heap containing all the nodes 
        //set the attachment cost for all nodes in V to inifinity or 9999 
        fillVFromGraph(inputGraph, 99999);
        V.ChangeKey(root, 0);
        V.printPriority(root);

        //3.MST is empty
        //  MST initialized in the constructor
        
  //       Map<// String, String> test2 = new Hashtable<>();
//         test.put("Alan", "Barnes");
//         test2.put("Alan", "Berns");
//         System.out.println(test.get("Alan"));
//         System.out.println(test2.get("Alan"));



        //get the node from the priority list with the lowest priority
        //1st one will be the root
        //add the node to the mst, the mst will represent the path taken to traverse the graph
        //remove the node from the priority que representing the graph
        //get the adj list from the node and add it to a list as edges for reference
        //as edges are used, remove it from the list, in the graph class?
        //add node to the visited hashmap
        //
        //when computations are complete change the priority in the priority que to new actual value so
        // it is picked up in the next iteration... all other nodes will have a priority of 99999
        //this is the reason for the use of infinity in attachment costs
        //actual attachment calculations are made from the graph class to the prims algorithm class.
        int order = 1;
        ABGraph.ABGraphNode cParent = null;
        float cCost = 0;
        vCount = V.length;
        sCount = S.length;
        maxEdges = vCount - 1;  //max number of edges in the mst, e = n - 1
        
        
        
        //process the root node
            //Extract MinNode with lowest attachment cost (ExtractMin)
            ABGraph.ABGraphNode currentNode = (ABGraph.ABGraphNode)V.ExtractMin();

            //Call method for processing node
            //processNode(currentNode, order);
            
            
            //Check to see if node is in list allready
 //           if(!visited.containsKey(currentNode)) {
                //Add MinNode to S if not allready included
                S.Insert(currentNode, order);
                //Mark node as visited
                visited.put(currentNode, true);
                
                //V.Delete(currentNode);
                //add cost and parent to queue if it is not the root node
                S.AddCostAndParent(currentNode, cParent, 0f);
  //          }
                cParent = currentNode;
        
        
        //While PriorityQueue is not Empty, and avoid infinite loop
        while(V.length > 0 && order < 100) {

            //get list of adjacent nodes
            ABGraph.ABGraphNode[] adj = inputGraph.getAdjacentNodes(currentNode);
            //get coresponding list of costs to these nodes,  adj[i].getNodeWeight(adj[i].GetNodeName())
            float[] weights = new float[adj.length];
            
            //For each node adjacent to MinNode:
            //loop thru each adjacent node to update the attachment cost and mark as visited
            for(int i = 0; i < adj.length; i++) {
                //Compute current attachment cost from MinNode to each adjacent node
                String nodeName = adj[i].GetNodeName();
                String weight = currentNode.getNodeWeight(nodeName);
                weights[i] = Float.parseFloat(weight);
                
                //if the node is visited put the node into que S with the best cost
                //if(!visited.containsKey(adj[i])) { //check to see if the node is still in V
                    //change the value of the key in V
                //}
                //else {
                //if computed cost is less than the adjacent node's current attachment cost:
                boolean dd = visited.containsKey(adj[i]);
                
                HeapPriorityQueue.heapNode nd;
                float vPriority;
                if((!S.contains(adj[i]))) {
                    nd = V.getNode(adj[i]);
                    vPriority = nd.getPriority();                
                }
                else {
                    nd = S.getNode(adj[i]);
                    vPriority = nd.getPriority();   
                }
                
                //Check to see if the node is still in V and if the weight is better
                if((!S.contains(adj[i])) && (weights[i] < vPriority)) {  
                    //Update attachment cost of adjacent node (Change Key)
                    V.ChangeKey(nd.getValue(), weights[i]);
                    //add cost and parent to queue if it is not the root node
                    V.AddCostAndParent(currentNode, currentNode, weights[i]);
                    adj[i].setParent(currentNode);
                    adj[i].setCost(weights[i]);
                } //end if
                
                 visited.put(adj[i], true);
 
            } //end for
            

                
                
                //get next node  ********************
                currentNode = (ABGraph.ABGraphNode)V.ExtractMin();
                //Add MinNode to S if not allready included
                S.Insert(currentNode, order);
                //Mark node as visited
                visited.put(currentNode, true);
                //add cost and parent to queue if it is not the root node
                S.AddCostAndParent(currentNode, cParent, currentNode.getCost());
            
                cParent = currentNode;
                order++;
                vCount = V.length;
                sCount = S.length;
            } //end while

        
        //fill mst graph with nodes from priority queue  ****************  
        int i = 0;
        //String[] result = new String[inputGraph.numOfVertices + 1];
        String result = "";
        result = "undirected weighted\n";
        i++;
        
        HeapPriorityQueue.heapNode h = S.getNode(S.peek());
            //ABGraph.ABGraphNode nodeI = inputGraph.getVertice(S.peek().GetNodeName());
            float cost = h.getCost();
            ABGraph.ABGraphNode parent = (ABGraph.ABGraphNode)h.getParent();
            ABGraph.ABGraphNode node = (ABGraph.ABGraphNode)S.ExtractMin();
            mst.addNode(node);
            //Add info to string to print out to file
            //result[i] = node.GetNodeName() + "=" + parent + "=" + cost;
            result = result + node.GetNodeName() + "=" + parent + "=" + cost + "\n";
            //System.out.println(result[i]);
            i++;
            
        
        while(S.length > 0 && order < 100) {
            h = S.getNode(S.peek());
            //ABGraph.ABGraphNode nodeI = inputGraph.getVertice(S.peek().GetNodeName());
            cost = h.getCost();
            parent = (ABGraph.ABGraphNode)h.getParent();
            node = (ABGraph.ABGraphNode)S.ExtractMin();
            mst.addNode(node);
            //Add info to string to print out to file
            //result[i] = node.GetNodeName() + "=" + parent.GetNodeName() + "=" + cost;
            result = result + node.GetNodeName() + "=" + parent.GetNodeName() + "=" + cost + "\n";
            //System.out.println(result[i]);
            i++;
        }
        System.out.println(result);
        //save the graph to a file       *******************
        writeFile(result);
        //return MST
        return mst;
    }

    public void loadFile() {
        //load the input graph from a file
        inputGraph.makeGraphFromFile();
        maxEdges = inputGraph.getNumOfVertices();
    }
    
    
    //load nodes from a graph to a priority queue
    public void fillVFromGraph(ABGraph g, int cost) {

        //get the number of nodes in the input graph
        int countV = g.getNumOfVertices();   
        //get all of the vertices in an array
        ABGraph.ABGraphNode[] graphVertices = g.getVertices();
        
        //add all of the nodes in the graph to the priority queue
        //set all priority values to infinity (99999)Insert
        for(int i = 0; i < countV; i++)
        {V.Insert(graphVertices[i], 99999);}
    }
    
    public void printInitialGraph() {
        inputGraph.printAllVertices();
    }
    
    public void printMSTGraph() {
        mst.printAllVertices();
    }
    
    
        ///////////////////////////////////////////////
    ////////method for writing a file
    ///////////////////////////////////////////////
    

			public void writeFile(String s) 	
			{
				String path = openSaveDialog("txt", 0);
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
//				JOptionPane.showMessageDialog(null,  s);
			}
    
    ///////////////////////////////
	 //Open and save dialog method//
	 ///////////////////////////////
	 public static String openSaveDialog(String extention, int dialogFunc) 
	 {
	     JFileChooser chooser = new JFileChooser();
		  FileNameExtensionFilter txtfilter = new FileNameExtensionFilter("Text Document", "txt");
        FileNameExtensionFilter glfilter = new FileNameExtensionFilter("Text Document", "gl");
		
		  if(extention.equalsIgnoreCase("txt"))
		  {
		      chooser.addChoosableFileFilter(txtfilter);
			   chooser.setFileFilter(txtfilter);
		  }
        else if(extention.equalsIgnoreCase("gl"))
		  {
		      chooser.addChoosableFileFilter(glfilter);
			   chooser.setFileFilter(glfilter);
		  }
	    
		  if(dialogFunc == 0)
		  {chooser.showSaveDialog(null);}
		  else if(dialogFunc == 1)
		  {chooser.showOpenDialog(null);}
		
		  File f = chooser.getSelectedFile();
		  String ff = chooser.getSelectedFile().toString();
		  String ex = ff.substring(ff.length() - 4);
	     ex = ex.substring(0,1);
		  //JOptionPane.showMessageDialog(null, ex);
        
		  if(extention.equalsIgnoreCase("txt") && dialogFunc == 0 && !ex.equals("."))
		  {f = new File(chooser.getSelectedFile() + ".txt");}
        else if(extention.equalsIgnoreCase("gl") && dialogFunc == 0 && !ex.equals("."))
		  {f = new File(chooser.getSelectedFile() + ".gl");}
		
		  String fileName = f.getAbsolutePath();
		  return fileName;
	}



}