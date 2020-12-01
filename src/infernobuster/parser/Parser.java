package infernobuster.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public abstract class Parser {
	protected String read(String filename) {
		File file = new File(filename); 
		  
		BufferedReader br;
		StringBuilder sb = null;
		try {
			br = new BufferedReader(new FileReader(file));
			
			sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return sb.toString();
	}
}
