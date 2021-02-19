package infernobuster.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;

import infernobuster.view.Badge;

public class Model extends AbstractTableModel {
	private static final long serialVersionUID = 6655916175935685147L;
	
	public static int PRIORITY_INDEX = 0;
	public static int SOURCE_IP_INDEX = 1;
	public static int SOURCE_PORT_INDEX = 2;
	public static int DESTINTATION_IP_INDEX = 3;
	public static int DESTINTATION_PORT_INDEX = 4;
	public static int DIRECTION_INDEX = 5;
	public static int PROTOCOL_INDEX = 6;
	public static int ACTION_INDEX = 7;
	public static int BADGE_INDEX = 8;
	public static int NUM_OF_COL = 9;
	
	private ArrayList<Rule> rules;
	private ArrayList<Anomaly> filter;
	private Detector detector;
	private DetectionResult result;
	private Parser parser;
	
	private ArrayList<RuleListener> listeners;
	
	private int focusedRule;
	
	public Model() {
		rules = new ArrayList<Rule>();
		filter = new ArrayList<Anomaly>();
		detector = new Detector();
		listeners = new ArrayList<RuleListener>();
		
		focusedRule = -1;
	}
	
	public String export() {
		return parser.export(rules);
	}
	
	public void parse(ArrayList<String> content, FWType type) throws ParserException {
		if (type == FWType.IPTABLES) {
    		parser = new IpTableParser();
        } else if (type == FWType.UFW) {
    		parser = new UFWParser();
        }

		rules = parser.parse(content);
		setRules(rules);
	}
	
	public void addRuleListener(RuleListener listener) {
		listeners.add(listener);
	}
	
	public void setFocusedRule(int row) {
		focusedRule = row != -1 ? rules.get(row).getId() : -1;
	}
	
	public void notifyAnomalyDetected() {
		for(RuleListener listener : listeners) {
			listener.anomalyDetected(this);
		}
	}
	
	public void notifyRuleModified() {
		for(RuleListener listener : listeners) {
			listener.ruleModified(this);
		}
	}
	
	public HashMap<Anomaly, Integer> getNumOfAnomalies() {
		return result.getNumOfAnomalies();
	}
	
	public void addFilter(Anomaly anomaly) {
		filter.add(anomaly);
	}
	
	public void removeFilter(Anomaly anomaly) {
		for(Anomaly a: filter) {
			if(a.toString().equalsIgnoreCase(anomaly.toString())) {
				filter.remove(a);
				break;
			}
		}
	}
	
	public RowFilter<Model,Integer> getFilter() {
		RowFilter<Model,Integer> anomalyFilter = new RowFilter<Model,Integer>() {
			public boolean include(Entry<? extends Model, ? extends Integer> entry) {
			    Rule rule = rules.get(entry.getIdentifier());
			    ArrayList<Integer> conflictingRules = result.getConflictedRules(rule.getId());
			    
			    
			    if(filter.isEmpty()) {
			    	if(focusedRule == -1) {
			    		return true;
			    	} else if(focusedRule == rule.getId() || (conflictingRules != null && conflictingRules.contains(rule.getId()))) {
			    		return true;
			    	}
			    	
			    } else if (isInFilter(rule)) {
			    	if(focusedRule == -1) {
			    		return true;
			    	} else if(focusedRule == rule.getId() || (conflictingRules != null && conflictingRules.contains(rule.getId()))){
			    		return true;
			    	}
				}
			    
			    return false;
			 }
		};
		
		return anomalyFilter;
	}
	
	public int getRowCount() {
		return rules.size();
	}

	public int getColumnCount() {
		return NUM_OF_COL;
	}

