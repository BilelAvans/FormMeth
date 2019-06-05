package Parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileStorage {
	
	public static boolean saveTextFile(String filename, String content) {
		System.out.println(filename);
		if (!filename.endsWith(".gv"))
			filename += ".gv";
		
		System.out.println("But here");
		
		File file = new File(System.getProperty("user.dir")+"\\Storage\\JPEG\\"+filename);
		
		System.out.println("Saved");
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
		if (!filename.endsWith(".gv"))
			filename += ".gv";
		
		try {
			File file = new File(System.getProperty("user.dir")+"\\Storage\\JPEG\\"+filename);
			BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = "";
			while(fr.ready()) {
				line += fr.readLine();
			}
			fr.close();
			
			return line;
			
		} catch (FileNotFoundException fEx) {
			return "";
		} catch (IOException ioEx) {
			return "";
		}
	}
}
