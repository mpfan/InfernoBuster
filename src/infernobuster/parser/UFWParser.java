package infernobuster.parser;

import java.util.ArrayList;

import org.graalvm.compiler.phases.common.DeadCodeEliminationPhase_OptionDescriptors;

/**
 * Parse rule-tuple entries from UFW config file extract. The parsing looks for
 * a ### RULES ### starting delimiter and ### END RULES ### ending delimiter in
 * the ufw rule file.
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
	public ArrayList<Rule> parse(ArrayList<String> fileContents) throws ParserException {
		// try {
		ArrayList<Rule> ruleList = new ArrayList<Rule>();
		int start_of_rules = 0;
		int end_of_rules = 0;

		for (String s : fileContents) {
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

					if (actionStr == null || actionStr.isEmpty()) {
						throw new ParserException("Malformed file");
					}

					Action action = null;
					if (actionStr.equalsIgnoreCase("deny")) {
						action = Action.DENY;
					} else if (actionStr.equalsIgnoreCase("allow")) {
						action = Action.ALLOW;
					}

					String protocolStr = lineArr[4];

					if (protocolStr == null || protocolStr.isEmpty()) {
						throw new ParserException("Malformed file");
					}

					Protocol protocol = null;
					if (protocolStr.equalsIgnoreCase("tcp")) {
						protocol = Protocol.TCP;
					} else if (protocolStr.equalsIgnoreCase("udp")) {
						protocol = Protocol.UDP;
					} else if (protocolStr.equalsIgnoreCase("any")) {
						protocol = Protocol.ANY;
					}

					int destinationPort = -2;
					String dport = lineArr[5];

					if (dport == null || dport.isEmpty()) {
						throw new ParserException("Malformed file");
					}

					if (dport.equalsIgnoreCase("any")) {
						destinationPort = Rule.ANY;
					} else {
						destinationPort = Integer.parseInt(dport);
					}
					String destinationIp = lineArr[6];

					int sourcePort = -2;
					String sport = lineArr[7];

					if (sport == null || sport.isEmpty()) {
						throw new ParserException("Malformed file");
					}

					if (sport.equalsIgnoreCase("any")) {
						sourcePort = Rule.ANY;
					} else {
						sourcePort = Integer.parseInt(sport);
					}
					String sourceIp = lineArr[8];

					String directionStr = lineArr[9];

					if (directionStr == null || directionStr.isEmpty()) {
						throw new ParserException("Malformed file");
					}

					Direction direction = null;
					if (directionStr.equalsIgnoreCase("in")) {
						direction = Direction.IN;
					} else if (directionStr.equalsIgnoreCase("out")) {
						direction = Direction.OUT;
					}

					Rule r = new Rule(sourceIp, destinationIp, sourcePort, destinationPort, action, direction, protocol,
							priority);
					ruleList.add(r);

					priority++;
				}

			}
		}

		return ruleList;
		// } catch (Exception e) {
		// System.err.println("Caught IOException: " + e.getMessage());
		// return null;
		// }

	}

	/**
	 * Rule format: -A ufw-user-<in/out> -p <protocol> -d <destination IP addr> --dport <destination port> -s <source IP addr> --sport <source port> -j <action>
	 * Tuple format: ### tuple ### "Action" "protocol" "dport" "dAddr" "Sport" "SAddr" "Dir"
	 * 
	 */
	@Override
	public String export(ArrayList<Rule> rules) {
		StringBuilder sb = new StringBuilder();

		sb.append("### RULES ###\n\n");

		for (Rule rule : rules) {
			String tupleStr = "### tuple ### " 
								+ (rule.getAction() == Action.ALLOW ? "allow" : "deny") 
								+ " " +  rule.getProtocol().toString().toLowerCase()  
								+ " " + (rule.getDestinationPort() == -1 ? "any" : rule.getDestinationPort())
								+ " " +  rule.getDestinationIp()
								+ " " + (rule.getSourcePort() == -1 ? "any" : rule.getSourcePort())
								+ " " +  rule.getSourceIp()
								+ " " +  rule.getDirection().toString().toLowerCase()
								+ "\n",
					ruleStr, 
					proto1, 
					proto2 = null, 
					srcIP = " -s " + rule.getSourceIp(), 
					destIP = " -d " + rule.getDestinationIp(), 
					srcPort = "",
					destPort = "",
					direction = "-A " + (rule.getDirection() == Direction.IN ? "ufw-user-input" : "ufw-user-output"),
					action = " -j " + (rule.getAction() == Action.ALLOW ? "ACCEPT" : "DROP");

			// handle "ANY" port case
			if (rule.getDestinationPort() != -1) {
				destPort = " --dport " + rule.getDestinationPort();
			}
			if (rule.getSourcePort() != -1) {
				srcPort = " --sport " + rule.getSourcePort();
			}

			// if protocol is any, 2 rows are needed in the UFW file (TCP -> UDP)
			if (rule.getProtocol() == Protocol.ANY) {
				proto2 = " -p udp";
				proto1 = " -p tcp";
			} else {
				proto1 = " -p " + rule.getProtocol();
			}

			ruleStr = direction + proto1 + destIP + destPort + srcIP + srcPort + action;
			
			sb.append(tupleStr);

			if (proto2 != null) {																	// Case: Protocol == "ANY"
				if (rule.getDestinationPort() != -1 || rule.getSourcePort() != -1) {				// Case: !srcPort || !destPort == "ANY"
					sb.append(ruleStr + "\n");
					ruleStr = direction + proto2 + destIP + destPort + srcIP + srcPort + action;
					sb.append(ruleStr + "\n");
				} else {																			// Case: srcPort && destPort == "ANY"
					ruleStr = direction  + destIP + destPort + srcIP + srcPort + action;
					sb.append(ruleStr + "\n");
				}
			} else {																				// Case: Protocol != "ANY"
				sb.append(ruleStr + "\n");
			}
			sb.append("\n");
		}

		sb.append("### END RULES ###");
		return sb.toString();
	}

}
