package Parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

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

	
	public static boolean openDotExeCodeString(IMethodAsString method) {
		System.out.println(method.getMethodAsGraphVizString());
		// Save our string as a file first
		if (saveTextFile(method.getMethodName(), method.getMethodAsGraphVizString())) {
			try {
				// Call the file and create a 'jpg'
				Process process = new ProcessBuilder("C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe","-Tjpeg", method.getMethodName(),"-o","graph2.jpg").start();
				
				new ImageDisplay(System.getProperty("user.dir") + "/graph2.jpg");
				
				// Open the JPeg in your standard photo viewer
				
			} catch (IOException ex) {
				return false;
			}		
			
			return true;
		}
		
		return false;
		
	}
}
