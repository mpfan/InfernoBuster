package infernobuster.detector;

import java.util.ArrayList;
import java.util.HashMap;

import infernobuster.parser.Rule;

public class DetectionResult {
	HashMap<Anomaly, ArrayList<Rule>> result;
	
	public DetectionResult(HashMap<Anomaly, ArrayList<Rule>> result) {
		this.result = result;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (HashMap.Entry<Anomaly, ArrayList<Rule>> entry : result.entrySet()) {
			sb.append(entry.getKey() + ":\n");
			for(Rule rule: entry.getValue()) {
				sb.append(rule.toString() + "\n");
			}
			sb.append("-----------------\n");
        }
		
		return sb.toString();
	}
	
	public HashMap<Anomaly, ArrayList<Rule>> getResult() {
		return result;
	}
}
