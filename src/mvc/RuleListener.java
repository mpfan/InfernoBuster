package mvc;

import infernobuster.client.Model;

public interface RuleListener {
	public void anomalyDetected(Model model);
	public void ruleModified(Model model);
}