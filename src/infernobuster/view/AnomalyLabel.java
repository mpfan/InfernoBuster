package infernobuster.view;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import infernobuster.model.Anomaly;

public class AnomalyLabel extends JPanel {
	private static final long serialVersionUID = 7116346537720155498L;
	
	private JLabel label;
	
	public AnomalyLabel(Anomaly anomaly, String initialText) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel badge = new JPanel();
		badge.setBackground(ViewHelper.getColor(anomaly));
		
		label = new JLabel(initialText);
		
		add(badge);
		add(label);
	}
	
	public void setText(String text) {
		label.setText(text);
	}
}
