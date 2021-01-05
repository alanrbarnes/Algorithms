//Written By: Alan Barnes
//Written on: September 29, 2020
//Written for: CSC 482, Assignment 2
//class for reading in external files
import java.util.*;
import java.lang.*;
import java.io.*;
import java.nio.file.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.FileOutputStream;
// import java.io.IOException;

public class ABGraphReadFile {

    /////////////////////////////
    //method for opening a file//
    /////////////////////////////
    public String readFile() {
	     String path = openSaveDialog("txt", 1);
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