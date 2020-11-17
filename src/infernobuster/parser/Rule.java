package infernobuster.parser;

import java.net.InetAddress;

public class Rule {
	private String sourceIp; // CIDR
	private String destinationIp; // CIDR
	private int sourcePort;
	private int destinationPort;
	private int priority;
	
	public Rule(String sourceIp, String destinationIp, int sourcePort, int destinationPort, int priority) {
		this.sourceIp = sourceIp;
		this.destinationIp = destinationIp;
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
		this.priority = priority;
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
	
	private void resolveRange(String ip) {
		String[] parsed = ip.split("/"); // Splits the address and the mask
		String startingIp = parsed[0];
		String[] endingIp = parsed[0].split(".");
		int mask = Integer.parseInt(parsed[1]);
		
		// Converting to byte
		
	}
}
