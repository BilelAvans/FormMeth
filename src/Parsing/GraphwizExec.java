package Parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GraphwizExec {
	
	public static boolean openDotExeCodeString(IMethodAsString method) {
		String fullGvName = System.getProperty("user.dir")+"\\Storage\\JPEG\\" +method.getMethodName()+".jpg";
		String fullJpegName = System.getProperty("user.dir")+"\\Storage\\JPEG\\" +method.getMethodName()+".jpg";
		
		// Save our string as a file first
		if (FileStorage.saveTextFile(method.getMethodName(), method.getMethodAsGraphVizString())) {
			try {
				// Call the file and create a 'jpg'
				Process process = new ProcessBuilder("C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe","-Tjpeg", method.getMethodName(),"-o","/Storage/JPEG/" +method.getMethodName()+".jpg").start();
				
				new ImageDisplay(System.getProperty("user.dir") +"/Storage/JPEG/" +method.getMethodName()+".jpg");
				
				// Open the JPeg in your standard photo viewer
				
			} catch (IOException ex) {
				return false;
			}		
			
			return true;
		}
		
		return false;
		
	}
	
	public static JLabel generateDFAJLabel(IMethodAsString method) {
		String fullGvName = System.getProperty("user.dir")+"\\Storage\\JPEG\\" +method.getMethodName()+".gv";
		String fullJpegName = System.getProperty("user.dir")+"\\Storage\\JPEG\\" +method.getMethodName()+".jpg";
		
		// Save our string as a file first
		if (FileStorage.saveTextFile(method.getMethodName(), method.getMethodAsGraphVizString())) {
			try {
				
				
				// Call the file and create a 'jpg'
				Process process = new ProcessBuilder("C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe","-Tjpeg", fullGvName,"-o", fullJpegName).start();
				
				return ImageDisplay.GetJComponentFromFile(fullJpegName);
				
				// Open the JPeg in your standard photo viewer
				
			} catch (IOException ex) {
				return null;
			}		
			
		}
		
		return null;
		
	}
	
	public static JLabel loadDFAJLabel(IMethodAsString method) {
		String fullJpegName = System.getProperty("user.dir")+"\\Storage\\JPEG\\" +method.getMethodName();
		
		return ImageDisplay.GetJComponentFromFile(fullJpegName);
	
	}
	
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
