package infernobuster.parser;

import java.util.ArrayList;

public class IpTableParser extends Parser{
	public IpTableParser() {}

	public ArrayList<Rule> parse(String filename) {
		ArrayList<String> file = read(filename);
		ArrayList<Rule> rules = new ArrayList<Rule>();
		
		int priority = 0;
		
		for(String line : file) {
			String[] tokens = line.split("\\s+");
			if(tokens.length < 4 || line.startsWith("#")) continue;
			
			// Inbound or Outbound
			String sourceIp = find(tokens, "-s");
			String destinationIp = find(tokens, "-d");
			int sourcePort = Integer.parseInt(find(tokens, "--sport"));
			int destinationPort = Integer.parseInt(find(tokens, "--dport"));
			String protocolStr = find(tokens, "-p");
			Protocol protocol = null;
			if(protocolStr.equalsIgnoreCase("tcp")) {
				protocol = Protocol.TCP;
			} else if(protocolStr.equalsIgnoreCase("udp")) {
				protocol = Protocol.UDP;
			}
			String actionStr = find(tokens, "-j");
			Action action = null;
			if(actionStr.equalsIgnoreCase("DROP")) {
				action = Action.DENY;
			} else if(actionStr.equalsIgnoreCase("ACCEPT")) {
				action = Action.ALLOW;
			}
			String directionStr = find(tokens, "-A");
			Direction direction = null;
			if(directionStr.equalsIgnoreCase("INPUT")) {
				direction = Direction.IN;
			} else if(directionStr.equalsIgnoreCase("OUTPUT")) {
				direction = Direction.OUT;
			}
			
			rules.add(new Rule(sourceIp, destinationIp, sourcePort, destinationPort, action, direction, protocol, priority));
			
			priority++;
		}
		
		return rules;
	}

	private String find(String[] tokens, String token) {
		for(int i = 0; i < tokens.length; i++) {
			if(tokens[i].equalsIgnoreCase(token)) {
				return tokens[++i];
			}
		}
		
		return null;
	}
}
