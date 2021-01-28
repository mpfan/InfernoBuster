package infernobuster.model;

public interface RuleListener {
	public void anomalyDetected(Model model);
	public void ruleModified(Model model);
	public void ruleFocused(Model model);
}
