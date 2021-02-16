package infernobuster.view;

import java.awt.Color;

import infernobuster.model.Anomaly;

public class ViewHelper {
	public static Color getColor(Anomaly anomaly) {
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
}
