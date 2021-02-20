package infernobuster.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This class encapsulates the conflicting rules detected from the Detector and adds some helper 
 * methods for working with the conflicting rules.
 *
 */
public class DetectionResult {
	HashMap<Anomaly, Set<Rule>> result;
	HashMap<Integer, ArrayList<Integer>> conflictingRules;
	
	public DetectionResult(HashMap<Anomaly, Set<Rule>> result, HashMap<Integer, ArrayList<Integer>> conflictingRules) {
		this.result = result;
		this.conflictingRules = conflictingRules;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (HashMap.Entry<Anomaly, Set<Rule>> entry : result.entrySet()) {
			sb.append(entry.getKey() + ":\n");
			for(Rule rule: entry.getValue()) {
				sb.append(rule.toString() + "\n");
			}
			sb.append("-----------------\n");
        }
		
		return sb.toString();
	}
	
	public HashMap<Anomaly, Set<Rule>> getResult() {
		return result;
	}
	
	public HashMap<Anomaly, Integer> getNumOfAnomalies() {
		HashMap<Anomaly, Integer> stats = new HashMap<Anomaly, Integer>();
		
		for (HashMap.Entry<Anomaly, Set<Rule>> entry : result.entrySet()) {
			stats.put(entry.getKey(), entry.getValue().size());
        }
		
		return stats;
	}
	
	public Anomaly getTypeOfAnomaly(Rule rule) {
		for (HashMap.Entry<Anomaly, Set<Rule>> entry : result.entrySet()) {
			for(Rule r: entry.getValue()) {
				if(r.getId() == rule.getId()) {
					return entry.getKey();
				}
			}
        }
		
		return null;
	}
	
	/**
	 * Returns all the type of anomalies that a rule is having.
	 */
	public ArrayList<Anomaly> getTypesOfAnomaly(Rule rule) {
		ArrayList<Anomaly> anomalies = new ArrayList<Anomaly>();
		
		for (HashMap.Entry<Anomaly, Set<Rule>> entry : result.entrySet()) {
			for(Rule r: entry.getValue()) {
				if(r.getId() == rule.getId()) {
					anomalies.add(entry.getKey());
				}
			}
        }
		
		return anomalies;
	}
	
	/**
	 * Returns a list of rule ids that the a given rule has conflict with
	 */
	public ArrayList<Integer> getConflictedRules(int id) {
		ArrayList<Integer> conflictedRules = conflictingRules.getOrDefault(id, null);
		
		if(conflictedRules == null) {
			conflictedRules = new ArrayList<Integer>();
			
			for (HashMap.Entry<Integer, ArrayList<Integer>> entry : conflictingRules.entrySet()) {
				for(Integer ruleId: entry.getValue()) {
					if(ruleId == id) {
						conflictedRules.add(entry.getKey());
					}
				}
	        }
		}
		
		return conflictedRules;
	}
}
