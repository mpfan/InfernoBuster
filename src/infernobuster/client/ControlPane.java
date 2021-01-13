package infernobuster.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

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
	public ControlPane() {
		setPreferredSize(new Dimension(1000,600));
		setLayout(new BorderLayout());
		
		table = new Table();
		
		add(table, BorderLayout.WEST);
		
		JButton button = new JButton("Load");
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		
		JPanel buttonPanel = new JPanel();
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
    		
    		Model model = table.getModel();
    		for(Rule r: rules) {
    			model.add(r);
    		}
    		
        } else {
            // user changed their mind
        }
	}
}
