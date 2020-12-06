package infernobuster.detector;

import java.util.ArrayList;
import java.util.HashMap;

import infernobuster.parser.Rule;

public class Detector {
	public Detector() {}
	
	// Algorithm 
	public DetectionResult detect(ArrayList<Rule> rules) {
		HashMap<Anomaly, ArrayList<Rule>> result = new HashMap<Anomaly, ArrayList<Rule>>();
		
		for(int i = 0; i < rules.size(); i++) {
			for(int j = i + 1; j < rules.size(); j++) {
				if(i == j || rules.get(i).compareProtocol(rules.get(j).getProtocol()) || rules.get(i).getDirection() != rules.get(j).getDirection()) continue;
				
				Anomaly anomaly = null;
				ArrayList<Rule> rList = null;
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
					rList = result.getOrDefault(anomaly, new ArrayList<Rule>());
					rList.add(rules.get(i));
					rList.add(rules.get(j));
					result.put(anomaly, rList);
				}
			}
		}
		
		return new DetectionResult(result);
	}
}
