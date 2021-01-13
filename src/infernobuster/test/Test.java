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
		Rule rule2 = new Rule("192.168.0.1/23", "192.168.0.1/31", 23, 23, Action.DENY, Direction.OUT, Protocol.TCP, 1);
		Rule rule1 = new Rule("192.168.0.1/24", "192.168.0.1/32", 23, 23, Action.ALLOW, Direction.OUT, Protocol.TCP, 2);
		
		Rule rule3 = new Rule("10.1.2.0/23", "10.1.2.0/24", 44, 23, Action.DENY, Direction.IN, Protocol.TCP, 1);
		Rule rule4 = new Rule("10.1.2.0/23", "10.1.2.0/24", 44, 23, Action.DENY, Direction.IN, Protocol.TCP, 2);
		Rule rule5 = new Rule("10.1.2.0/23", "10.1.2.0/24", 44, 23, Action.DENY, Direction.IN, Protocol.TCP, 3);
		Rule rule6 = new Rule("10.1.2.0/23", "10.1.2.0/24", 44, 23, Action.DENY, Direction.IN, Protocol.TCP, 4);
		
		
		System.out.println(rule2.isShadowing(rule1));
		System.out.println(rule3.isRedundant(rule4));
		
		System.out.println(rule1.getSource());
		System.out.println(rule1.getDestination());
		System.out.println(rule2.getSource());
		System.out.println(rule2.getDestination());
		
		// Test detector
		Detector detector = new Detector();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		rules.add(rule2);
		rules.add(rule1);
		rules.add(rule3);
		rules.add(rule4);
		rules.add(rule5);
		rules.add(rule6);
		
		DetectionResult result = detector.detect(rules);
		System.out.println(result);
	}
}
