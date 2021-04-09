package infernobuster.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Detector {
	public Detector() {}
	
	// Algorithm 
	public DetectionResult detect(ArrayList<Rule> rules) {
		long startTime = System.nanoTime();
		HashMap<Anomaly, Set<Rule>> result = new HashMap<Anomaly, Set<Rule>>();
		HashMap<Integer, ArrayList<Integer>> conflictingRules = new HashMap<Integer, ArrayList<Integer>>();

		for(int i = 0; i < rules.size(); i++) {
			for(int j = i + 1; j < rules.size(); j++) {
				if(i == j) continue;
				
				Anomaly anomaly = null;
				Set<Rule> rList = null;
				ArrayList<Integer> cList = null;
				if(rules.get(i).isRedundant(rules.get(j))) {
					anomaly = Anomaly.REDUNDANCY;
				}
				else if(rules.get(i).isInconsistent(rules.get(j))) {
					anomaly = Anomaly.INCONSISTENCY;
				}
				else if(rules.get(i).isShadowing(rules.get(j))) {
					anomaly = Anomaly.SHADOWING;
				}
				else if(rules.get(i).isDownRedundant(rules.get(j))) {
					anomaly = Anomaly.DOWN_REDUNDANT;
				}
				else if(rules.get(i).isGeneralizing(rules.get(j))) {
					anomaly = Anomaly.GENERALIZIATION;
				}
				else if(rules.get(i).isUpRedundant(rules.get(j))) {
					anomaly = Anomaly.UP_REDUNDANT;
				}
				else if(rules.get(i).isCorrelating(rules.get(j))) {
					anomaly = Anomaly.CORRELATION;
				}
				else if(rules.get(i).isPartialRedundant(rules.get(j))) {
					anomaly = Anomaly.PARTIAL_REDUNDANCY;
				}
				
				if(anomaly != null) {
					rList = result.getOrDefault(anomaly, new HashSet<Rule>());
					rList.add(rules.get(i));
					rList.add(rules.get(j));
					result.put(anomaly, rList);
					
					cList = conflictingRules.getOrDefault(rules.get(i).getId(), new ArrayList<Integer>());
					cList.add(rules.get(j).getId());
					conflictingRules.put(rules.get(i).getId(), cList);
				}
			}
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("Detector took " + duration/1000000 + " milliseconds to find anomalies in " + rules.size() + " rules");
		return new DetectionResult(result, conflictingRules);
	}
}
