package infernobuster.model;

import java.util.ArrayList;

public class IpTableParser extends Parser{
	public IpTableParser() {}

	public ArrayList<Rule> parse(ArrayList<String> content) throws ParserException {
//		try {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		
		int priority = 0;
		
		for(String line : content) {
			String[] tokens = line.split("\\s+");
			if(tokens.length < 4 || line.startsWith("#")) continue;
			
			// Inbound or Outbound
			String sourceIp = find(tokens, "-s");
			String destinationIp = find(tokens, "-d");
			
			if(sourceIp == null || destinationIp == null) {
				throw new ParserException("Malformed file");
			}
			
			int sourcePort = -2;
			String sport = find(tokens, "--sport");
			
			if(sport == null) {
				throw new ParserException("Malformed file");
			}
			
			if(sport.equalsIgnoreCase("any")) {
				sourcePort = Rule.ANY;
			} else {
				sourcePort = Integer.parseInt(sport);
			}
			
			int destinationPort = -2;
			String dport = find(tokens, "--dport");
			
			if(dport == null) {
				throw new ParserException("Malformed file");
			}
			
			if(dport.equalsIgnoreCase("any")) {
				destinationPort = Rule.ANY;
			} else {
				destinationPort = Integer.parseInt(dport);
			}
			
			String protocolStr = find(tokens, "-p");
			
			if(protocolStr == null) {
				throw new ParserException("Malformed file");
			}
			
			Protocol protocol = null;
			if(protocolStr.equalsIgnoreCase("tcp")) {
				protocol = Protocol.TCP;
			} else if(protocolStr.equalsIgnoreCase("udp")) {
				protocol = Protocol.UDP;
			} else if(protocolStr.equalsIgnoreCase("any")) {
				protocol = Protocol.ANY;
			}
			
			String actionStr = find(tokens, "-j");
			
			if(actionStr == null) {
				throw new ParserException("Malformed file");
			}
			
			Action action = null;
			if(actionStr.equalsIgnoreCase("DROP")) {
				action = Action.DENY;
			} else if(actionStr.equalsIgnoreCase("ACCEPT")) {
				action = Action.ALLOW;
			}
			String directionStr = find(tokens, "-A");
			
			if(directionStr == null) {
				throw new ParserException("Malformed file");
			}
			
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
//		} catch (Exception e){
//			System.err.println("Caught IOException: " + e.getMessage());
//			return null;
//		}
	}

	private String find(String[] tokens, String token) {
		for(int i = 0; i < tokens.length; i++) {
			if(tokens[i].equalsIgnoreCase(token)) {
				return tokens[++i];
			}
		}
		
		return null;
	}

	@Override
	public String export(ArrayList<Rule> rules) {
		StringBuilder sb = new StringBuilder();
		for(Rule rule : rules) {
			String direction = rule.getDirection() == Direction.IN ? "INPUT" : "OUTPUT"; 
			String action = rule.getAction() == Action.ALLOW ? "ACCEPT" : "DROP";
			String line = "-A " + direction + " -s " + rule.getSourceIp() + " -d " + rule.getDestinationIp() + " -p " + 
			rule.getProtocol().toString().toLowerCase() + " --sport "+ rule.getSourcePort() + " --dport " + rule.getDestinationPort() +
			" -j " + action;
			
			sb.append(line + "\n");
 		}
		return sb.toString();
	}
}
