package infernobuster.client;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTable; 

import infernobuster.parser.Rule;

public class Table extends JPanel{
	private static final long serialVersionUID = 8804243421849192593L;
	
	JTable table; 
	
	public Table() {
		Model model = new Model(new ArrayList<Rule>()); 
		
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800,600));
		
		add(scrollPane);
	}

	public Model getModel() {
		return (Model) table.getModel();
	}
}
