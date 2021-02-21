package infernobuster.model;

/**
 * This class abstracts firewall rules into an object along with their required properties.
 * If any other properties are included in a firewall rule, then they should be added in here.
 *
 */
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
	private int id;
	
	public static int ANY = -1;
	
	public static int NUM_OF_FIELD = 8;
	
	private static int idCounter = 0;
	
	/**
	 * 
	 * @param sourceIp
	 * @param destinationIp
	 * @param sourcePort
	 * @param destinationPort
	 * @param action
	 * @param direction
	 * @param protocol
	 * @param priority
	 */
	public Rule(String sourceIp, String destinationIp, int sourcePort, int destinationPort, Action action, Direction direction, Protocol protocol, int priority) {
		this.sourceIp = sourceIp;
		this.destinationIp = destinationIp;
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
		this.action = action;
		this.direction = direction;
		this.protocol = protocol;
		this.priority = priority;
		this.id = idCounter;
		
		idCounter++;
		
		
		source = new IpRange(sourceIp.equalsIgnoreCase("any") ? "any" : sourceIp);
		destination = new IpRange(destinationIp.equalsIgnoreCase("any") ? "any" : destinationIp);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSourceIp() {
		return sourceIp;
	}

	/**
	 * 
	 * @return
	 */
	public String getDestinationIp() {
		return destinationIp;
	}

	/**
	 * 
	 * @return
	 */
	public int getSourcePort() {
		return sourcePort;
	}

	/**
	 * 
	 * @return
	 */
	public int getDestinationPort() {
		return destinationPort;
	}

	/**
	 * 
	 * @return
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * 
	 * @return
	 */
	public IpRange getSource() {
		return source;
	}

	/**
	 * 
	 * @return
	 */
	public IpRange getDestination() {
		return destination;
	}
	
	/**
	 * 
	 * @return
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * 
	 * @return
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * 
	 * @return
	 */
	public Protocol getProtocol() {
		return protocol;
	}
	
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
		setSource(new IpRange(sourceIp.equalsIgnoreCase("any") ? "any" : sourceIp));
	}

	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
		setDestination(new IpRange(destinationIp.equalsIgnoreCase("any") ? "any" : destinationIp));
	}

	public void setSource(IpRange source) {
		this.source = source;
	}

	public void setDestination(IpRange destination) {
		this.destination = destination;
	}

	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}

	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return 
	 */
	private boolean comparePort(int port1, int port2) {
		if(port1 == ANY || port2 == ANY) {
			return true;
		} else {
			return port1 == port2;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean compareProtocol(Protocol protocol1, Protocol protocol2) {
		if(protocol1 == Protocol.ANY || protocol2 == Protocol.ANY) {
			return true;
		}
		else {
			return protocol1 == protocol2;
		}
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	private boolean equals(Rule rule) {
		return source.equals(rule.getSource()) 
				&& destination.equals(rule.getDestination()) 
				&& comparePort(sourcePort, rule.getSourcePort())
				&& comparePort(destinationPort, rule.getDestinationPort())
				&& compareProtocol(protocol, rule.getProtocol())
				&& direction == rule.getDirection();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	private boolean isSubset(Rule rule) {
		return source.isSubset(rule.getSource()) 
		&& destination.isSubset(rule.getDestination()) 
		&& comparePort(sourcePort, rule.getSourcePort())
		&& comparePort(destinationPort, rule.getDestinationPort())
		&& compareProtocol(protocol, rule.getProtocol())
		&& direction == rule.getDirection();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	private boolean isSuperset(Rule rule) {
		return source.isSuperset(rule.getSource()) 
		&& destination.isSuperset(rule.getDestination()) 
		&& comparePort(sourcePort, rule.getSourcePort())
		&& comparePort(destinationPort, rule.getDestinationPort())
		&& compareProtocol(protocol, rule.getProtocol())
		&& direction == rule.getDirection();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	private boolean isIntersecting(Rule rule) {
		return source.isIntersecting(rule.getSource())
				&& destination.isIntersecting(rule.getDestination())
				&& comparePort(sourcePort, rule.getSourcePort())
				&& comparePort(destinationPort, rule.getDestinationPort())
				&& compareProtocol(protocol, rule.getProtocol())
				&& direction == rule.getDirection();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	public boolean isRedundant(Rule rule) {
		return equals(rule) && action == rule.getAction();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	public boolean isInconsistent(Rule rule) {
		return equals(rule)&& action != rule.getAction();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	public boolean isShadowing(Rule rule) {
		return isSuperset(rule) && action != rule.getAction() && priority < rule.getPriority();
	}

	/**
	 * 
	 * @param rule
	 * @return
	 */
	public boolean isDownRedundant(Rule rule) {
		return isSuperset(rule) && action == rule.getAction() && priority < rule.getPriority();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	public boolean isGeneralizing(Rule rule) {
		return isSubset(rule) && action != rule.getAction() && priority < rule.getPriority();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	public boolean isUpRedundant(Rule rule) {
		return isSubset(rule) && action == rule.getAction() && priority < rule.getPriority();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	public boolean isCorrelating(Rule rule) {
		return isIntersecting(rule) && action != rule.getAction();
	}
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	public boolean isPartialRedundant(Rule rule) {
		return isIntersecting(rule) && action == rule.getAction();
	}
	
	/**
	 * 
	 */
	public String toString() {
		return sourceIp + " " + sourcePort + " " + destinationIp + " " + destinationPort + " " + action + " " + direction + " " + protocol + " " + priority;
	}
}
