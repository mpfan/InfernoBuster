package infernobuster.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import infernobuster.model.DetectionResult;
import infernobuster.model.Detector;
import infernobuster.model.IpTableParser;
import infernobuster.model.Parser;
import infernobuster.model.ParserException;
import infernobuster.model.Rule;
import infernobuster.model.UFWParser;

/*
 * --ufw for ufw
 * --iptable for iptable
 * 
 * --debug # for printing the rules
 * 
 * Format for command:
 * <<program name>> <<file name>> <<file format>>
 *                     args[0]        args[1]
 */

public class Client {
	public static void main(String[] args) {
		File file = new File(".");
		System.out.println(file.getAbsolutePath());
		System.out.println(file.exists());
		  
		BufferedReader br;
		ArrayList<String> content = new ArrayList<String>();
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line;
			while ((line = br.readLine()) != null) {
				content.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Error reading file");
			System.exit(1);
		} 

		Parser parser = null;
		if(args[1].equalsIgnoreCase("--iptable")) {
			parser = new IpTableParser();
		} else if(args[1].equalsIgnoreCase("--ufw")) {
			parser = new UFWParser();
		} else {
			System.out.println("Invalid option");
			System.exit(1);
		}

		ArrayList<Rule> rules = null;
		try {
			rules = parser.parse(content);
		} catch (ParserException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		if(args[2].equalsIgnoreCase("--debug")) {
			System.out.println("Parsed Rules:");
			for(Rule rule : rules) {
				System.out.println(rule);
			}
			System.out.println("-----------------");
		}
		
		
		Detector detector = new Detector();
		DetectionResult result = detector.detect(rules);
		System.out.println(result);
	}
}
