package infernobuster.client;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import infernobuster.parser.Action;
import infernobuster.parser.Direction;
import infernobuster.parser.Protocol;
import infernobuster.parser.Rule;

public class RuleInputDialog extends JPanel {
	private static final long serialVersionUID = 3196130090235874276L;
	
	private Field priority;
	private Field sourceIp;
	private Field sourcePort;
	private Field destinationIp;
	private Field destinationPort;
	private Field action;
	private Field direction;
	private Field protocol;
	
	public RuleInputDialog() {
		setLayout(new FlowLayout(FlowLayout.LEADING));
		
		priority = new Field("Priority");
		sourceIp = new Field("Source IP");
		sourcePort = new Field("Source Port");
		destinationIp = new Field("Destination IP");
		destinationPort = new Field("Destination Port");
		action = new Field("Action");
		direction = new Field("Direction");
		protocol = new Field("Protocol");
		
		add(priority);
		add(sourceIp);
		add(sourcePort);
		add(destinationIp);
		add(destinationPort);
		add(action);
		add(direction);
		add(protocol);
	}
	
	public void showDialog() {
		JOptionPane.showMessageDialog(null, this);
	}
	
	public Rule getRule() {
		Rule rule = new Rule(sourceIp.getValue(), destinationIp.getValue(), Integer.parseInt(sourcePort.getValue()), 
				Integer.parseInt(destinationPort.getValue()), Action.fromString(action.getValue()), Direction.fromString(direction.getValue())
				,Protocol.fromString(protocol.getValue()), Integer.parseInt(priority.getValue()));
		
		// Exception handling here
		
		return rule;
	}

	private class Field extends JPanel {
		private static final long serialVersionUID = 2502137756906477652L;
		
		private String name;
		private JTextField field;
		
		public Field(String name) {
			this.name = name;
			field = new JTextField();
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			add(new JLabel(this.name));
			add(field);
		}
		
		public String getValue() {
			return field.getText().strip();
		}
	}
}
