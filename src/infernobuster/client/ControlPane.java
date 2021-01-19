package infernobuster.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import infernobuster.detector.Anomaly;
import infernobuster.detector.Detector;
import infernobuster.parser.Action;
import infernobuster.parser.Direction;
import infernobuster.parser.IpTableParser;
import infernobuster.parser.Parser;
import infernobuster.parser.ParserException;
import infernobuster.parser.Protocol;
import infernobuster.parser.Rule;
import infernobuster.parser.UFWParser;

public class ControlPane extends JPanel {
	private static final long serialVersionUID = 1702526798477578409L;
	
	Table table;
	HashMap<Anomaly,JLabel> labels;
	
	public ControlPane() {
		setPreferredSize(new Dimension(1000,600));
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		
		table = new Table();
		
		add(table, BorderLayout.WEST);
		
		JButton button = new JButton("Load");
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(new BorderLayout());
		
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new GridLayout(2,4));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
		
		for (Anomaly anomaly : Anomaly.values()) {
            JCheckBox checkBox = new JCheckBox(anomaly.toString());
            
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
		
		toolPanel.add(filterPanel, BorderLayout.WEST);
		toolPanel.add(statsPanel, BorderLayout.CENTER);
		
		add(toolPanel, BorderLayout.NORTH);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		
		button.setPreferredSize(new Dimension(130,30));
		
		buttonPanel.add(button);
		
		add(buttonPanel, BorderLayout.CENTER);
	}
	
	private void openFile() {
		JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
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

    		Parser parser = new IpTableParser();;

    		ArrayList<Rule> rules = null;
    		try {
    			rules = parser.parse(content);
    		} catch (ParserException e) {
    			System.out.println(e.getMessage());
    			System.exit(1);
    		}
    		table.getModel().setRules(rules);
    		
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
}
