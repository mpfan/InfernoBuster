package infernobuster.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DropdownCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 5930949249161732997L;
	
	public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof String) {
            setText((String) value);
        }
         
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(Color.WHITE);
        }
         
        return this;
    }
}
