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
	
	public HashMap<Anomaly, Integer> getNumOfAnomalies() {
		HashMap<Anomaly, Integer> stats = new HashMap<Anomaly, Integer>();
		
		for (HashMap.Entry<Anomaly, ArrayList<Rule>> entry : result.entrySet()) {
			stats.put(entry.getKey(), entry.getValue().size());
        }
		
		return stats;
	}
	
	public Anomaly getTypeOfAnomaly(Rule rule) {
		for (HashMap.Entry<Anomaly, ArrayList<Rule>> entry : result.entrySet()) {
			for(Rule r: entry.getValue()) {
				if(r.getId() == rule.getId()) {
					return entry.getKey();
				}
			}
        }
		
		return null;
	}
	
	public ArrayList<Anomaly> getTypesOfAnomaly(Rule rule) {
		ArrayList<Anomaly> anomalies = new ArrayList<Anomaly>();
		
		for (HashMap.Entry<Anomaly, ArrayList<Rule>> entry : result.entrySet()) {
			for(Rule r: entry.getValue()) {
				if(r.getId() == rule.getId()) {
					anomalies.add(entry.getKey());
				}
			}
        }
		
		return anomalies;
	}
}
