package infernobuster.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public abstract class Parser {
	protected ArrayList<String> read(String filename) {
		File file = new File(filename); 
		  
		BufferedReader br;
		ArrayList<String> content = new ArrayList<String>();
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line;
			while ((line = br.readLine()) != null) {
				content.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return content;
	}
	
	public abstract ArrayList<Rule> parse(String file);
}
