package infernobuster.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import infernobuster.model.Action;
import infernobuster.model.Direction;
import infernobuster.model.Model;
import infernobuster.model.Protocol;

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
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		sorter = new TableRowSorter<Model>(model);
		sorter.setRowFilter(model.getFilter());
		sorter.setComparator(Model.BADGE_INDEX, new BadgeSorter());
		
		// Disable sorters for columns 1 - 7
		for(int i = Model.SOURCE_IP_INDEX; i <= Model.ACTION_INDEX; i++) {
			sorter.setSortable(i, false);
		}
		
		table.setRowSorter(sorter);
		// Custom renderers
		table.getColumnModel().getColumn(Model.DIRECTION_INDEX).setCellRenderer(new DropdownCellRenderer());
		table.getColumnModel().getColumn(Model.ACTION_INDEX).setCellRenderer(new DropdownCellRenderer());
		table.getColumnModel().getColumn(Model.PROTOCOL_INDEX).setCellRenderer(new DropdownCellRenderer());
		table.getColumnModel().getColumn(Model.BADGE_INDEX).setCellRenderer(new Badge());
		
		// Cutsom editor
		table.getColumnModel().getColumn(Model.DIRECTION_INDEX).setCellEditor(new DropdownCellEditor<String>(Direction.getAll()));
		table.getColumnModel().getColumn(Model.ACTION_INDEX).setCellEditor(new DropdownCellEditor<String>(Action.getAll()));
		table.getColumnModel().getColumn(Model.PROTOCOL_INDEX).setCellEditor(new DropdownCellEditor<String>(Protocol.getAll()));
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(1000,600));
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
