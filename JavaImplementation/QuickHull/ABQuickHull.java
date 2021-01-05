//Written By: Alan Barnes
//Written on: October 30, 2020
//Written for: CSC 482, Assignment 4

import java.util.*;
import java.lang.*;
import java.lang.Math.*;
import java.io.*;

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



public class ABQuickHull {
    //array for holding points
    private Points[] coordinates;
    //value to store the number of points read
    private int numOfPoints;
    private int numOfConvexPoints;
    //Array to hold the finished array of points for the convex hull solution
    private Points[] convex_hull;
    //array to hold the contents of the file read in.
    private String[] fileContents;  
    private Map<Points, Integer> added;
    
    
    
    public ABQuickHull() {
        //read in file
        String file = readFile();
        //clear data in memory
        initialize(file);
        Points[] arr = coordinates;
        Quickhull(arr);
    }
    
    public void initialize(String file) {
        numOfPoints = 0;
        numOfConvexPoints = 0;
        fileContents = file.split("\\r?\\n");
        coordinates = new Points[fileContents.length];
        convex_hull = new Points[fileContents.length];
        added = new Hashtable<>();
        
        
        for (int i = 0; i < fileContents.length; i++) {
            String str = fileContents[i];  
            String[] arr = str.split(" ");
            float x =  Float.parseFloat(arr[0]); 
            float y = Float.parseFloat(arr[1]);
            Points p = new Points(x, y, i);
            coordinates[i] = p;
            numOfPoints++;
        }

    }
    
    private void Quickhull(Points[] P) {
        
        //Find the leftmost and rightmost points A and B
        Points[] twoPoints = findPoints(P);
        Points A = twoPoints[0];
        Points B = twoPoints[1];
        
        //Add points A and B to Convex_hull
        convex_hull[0] = A;
        numOfConvexPoints++;
        convex_hull[1] = B;
        numOfConvexPoints++;
        added.put(A, A.nodeNumber);
        added.put(B, B.nodeNumber);
        
        //Segment AB divides the remaining points into 2 groups S1 aand S2
        //find points S1, points on the right side of the oriented line AB
        Points[] S1 = new Points[P.length];
        //find points S2, points on the right side of the oriented line BA, reversed
        Points[] S2 = new Points[P.length];
        int s1Count = 0;
        int s2Count = 0;
        for (int i = 0; i < P.length; i++) {
            //find out if the point is used in the definition of the line
            if(P[i] == A || P[i] == B)
            {continue;}
            
            //Calculation to find out what side of the line the point is
            float calc = sideOfLine(A, B, P[i]);
            //Find points on the right side of the line S1, put these in S1
            if(calc < 0) {
                S1[s1Count] = P[i];
                s1Count++;
            }
            //Find points on the left side of the line, put these in S2
            else if(calc > 0) {
                S2[s2Count] = P[i];
                s2Count++;
            }
            //Find points that are on the line, put these in S1
            else if(calc == 0) {
                S1[s1Count] = P[i];
                s1Count++;
            }
            
        }
        
        //remove null spaces in S1 and S2
        Points[] temp = S1;
        S1 = new Points[s1Count];
        System.arraycopy(temp, 0, S1, 0, s1Count);
        temp = S2;
        S2 = new Points[s2Count];
        System.arraycopy(temp, 0, S2, 0, s2Count);
        
//         System.out.println("S1 Points");
//         listPoints(S1);
//         System.out.println(" ");
//         System.out.println("S2 Points");
//         listPoints(S2);
        
        //Call recursive function Find_hull(S1, A, B)
        Find_hull(S1, A, B);
        
        //Call recursive function Find_hull(S2, B, A)
        Find_hull(S2, B, A);
        
        //output hullPoints
        System.out.println("Hull Points");
        
        temp = convex_hull;
        convex_hull = new Points[numOfConvexPoints];
        System.arraycopy(temp, 0, convex_hull, 0, numOfConvexPoints);
        
        String hullPoints = listPoints(convex_hull);
        writeFile(hullPoints);
        
    }
    
