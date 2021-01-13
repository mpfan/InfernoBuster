package infernobuster.client;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import infernobuster.detector.Anomaly;
import infernobuster.detector.DetectionResult;
import infernobuster.detector.Detector;
import infernobuster.parser.Action;
import infernobuster.parser.Direction;
import infernobuster.parser.Protocol;
import infernobuster.parser.Rule;

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
	public static int NUM_OF_COL = 8;
	
	ArrayList<Rule> rules;
	Detector detector;
	DetectionResult result;
	
	public Model(ArrayList<Rule> rules) {
		this.rules = rules;
		detector = new Detector();
	}
	
	public int getRowCount() {
		return rules.size();
	}

	public int getColumnCount() {
		return Rule.NUM_OF_FIELD;
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
		} 
		return null;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Rule rule = rules.get(rowIndex);
		
		if(columnIndex == SOURCE_IP_INDEX) {
			return rule.getSourceIp();
		} else if(columnIndex == DESTINTATION_IP_INDEX) {
			return rule.getDestinationIp();
		} else if(columnIndex == SOURCE_PORT_INDEX) {
			return rule.getSourcePort();
		} else if(columnIndex == DESTINTATION_PORT_INDEX) {
			return rule.getDestinationPort();
		} else if(columnIndex == ACTION_INDEX) {
			return rule.getAction();
		} else if(columnIndex == DIRECTION_INDEX) {
			return rule.getDirection();
		} else if(columnIndex == PROTOCOL_INDEX) {
			return rule.getProtocol();
		} else if(columnIndex == PRIORITY_INDEX) {
			return rule.getPriority();
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
	}
	
	

	public ArrayList<Rule> getRules() {
		return rules;
	}

	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
		result = detector.detect(rules);
		fireTableDataChanged();
	}
	
	public void add(Rule rule) {
        rules.add(rule);
        result = detector.detect(rules);
        fireTableRowsInserted(rules.size() - 1, rules.size() - 1);
    }
	
	private Color getColor(Anomaly anomaly) {
		if(anomaly == null) return Color.WHITE;
		
		switch(anomaly) {
			case REDUNDANCY:
				return Color.BLUE;
			case INCONSISTENCY:
				return Color.CYAN;
			case SHADOWING:
				return Color.DARK_GRAY;
			case DOWN_REDUNDANT:
				return Color.GRAY;
			case GENERALIZIATION:
				return Color.GREEN;
			case UP_REDUNDANT:
				return Color.LIGHT_GRAY;
			case PARTIAL_REDUNDANCY:
				return Color.MAGENTA;
			case CORRELATION:
				return Color.ORANGE;
			default:
				return Color.WHITE;
		}
	}
	
	public CustomCellRenderer getCustomCellRenderer() {
		return new CustomCellRenderer();
	}
	
	private class CustomCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 7694050302588028071L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        Rule rule = rules.get(row);
	        Anomaly anomaly = result.getTypeOfAnomaly(rule);
	        
	        c.setBackground(getColor(anomaly));
	        
	        return c;
	    }
	}
}
