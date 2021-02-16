package infernobuster.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import infernobuster.model.Anomaly;
import infernobuster.model.FWType;
import infernobuster.model.IpTableParser;
import infernobuster.model.Model;
import infernobuster.model.ParserException;
import infernobuster.model.Rule;
import infernobuster.model.UFWParser;
import infernobuster.view.ControlPane;
import infernobuster.view.RuleInputDialog;

public class Controller {
	private ControlPane view;
	private Model model;
	
	public Controller(ControlPane view, Model model) {
		this.view = view;
		this.model = model;
	}
	
	/**
	 * 
	 */
	public FWType selectType() {
		Object[] possibilities = {"UFW", "IPTables"};
		String s = (String)JOptionPane.showInputDialog(
		                    view, "Please select a firewall type:",
		                    "FireWall Selection",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null, possibilities,
		                    "UFW");	
		
		if (s != null) {
			return FWType.fromString(s);
		} 
		
		return null;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void openFile(FWType type) {
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

    		try {
    			model.parse(content, type);
    		} catch (ParserException e) {
    			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    			System.out.println(e.getMessage());
    		}
    		
    		view.getAdd().setEnabled(true);
    		view.getFocus().setEnabled(true);
    		view.getExport().setEnabled(true);
    		
        } else {
            // user changed their mind
        }
	}
	
	/**
	 *
	 */
	public void exportFile() {
		JFileChooser chooser = new JFileChooser();
       // optionally set chooser options ...
       if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
       	FileWriter fw;
			try {
				fw = new FileWriter(chooser.getSelectedFile());
				fw.write(model.export());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
           
       } else {
           // user changed their mind
       }
	}
	
	public void addRule() {
		RuleInputDialog dialog = new RuleInputDialog();
		
		Rule rule = dialog.showDialog();
		
		if(rule != null) {
			model.add(rule);
		}
	}
	
	public void removeRule() {
		model.remove(view.getTable().getSelectedRow());
	}
	
	public void focus() {
		model.setFocusedRule(view.getTable().getSelectedRow());
		view.getTable().setFilter();
		view.getUnfocus().setEnabled(true);
	}
	
	public void unfocus() {
		model.setFocusedRule(-1);
		view.getTable().setFilter();
		view.getUnfocus().setEnabled(false);
	}
	
	public void handleCheckBox(ActionEvent e) {
		JCheckBox checkBox = (JCheckBox) e.getSource();
		
		if(checkBox.isSelected()) {
			model.addFilter(Anomaly.fromString(checkBox.getText()));
		} else {
			model.removeFilter(Anomaly.fromString(checkBox.getText()));
		}
		
		view.getTable().setFilter();
	}
}
