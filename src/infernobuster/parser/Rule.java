package infernobuster.parser;

public class Rule {
	private String sourceIp; // CIDR
	private String destinationIp; // CIDR
	private IpRange source;
	private IpRange destination;
	private int sourcePort;
	private int destinationPort;
	private int priority;
	private Action action;
	private Direction direction;
	private Protocol protocol;
	
	public Rule(String sourceIp, String destinationIp, int sourcePort, int destinationPort, Action action, Direction direction, Protocol protocol, int priority) {
		this.sourceIp = sourceIp;
		this.destinationIp = destinationIp;
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
		this.action = action;
		this.direction = direction;
		this.protocol = protocol;
		this.priority = priority;
		
		source = new IpRange(sourceIp);
		destination = new IpRange(destinationIp);
	}
	
	public String getSourceIp() {
		return sourceIp;
	}

	public String getDestinationIp() {
		return destinationIp;
	}

	public int getSourcePort() {
		return sourcePort;
	}

	public int getDestinationPort() {
		return destinationPort;
	}

	public int getPriority() {
		return priority;
	}
	
	public IpRange getSource() {
		return source;
	}

	public IpRange getDestination() {
		return destination;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public Action getAction() {
		return action;
	}
	
	public Protocol getProtocol() {
		return protocol;
	}
	
	private boolean equals(Rule rule) {
		return source.equals(rule.getSource()) 
				&& destination.equals(rule.getDestination()) 
				&& sourcePort == rule.getSourcePort() 
				&& destinationPort == rule.getDestinationPort()
				&& direction == rule.getDirection();
	}
	
	private boolean isSubset(Rule rule) {
		return source.isSubset(rule.getSource()) 
		&& destination.isSubset(rule.getDestination()) 
		&& sourcePort == rule.getSourcePort() 
		&& destinationPort == rule.getDestinationPort()
		&& direction == rule.getDirection();
	}
	
	private boolean isSuperset(Rule rule) {
		return source.isSuperset(rule.getSource()) 
		&& destination.isSuperset(rule.getDestination()) 
		&& sourcePort == rule.getSourcePort() 
		&& destinationPort == rule.getDestinationPort()
		&& direction == rule.getDirection();
	}
	
	private boolean isIntersecting(Rule rule) {
		return source.isIntersecting(rule.getSource())
				&& destination.isIntersecting(rule.getDestination())
				&& sourcePort == rule.getSourcePort() 
				&& destinationPort == rule.getDestinationPort()
				&& direction == rule.getDirection();
	}
	
	public boolean isRedundant(Rule rule) {
		return equals(rule)&& action == rule.getAction();
	}
	
	public boolean isInconsistent(Rule rule) {
		return equals(rule)&& action != rule.getAction();
	}
	
	public boolean isShadowing(Rule rule) {
		return isSuperset(rule) && action != rule.getAction() && priority < rule.getPriority();
	}
	
	public boolean isDownRedundant(Rule rule) {
		return isSuperset(rule) && action == rule.getAction() && priority < rule.getPriority();
	}
	
	public boolean isGeneralizing(Rule rule) {
		return isSubset(rule) && action != rule.getAction() && priority < rule.getPriority();
	}
	
	public boolean isUpRedundant(Rule rule) {
		return isSubset(rule) && action == rule.getAction() && priority < rule.getPriority();
	}
	
	public boolean isCorrelating(Rule rule) {
		return isIntersecting(rule) && action != rule.getAction();
	}
	
	public boolean isPartialRedundant(Rule rule) {
		return isIntersecting(rule) && action == rule.getAction();
	}
	
	public String toString() {
		return sourceIp + " " + sourcePort + " " + destinationIp + " " + destinationPort + " " + action + " " + direction + " " + protocol + " " + priority;
	}
}
