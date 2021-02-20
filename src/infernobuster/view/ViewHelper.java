package infernobuster.view;

import java.awt.Color;

import infernobuster.model.Anomaly;

public class ViewHelper {
	public static Color getColor(Anomaly anomaly) {
		if(anomaly == null) return Color.WHITE;
		
		switch(anomaly) {
			case REDUNDANCY:
				return new Color(128, 191, 255);
			case INCONSISTENCY:
				return new Color(255, 128, 128);
			case SHADOWING:
				return Color.DARK_GRAY;
			case DOWN_REDUNDANT:
				return Color.GRAY;
			case GENERALIZIATION:
				return new Color(128, 255, 170);
			case UP_REDUNDANT:
				return new Color(128, 223, 255);
			case PARTIAL_REDUNDANCY:
				return new Color(153, 153, 255);
			case CORRELATION:
				return new Color(255, 179, 153);
			default:
				return Color.WHITE;
		}
	}
}
