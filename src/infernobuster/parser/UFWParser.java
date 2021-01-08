package infernobuster.parser;

import java.util.ArrayList;


/**
 * Parse rule-tuple entries from UFW config file extract. The parsing looks for a ### RULES ### starting delimiter and ### END RULES ### ending delimiter in the ufw rule file.
 * 
 * The rule tuples exist in the following format: 
 * 
 * ### tuple ### "Action" "protocol" "dport" "dAddr" "Sport" "SAddr" "Dir"
 * 
 * @author Souheil
 *
 */
public class UFWParser extends Parser {
	
	public UFWParser() {
	}

	@Override
	public ArrayList<Rule> parse(ArrayList<String> fileContents) {
		ArrayList<Rule> ruleList = new ArrayList<Rule>();
		int start_of_rules = 0;
		int end_of_rules = 0;

		for (String s : fileContents){
			if (s.equals("### RULES ###")) {
				start_of_rules = fileContents.indexOf(s);
			} else if (s.equals("### END RULES ###")) {
				end_of_rules = fileContents.indexOf(s);
				break;
			}
		}
		
		int priority = 0;
		for (int i = start_of_rules; i < end_of_rules; i++) {
			String line = fileContents.get(i);
			if (line.isEmpty()) {
				continue;
			} else {
				String[] lineArr = line.split(" ");
				if (lineArr[1].equals("tuple")) {
					
					String actionStr = lineArr[3];
					Action action = null;
					if(actionStr.equalsIgnoreCase("deny")) {
						action = Action.DENY;
					} else if(actionStr.equalsIgnoreCase("allow")) {
						action = Action.ALLOW;
					}
					
					String protocolStr = lineArr[4];
					Protocol protocol = null;
					if(protocolStr.equalsIgnoreCase("tcp")) {
						protocol = Protocol.TCP;
					} else if(protocolStr.equalsIgnoreCase("udp")) {
						protocol = Protocol.UDP;
					} else if(protocolStr.equalsIgnoreCase("any")) {
						protocol = Protocol.ANY;
					}
					
					int destinationPort = -2;
					String dport = lineArr[5];
					if(dport.equalsIgnoreCase("any")) {
						destinationPort = Rule.ANY;
					} else {
						destinationPort = Integer.parseInt(dport);
					}
					String destinationIp = lineArr[6];
					
					int sourcePort = -2;
					String sport = lineArr[7];
					if(sport.equalsIgnoreCase("any")) {
						sourcePort = Rule.ANY;
					} else {
						sourcePort = Integer.parseInt(sport);
					}
					String sourceIp = lineArr[8];
					
					String directionStr = lineArr[9];
					Direction direction = null;
					if(directionStr.equalsIgnoreCase("in")) {
						direction = Direction.IN;
					} else if(directionStr.equalsIgnoreCase("out")) {
						direction = Direction.OUT;
					}
					
					Rule r = new Rule(sourceIp, destinationIp, sourcePort, destinationPort, action, direction, protocol, priority);
					ruleList.add(r);
					
					priority++;
				}
					
			}
		}
		
		return ruleList;
	}


}
