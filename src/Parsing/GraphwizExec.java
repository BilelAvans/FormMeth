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

public class GraphwizExec {
	
	public static boolean saveTextFile(String filename, String content) {
		File file = new File(System.getProperty("user.dir") + "/"+filename);
		
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.write(content);
			writer.close();
			
		} catch (FileNotFoundException fEx) {
			return false;
		}
		
		return true;
	}
	
	public static String loadTextFile(String filename) {
		
		try {
			File file = new File(System.getProperty("user.dir") + "/"+filename);
			BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";
			while(fr.ready()) {
				line += fr.readLine();
			}
			fr.close();
			System.out.println(line);
			
			return line;
			
		} catch (FileNotFoundException fEx) {
			return "";
		} catch (IOException ioEx) {
			return "";
		}
	}
	
	public static boolean openDotExeCodeString(IMethodAsString method) {
		System.out.println("Here: "+ method.getMethodAsGraphVizString());
		// Save our string as a file first
		if (saveTextFile(method.getMethodName(), method.getMethodAsGraphVizString())) {
			try {
				// Call the file and create a 'jpg'
				Process process = new ProcessBuilder("C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe","-Tjpeg", method.getMethodName(),"-o","graph.jpg").start();
				
				new ImageDisplay(System.getProperty("user.dir") + "/graph.jpg");
				
				// Open the JPeg in your standard photo viewer
				
			} catch (IOException ex) {
				return false;
			}		
			
			return true;
		}
		
		return false;
		
	}
	
	public static JLabel openDotExeCodePanel(IMethodAsString method) {
		System.out.println("Here: "+ method.getMethodAsGraphVizString());
		// Save our string as a file first
		if (saveTextFile(method.getMethodName(), method.getMethodAsGraphVizString())) {
			try {
				// Call the file and create a 'jpg'
				Process process = new ProcessBuilder("C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe","-Tjpeg", method.getMethodName(),"-o","graph.jpg").start();
				
				return ImageDisplay.GetJComponentFromFile(System.getProperty("user.dir") + "/graph.jpg");
				
				// Open the JPeg in your standard photo viewer
				
			} catch (IOException ex) {
				return null;
			}		
			
		}
		
		return null;
		
	}
}
