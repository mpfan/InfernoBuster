package infernobuster.view;

import java.awt.FlowLayout;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import infernobuster.model.Action;
import infernobuster.model.Direction;
import infernobuster.model.Protocol;
import infernobuster.model.Rule;

public class RuleInputDialog extends JPanel {
	private static final long serialVersionUID = 3196130090235874276L;
	
	private Field priority;
	private Field sourceIp;
	private Field sourcePort;
	private Field destinationIp;
	private Field destinationPort;
	private DropDown action;
	private DropDown direction;
	private DropDown protocol;
	
	public RuleInputDialog() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel upperPanel = new JPanel();
		JPanel lowerPanel = new JPanel();
		
		
		priority = new Field("Priority");
		sourceIp = new Field("Source IP");
		sourcePort = new Field("Source Port");
		destinationIp = new Field("Destination IP");
		destinationPort = new Field("Destination Port");
		action = new DropDown("Action", Action.getAll());
		direction = new DropDown("Direction", Direction.getAll());
		protocol = new DropDown("Protocol", Protocol.getAll());
		
		upperPanel.add(priority);
		upperPanel.add(sourceIp);
		upperPanel.add(sourcePort);
		upperPanel.add(destinationIp);
		upperPanel.add(destinationPort);
		
		
		lowerPanel.add(action);
		lowerPanel.add(direction);
		lowerPanel.add(protocol);
		
		add(upperPanel);
		add(lowerPanel);
	}
	
	public Rule showDialog() {		
		int result = -1;
		Rule rule = null;
		
		do {
			result = JOptionPane.showConfirmDialog(null, this, "Creating new rule", JOptionPane.OK_CANCEL_OPTION);
			
			if(result == JOptionPane.OK_OPTION) {
				rule = getRule();
			}
			
		} while(result != JOptionPane.CANCEL_OPTION && result != JOptionPane.CLOSED_OPTION && result != JOptionPane.OK_OPTION);
		
		return rule;
	}
	
	private Rule getRule() {
		// Handle any for port
		int sport = -2;
		int dport = -2;
		
		if(sourcePort.getValue().isEmpty() || sourceIp.getValue().isEmpty() || destinationPort.getValue().isEmpty() || destinationIp.getValue().isEmpty()
				|| priority.getValue().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Missing Fields","Fields cannot be empty", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if(sourcePort.getValue().equalsIgnoreCase("any")) {
			sport = -1;
		} else {
			sport = Integer.parseInt(sourcePort.getValue());
		}
		
		if(destinationPort.getValue().equalsIgnoreCase("any")) {
			dport = -1;
		} else {
			dport = Integer.parseInt(destinationPort.getValue());
		}
		
		// Field validation
		if((sport < -1 || sport > 65535) || (dport < -1 || dport > 65535)) {
			JOptionPane.showMessageDialog(null, "Port must be between 0 and 65535", "Invalid Port", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		
		if(!(checkIpFormat(sourceIp.getValue()) && checkIpFormat(destinationIp.getValue()))) {
			JOptionPane.showMessageDialog(null, "Ip address must be in the format: [0-255].[0-255].[0-255].[0-255]/[0-32]", "Invalid IP", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		Rule rule = new Rule(sourceIp.getValue(), destinationIp.getValue(), sport, 
				dport, Action.fromString(action.getValue()), Direction.fromString(direction.getValue())
				,Protocol.fromString(protocol.getValue()), Integer.parseInt(priority.getValue()));
		
		
		return rule;
	}
	
	private boolean checkIpFormat(String ip) {
		Pattern pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(?:\\.(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}(?:\\/[0-2]\\d|\\/3[0-2])?$");
		return pattern.matcher(ip).find();
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
	
	private class DropDown extends JPanel {
		private static final long serialVersionUID = -6028694039395664734L;
		
		private String name;
		private JComboBox<String> dropDown;
		
		public DropDown(String name, String[] options) {
			this.name = name;
			dropDown = new JComboBox<String>(options);
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			add(new JLabel(this.name));
			add(dropDown);
		}
		
		public String getValue() {
			return dropDown.getSelectedItem().toString();
		}
	}
}
