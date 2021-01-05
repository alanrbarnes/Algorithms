//Written By: Alan Barnes
//Written on: September 29, 2020
//Written for: CSC 482, Assignment 2

public class ABGraphNode<T extends Comparable<T>> {
  //adjacency list, array that holds list of nodes that are adjacent to the node
  private T[] adj;
  private T[] weight;
  
  //degree n sub v number of edges that the array has
  private int degree;
  
  private T datavalue2;
  
  public ABGraphNode(T[] value1, T value2) {
      adj = value1;
      degree = t.length();
      datavalue2 = value2;
  }
  
//   public int getDegree() {
//       //calculate number of degrees for the vertex
//       for(int i = 0; i < adj.length(); i++) {
//       
//       }
  }
  
  public string list() {
      String result = "Label" + ": [";
      int i = 0;
      result = result + adj[i];
      for(; i < adj.length(); i++) {
          result = result + ",";
          result = result + adj[i];
      }
      String result = "]";
      return result;
  }
}

