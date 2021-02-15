package infernobuster.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import infernobuster.model.Model;
import infernobuster.model.Rule;

public class Table extends JPanel{
	private static final long serialVersionUID = 8804243421849192593L;
	
	private JTable table; 
	private TableRowSorter<Model> sorter;
	private Model model;
	
	public Table(Model model) {
		this.model = model;
		
		setBackground(Color.WHITE);
		
		table = new JTable(model);
		table.setBackground(Color.WHITE);
		sorter = new TableRowSorter<Model>(model);
		table.setRowSorter(sorter);
		sorter.setRowFilter(model.getFilter());
		
		// The badges will be custom rendered
		table.getColumnModel().getColumn(Model.BADGE_INDEX).setCellRenderer(new Badge());
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800,600));
		scrollPane.setBackground(Color.WHITE);
		
		add(scrollPane);
	}
	
	public void setFilter() {
		sorter.setRowFilter(model.getFilter());
	}

	public Model getModel() {
		return (Model) table.getModel();
	}
	
	public int getSelectedRow() {
		return table.getSelectedRow();
	}
}