    public void Find_hull(Points[] Point_set, Points P, Points Q) {
        //System.out.println("Find_hull");

        
        //If Point_set is empty return
        if(Point_set.length == 0) {
            return;
        }
        
        //Find the farthest point, C from segment PQ in the Point_set, need MATH
        float maxDistance = 0;
        Points C = Point_set[0];
        for(int i = 0; i < Point_set.length; i++) {
            float distance = hullDistance(P, Q, Point_set[i]);
            if(distance > maxDistance) {
                C = Point_set[i];
                maxDistance = distance;
            }
        }
        
        //Add C to convex hull
        if(!added.containsKey(C)) {
        convex_hull[numOfConvexPoints] = C;
        numOfConvexPoints++;
        added.put(C, C.nodeNumber);
        }
        
//         System.out.println("triangle Definition");
//         System.out.println((int)P.x + " " + (int)P.y);
//         System.out.println((int)Q.x + " " + (int)Q.y);
//         System.out.println((int)C.x + " " + (int)C.y);
//         System.out.println("");
//         System.out.println("Inside Triangle:");
        
        //Three points P, Q, and C form a triangle and partition the remaining points into 3 subsets: S0, S1, and S2
        //S0 are the points inside the triangle PQC and can be ignored, need MATH.
        Points[] remaining = new Points[Point_set.length];
        Points[] S0 = new Points[Point_set.length];
        Points[] S1 = new Points[Point_set.length];
        Points[] S2 = new Points[Point_set.length];
        int S0count = 0;
        int S1count = 0;
        int S2count = 0;
        int remcount = 0;
        
        Points[] triangle = new Points[]{P,Q,C};
        for(int i = 0; i < Point_set.length; i++) {
            //test to see if the point is part of the triangle
            if(Point_set[i] == C) {
                continue;
            }
            
            //test to see if the point is in the triangle
            boolean result = hullCalculations(triangle, Point_set[i]);
            if(result == true) {
                S0[S0count] = Point_set[i];
                S0count++;
                //System.out.println((int)Point_set[i].x + " " + (int)Point_set[i].y);
                continue;
            }
            
            //if the point is not in the triangle place in remaining
            remaining[remcount] = Point_set[i];
            remcount++;
        }
        
        //remove null spaces in S1 and S2
        Points[] temp = S0;
        S0 = new Points[S0count];
        System.arraycopy(temp, 0, S0, 0, S0count);
        temp = remaining;
        remaining = new Points[remcount];
        System.arraycopy(temp, 0, remaining, 0, remcount);
        

        
        
        //S1 are the points on the right side of the oriented line PC, need MATH     
        //S2 are the points on the right side of teh oriented line CQ, need MATH
        
        //Find points for S1 on the right side of oriented line PC
        for (int i = 0; i < remaining.length; i++) {
            //find out if the point is used in the definition of the line
            if(remaining[i] == P || remaining[i] == C)
            {continue;}
            
            //Calculation to find out what side of the line the point is
            float calc = sideOfLine(P, C, remaining[i]);
            //Find points on the right side of the line S1, put these in S1
            if(calc < 0 || calc == 0) {
                S1[S1count] = remaining[i];
                S1count++;
            }
            
        }
        
        //Find points for S2 on the right side of oriented line CQ
        for (int i = 0; i < remaining.length; i++) {
            //find out if the point is used in the definition of the line
            if(remaining[i] == C || remaining[i] == Q)
            {continue;}
            
            //Calculation to find out what side of the line the point is
            float calc = sideOfLine(C, Q, remaining[i]);
            //Find points on the right side of the line S1, put these in S1
            //if(calc > 0) {   //Changed
            if(calc < 0 || calc == 0) {
                S2[S2count] = remaining[i];
                S2count++;
            }
            
        }
        
        //remove null spaces in S1 and S2
        temp = S1;
        S1 = new Points[S1count];
        System.arraycopy(temp, 0, S1, 0, S1count);
        temp = S2;
        S2 = new Points[S2count];
        System.arraycopy(temp, 0, S2, 0, S2count);
        
//         System.out.println("S1 Points");
//         listPoints(S1);
//         System.out.println(" ");
//         System.out.println("S2 Points");
//         listPoints(S2);

        
        //Recursively call the Find_hull(S1, P, C) function for one set of points
        Find_hull(S1, P, C);
        
        //Recursively call the Find_hull(S2, C, Q) function for the other set of points
        Find_hull(S2, C, Q);
        
        
    }
    
