package infernobuster.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;


import infernobuster.detector.Anomaly;
import infernobuster.mvc.RuleListener;
import infernobuster.parser.IpTableParser;
import infernobuster.parser.Parser;
import infernobuster.parser.ParserException;
import infernobuster.parser.Rule;
import infernobuster.parser.UFWParser;

public class ControlPane extends JPanel implements RuleListener {
	private static final long serialVersionUID = 1702526798477578409L;
	
	private Table table;
	private HashMap<Anomaly,JLabel> labels;
	private HashMap<Anomaly,JCheckBox> filters;
	private JButton add;
	private JButton remove;
	private JButton focus;
	private JButton unfocus;
	
	public ControlPane() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		
		JMenuItem open = new JMenuItem("Open File..");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FWType type = selectType();
				if (type != null) {
					openFile(type);
				}
			}
		});
		
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(new BorderLayout());
		
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new GridLayout(2,4));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
		
		filters = new HashMap<Anomaly,JCheckBox>();
		for (Anomaly anomaly : Anomaly.values()) {
            JCheckBox checkBox = new JCheckBox(anomaly.toString());
            checkBox.setEnabled(false);
            
            filters.put(anomaly, checkBox);
            filterPanel.add(checkBox);
            checkBox.addActionListener(new CheckBoxListener());
        }
		
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new GridLayout(4,2));
		statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
		
		labels = new HashMap<Anomaly,JLabel>();
		for (Anomaly anomaly : Anomaly.values()) {
            JLabel label = new JLabel(anomaly.toString() + ": 0");
            
            statsPanel.add(label);
            labels.put(anomaly, label);
        }
		
		JPanel ruleControlPanel = new JPanel();
		remove = new JButton("Remove");
		remove.setEnabled(false);
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.getModel().remove(table.getSelectedRow());
			}
		});
		
		add = new JButton("Add");
		add.setEnabled(false);
		
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RuleInputDialog dialog = new RuleInputDialog();
				
				dialog.showDialog();
				
				Rule rule = dialog.getRule();
				
				table.getModel().add(rule);
			}
		});
		
		focus = new JButton("Focus");
		focus.setEnabled(false);
		focus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.getModel().setFocusedRule(table.getSelectedRow());
				table.setFilter();
				unfocus.setEnabled(true);
			}
		});
		
		unfocus = new JButton("Unfocus");
		unfocus.setEnabled(false);
		unfocus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.getModel().setFocusedRule(-1);
				table.setFilter();
				unfocus.setEnabled(false);
			}
		});
		
		ruleControlPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		ruleControlPanel.setBorder(BorderFactory.createTitledBorder("Rule Control"));
		ruleControlPanel.add(add);
		ruleControlPanel.add(remove);
		ruleControlPanel.add(focus);
		ruleControlPanel.add(unfocus);
		
		toolPanel.add(filterPanel, BorderLayout.WEST);
		toolPanel.add(statsPanel, BorderLayout.CENTER);
		toolPanel.add(ruleControlPanel, BorderLayout.SOUTH);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		
		JMenuItem export = new JMenuItem("Export As");
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FWType type = selectType();
				if (type != null) {
					exportFile();
				}
			}
		});
		
		menu.add(open);
		menu.add(export);
		menuBar.add(menu);
		
		
		table = new Table();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		
		panel.add(table, BorderLayout.WEST);
		panel.add(toolPanel, BorderLayout.NORTH);
		
		table.getModel().addRuleListener(this);
	}
	
	/**
	 * 
	 */
	private void openFile(FWType type) {
		JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        	File file = chooser.getSelectedFile();
        	
        	BufferedReader br;
    		ArrayList<String> content = new ArrayList<String>();
            
            try {
    			br = new BufferedReader(new FileReader(file));
    			
    			String line;
    			while ((line = br.readLine()) != null) {
    				content.add(line);
    			}
    		} catch (FileNotFoundException e) {
    			System.out.println("File not found");
    			System.exit(1);
    		} catch (IOException e) {
    			System.out.println("Error reading file");
    			System.exit(1);
    		} 

            Parser parser = null;
            if (type == FWType.IPTABLES) {
        		parser = new IpTableParser();
            } else if (type == FWType.UFW) {
        		parser = new UFWParser();
            }

    		ArrayList<Rule> rules = null;
    		try {
    			rules = parser.parse(content);
    		} catch (ParserException e) {
    			JOptionPane.showMessageDialog(table, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    			System.out.println(e.getMessage());
    		}
    		table.getModel().setRules(rules);
    		add.setEnabled(true);
    		focus.setEnabled(true);
    		
        } else {
            // user changed their mind
        }
	}
	
	private class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBox checkBox = (JCheckBox) e.getSource();
			
			if(checkBox.isSelected()) {
				table.getModel().addFilter(Anomaly.fromString(checkBox.getText()));
			} else {
				table.getModel().removeFilter(Anomaly.fromString(checkBox.getText()));
			}
			
			table.setFilter();
		}
		
	}
	/**
	 *
	 */
	private void exportFile() {
		JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        	// To be done
        } else {
            // user changed their mind
        }
	}

	/**
	 * 
	 */
	private FWType selectType() {
		Object[] possibilities = {"UFW", "IPTables"};
		String s = (String)JOptionPane.showInputDialog(
		                    this, "Please select a firewall type:",
		                    "FireWall Selection",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null, possibilities,
		                    "UFW");	
		
		if (s != null) {
			return FWType.fromString(s);
		} 
		
		return null;
	}

	@Override
	public void anomalyDetected(Model model) {
		HashMap<Anomaly, Integer> stats = model.getNumOfAnomalies();
		for (HashMap.Entry<Anomaly, JLabel> entry : labels.entrySet()) {
			JLabel label = entry.getValue();
			
			label.setText(entry.getKey().toString() + ": " + stats.getOrDefault(entry.getKey(),0));
        }
	}

	@Override
	public void ruleModified(Model model) {
		HashMap<Anomaly, Integer> stats = model.getNumOfAnomalies();
		for (HashMap.Entry<Anomaly, JCheckBox> entry : filters.entrySet()) {
			entry.getValue().setEnabled(stats.getOrDefault(entry.getKey(), 0) > 0);
        }
		
		remove.setEnabled(model.getRules().size() > 0);
	}

	@Override
	public void ruleFocused(Model model) {
		
		
	}
	
}
