package infernobuster.client;

import java.util.ArrayList;

import infernobuster.detector.DetectionResult;
import infernobuster.detector.Detector;
import infernobuster.parser.IpTableParser;
import infernobuster.parser.Parser;
import infernobuster.parser.Rule;
import infernobuster.parser.UFWParser;

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
		Parser parser = null;
		if(args[1].equalsIgnoreCase("--iptable")) {
			parser = new IpTableParser();
		} else if(args[1].equalsIgnoreCase("--ufw")) {
			parser = new UFWParser();
		}
		ArrayList<Rule> rules = parser.parse(args[0]);
		
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