	public String getColumnName(int columnIndex) {
		if(columnIndex == SOURCE_IP_INDEX) {
			return "Source IP";
		} else if(columnIndex == DESTINTATION_IP_INDEX) {
			return "Destination IP";
		} else if(columnIndex == SOURCE_PORT_INDEX) {
			return "Source Port";
		} else if(columnIndex == DESTINTATION_PORT_INDEX) {
			return "Destination Port";
		} else if(columnIndex == ACTION_INDEX) {
			return "Action";
		} else if(columnIndex == DIRECTION_INDEX) {
			return "Direction";
		} else if(columnIndex == PROTOCOL_INDEX) {
			return "Protocol";
		} else if(columnIndex == PRIORITY_INDEX) {
			return "Priority";
		} else if(columnIndex == BADGE_INDEX) {
			return "Anomalies";
		} 
		return null;
	}

	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == BADGE_INDEX) {
			return ArrayList.class;
		} else if(columnIndex == PRIORITY_INDEX) {
			return Integer.class;
		}
		
		return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex != BADGE_INDEX;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Rule rule = rules.get(rowIndex);
		
		if(columnIndex == SOURCE_IP_INDEX) {
			return rule.getSourceIp();
		} else if(columnIndex == DESTINTATION_IP_INDEX) {
			return rule.getDestinationIp();
		} else if(columnIndex == SOURCE_PORT_INDEX) {
			return rule.getSourcePort()
					== -1 ? "ANY" : rule.getSourcePort();
		} else if(columnIndex == DESTINTATION_PORT_INDEX) {
			return rule.getDestinationPort() 
					== -1 ? "ANY" : rule.getDestinationPort();
		} else if(columnIndex == ACTION_INDEX) {
			return rule.getAction().toString();
		} else if(columnIndex == DIRECTION_INDEX) {
			return rule.getDirection().toString();
		} else if(columnIndex == PROTOCOL_INDEX) {
			return rule.getProtocol().toString();
		} else if(columnIndex == PRIORITY_INDEX) {
			return rule.getPriority();
		} else if(columnIndex == BADGE_INDEX){
			return result.getTypesOfAnomaly(rule);
		} else {
			return null;
		}
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Rule rule = rules.get(rowIndex);
		String value = (String) aValue;
		
		if(columnIndex == SOURCE_IP_INDEX) {
			rule.setSourceIp(value);
		} else if(columnIndex == DESTINTATION_IP_INDEX) {
			rule.setDestinationIp(value);
		} else if(columnIndex == SOURCE_PORT_INDEX) {
			rule.setSourcePort(Integer.parseInt(value));
		} else if(columnIndex == DESTINTATION_PORT_INDEX) {
			rule.setDestinationPort(Integer.parseInt(value));
		} else if(columnIndex == PRIORITY_INDEX) {
			rule.setPriority(Integer.parseInt(value));
		}
		
		try {
			if(columnIndex == ACTION_INDEX) {
				rule.setAction(Action.fromString(value));
			} else if(columnIndex == DIRECTION_INDEX) {
				rule.setDirection(Direction.fromString(value));
			} else if(columnIndex == PROTOCOL_INDEX) {
				rule.setProtocol(Protocol.fromString(value));
			} 
		} catch(Exception e) {
		}
		
		result = detector.detect(rules);
		fireTableDataChanged();
		notifyAnomalyDetected();
	}
	

	public ArrayList<Rule> getRules() {
		return rules;
	}

	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
		result = detector.detect(rules);
		fireTableDataChanged();
		notifyAnomalyDetected();
		notifyRuleModified();
	}
	
	public void add(Rule rule) {
		rules.add(rule);
        result = detector.detect(rules);
        fireTableRowsInserted(rules.size() - 1, rules.size() - 1);
        notifyAnomalyDetected();
        notifyRuleModified();
    }
	
	public void remove(int i) {
		rules.remove(i);
		result = detector.detect(rules);
		fireTableDataChanged();
		notifyAnomalyDetected();
        notifyRuleModified();
	}
	
	private boolean isInFilter(Rule rule) {
		ArrayList<Anomaly> ruleAnomalies = result.getTypesOfAnomaly(rule);
		for(Anomaly ra : ruleAnomalies) {
			if(filter.contains(ra)) {
				return true;
			}
		}
		
		return false;
	}
}
