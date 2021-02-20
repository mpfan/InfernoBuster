package infernobuster.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class DropdownCellEditor<T> extends AbstractCellEditor implements TableCellEditor, ActionListener {
	private static final long serialVersionUID = 5521968001809397555L;
	
	private T value;
    private T[] values;
     
    public DropdownCellEditor(T[] values) {
        this.values = values;
    }
     
    @Override
    public Object getCellEditorValue() {
        return this.value;
    }
 
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
    	this.value = (T) value;
         
        JComboBox<T> dropdown = new JComboBox<T>();
         
        for (int i = 0; i < values.length; i++) {
        	dropdown.addItem(values[i]);
        }
         
        dropdown.setSelectedItem(value);
        dropdown.addActionListener(this);
         
        if (isSelected) {
            dropdown.setBackground(table.getSelectionBackground());
        } else {
        	dropdown.setBackground(Color.WHITE);
        }
         
        return dropdown;
    }
 
    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<T> comboCountry = (JComboBox<T>) event.getSource();
        this.value = (T) comboCountry.getSelectedItem();
    }
}