    public float sideOfLine(Points A, Points B, Points P) {
        float result = ((B.x - A.x) * (P.y - A.y)) - ((B.y - A.y) * (P.x - A.x));
        return result;
    }
    
    
    //method for finding the distance a point is from a line defined by two points
    public float hullDistance(Points A, Points B, Points P) {
        //Inputs: 
        //two points A and B to define a line 
        //Point P with coordinates P.x and P.y for finding the length from the line to the point
        //m = slope of line AB, b = intercept

        //m = y2-y1/x2-x1
        float m = (B.y - A.y) / (B.x - A.x);
    
        //y = mx+ b
        //y - y1 = m(x - x1)
        //y = m(x - x1) + y1
        //float b = (m * A.x) + A.y;
        float b = -(m * A.x) + A.y;
        double mm = (double)m;
        double l =  (mm*mm) + 1;
        double root = java.lang.Math.sqrt(l);
        //java.lang.Math.pow() // 
    
        //Pow
        float distance = (((m * -1) * P.x) + (P.y - b) )/ ((float)root);
        return java.lang.Math.abs(distance);
    }
    
    //method for finding out if a point lies inside of a triangle
    public boolean hullCalculations(Points[] triangle, Points P) {
        boolean inside = false;
        Points A = triangle[0];
        Points B = triangle[1];
        Points C = triangle[2];
        
        //Triangle ABC, If all three calculations have the same sign, the point P is in the triangle
        float one = ((P.x - A.x) * (B.y - A.y)) - ((P.y - A.y) * (B.x - A.x));
        float two = ((P.x - B.x) * (C.y - B.y)) - ((P.y - B.y) * (C.x - B.x));
        float three = ((P.x - C.x) * (A.y - C.y)) - ((P.y - C.y) * (A.x - C.x));
        
        if(one > 0 && two > 0 && three > 0) {
            inside = true;
        }
        
        else if(one < 0 && two < 0 && three < 0) {
            inside = true;
        }
        
        return inside;
    }
    
    
    //function for finding points 
    public Points[] findPoints(Points[] P) {
        //array for holding the two points
        Points[] twoPoints = new Points[2];
        //Math for finding the two points
        //Loop thru all of the points to find the leftmost and rightmost points
        int i = 0;
        Points Xmin = P[i];
        Points Xmax = P[i];
        i++;
        for (; i < P.length; i++) {
            if(P[i].x < Xmin.x)
            {Xmin = P[i];}
            else if(P[i].x > Xmax.x)
            {Xmax = P[i];}
            
        } 
        twoPoints[0] = Xmin;
        twoPoints[1] = Xmax;
        return twoPoints;
    }
    
    public String listPoints(Points[] P) {
        String List = "";
        for(int i = 0; i < P.length; i++) {
            System.out.println((int)P[i].x + " " + (int)P[i].y);
            List = List + (int)P[i].x + " " + (int)P[i].y + "\n";
        }
        return List;
    }
    

    
    
    
    
    //Class for holding point values and information on points
    public class Points {
        public float x;
        public float y;
        public float distance;
        public int nodeNumber;
        public boolean isOnRight;
        
        public Points(float xValue, float yValue, int nodeNum) {
            x = xValue;
            y = yValue;
            nodeNumber = nodeNum;
        }
        
        public void setDistance(float value) {
            distance = value;
        }
        
        public void setIsOnRight(boolean value) {
            isOnRight = value;
        }
        
        public float getDistance() {
            return distance;
        }
        
        public boolean getIsOnRight() {
            return isOnRight;
        }
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