package infernobuster.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import infernobuster.model.Anomaly;

public class Badge extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 7694050302588028071L;
	
	ArrayList<JPanel> badges;
	public Badge() {
		setLayout(new GridLayout(1,8));
		
		badges = new ArrayList<JPanel>();
		for(int i = 0; i < 8; i++) {
			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			badges.add(panel);
			add(panel);
		}
	}
	
	private void resetBadge() {
		for(JPanel badge : badges) {
			badge.setBackground(Color.WHITE);
		}
	}
	
	private Color getColor(Anomaly anomaly) {
		if(anomaly == null) return Color.WHITE;
		
		switch(anomaly) {
			case REDUNDANCY:
				return Color.BLUE;
			case INCONSISTENCY:
				return Color.CYAN;
			case SHADOWING:
				return Color.DARK_GRAY;
			case DOWN_REDUNDANT:
				return Color.GRAY;
			case GENERALIZIATION:
				return Color.GREEN;
			case UP_REDUNDANT:
				return Color.LIGHT_GRAY;
			case PARTIAL_REDUNDANCY:
				return Color.MAGENTA;
			case CORRELATION:
				return Color.ORANGE;
			default:
				return Color.WHITE;
		}
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        ArrayList<Anomaly> anomalies = (ArrayList<Anomaly>) value;
        
        resetBadge();
        
        for(int i = 0; i < anomalies.size(); i++) {
        	badges.get(i).setBackground(getColor(anomalies.get(i)));
        }
        
        return this;
    }
}
