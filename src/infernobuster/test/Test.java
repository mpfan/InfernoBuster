package infernobuster.test;

import infernobuster.parser.Action;
import infernobuster.parser.Direction;
import infernobuster.parser.Rule;

public class Test {
	public static void main(String args[]) {
		// Test rule class
		Rule rule1 = new Rule("192.168.0.1/24", "192.168.0.1/32", 23, 23, Action.ALLOW, Direction.OUT, 2);
		Rule rule2 = new Rule("192.168.0.1/23", "192.168.0.1/31", 23, 23, Action.DENY, Direction.OUT, 1);
		
		System.out.println(rule2.isShadowing(rule1));
	}
}
