package infernobuster.test;

import java.util.ArrayList;

import infernobuster.detector.DetectionResult;
import infernobuster.detector.Detector;
import infernobuster.parser.Action;
import infernobuster.parser.Direction;
import infernobuster.parser.Protocol;
import infernobuster.parser.Rule;

public class Test {
	public static void main(String args[]) {
		// Test rule class
		Rule rule1 = new Rule("192.168.0.1/24", "192.168.0.1/32", 23, 23, Action.ALLOW, Direction.OUT, Protocol.TCP, 2);
		Rule rule2 = new Rule("192.168.0.1/23", "192.168.0.1/31", 23, 23, Action.DENY, Direction.OUT, Protocol.TCP, 1);
		
		System.out.println(rule2.isShadowing(rule1));
		
		System.out.println(rule1.getSource());
		System.out.println(rule1.getDestination());
		System.out.println(rule2.getSource());
		System.out.println(rule2.getDestination());
		
		// Test detector
		Detector detector = new Detector();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		rules.add(rule1);
		rules.add(rule2);
		
		DetectionResult result = detector.detect(rules);
		System.out.println(result);
	}
}
