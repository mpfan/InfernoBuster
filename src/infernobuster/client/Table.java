package infernobuster.client;

import java.awt.Color;
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
		setBackground(Color.WHITE);
		
		Model model = new Model(new ArrayList<Rule>()); 
		
		table = new JTable(model);
		table.setBackground(Color.WHITE);
		
		for (int i = 0; i < Model.NUM_OF_COL; i++) {
		     table.getColumnModel().getColumn(i).setCellRenderer(model.getCustomCellRenderer());
		 }
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800,600));
		scrollPane.setBackground(Color.WHITE);
		
		add(scrollPane);
	}

	public Model getModel() {
		return (Model) table.getModel();
	}
}
