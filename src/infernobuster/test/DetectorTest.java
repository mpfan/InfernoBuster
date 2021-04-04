package infernobuster.model;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

public class DetectorTest extends TestCase {
	Rule rule1;
	IpRange source1; 

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDetect() {
		// Test rule class
		Rule rule2 = new Rule("192.168.0.1/23", "192.168.0.1/31", 23, 23, Action.DENY, Direction.OUT, Protocol.TCP, 1);
		Rule rule1 = new Rule("192.168.0.1/24", "192.168.0.1/32", 23, 23, Action.ALLOW, Direction.OUT, Protocol.TCP, 2);
		
		Rule rule3 = new Rule("10.1.2.0/23", "10.1.2.0/24", 44, 23, Action.DENY, Direction.IN, Protocol.TCP, 1);
		Rule rule4 = new Rule("10.1.2.0/23", "10.1.2.0/24", 44, 23, Action.DENY, Direction.IN, Protocol.TCP, 2);
		Rule rule5 = new Rule("10.1.2.0/23", "10.1.2.0/24", 44, 23, Action.DENY, Direction.IN, Protocol.TCP, 3);
		Rule rule6 = new Rule("10.1.2.0/23", "10.1.2.0/24", 44, 23, Action.DENY, Direction.IN, Protocol.TCP, 4);

		
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
		
		assertTrue(result instanceof DetectionResult);
		
		HashMap<Anomaly, Integer> numOfAnomalies = new HashMap<Anomaly, Integer>() {{
			put(Anomaly.SHADOWING, 2);
			put(Anomaly.REDUNDANCY, 4);
		}};
		
		assertEquals(result.getNumOfAnomalies(), numOfAnomalies);
	}

}

