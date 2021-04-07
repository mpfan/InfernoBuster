package infernobuster.view;

import java.util.ArrayList;
import java.util.Comparator;

import infernobuster.model.Anomaly;

public class BadgeSorter implements Comparator<ArrayList<Anomaly>>{
	public int compare(ArrayList<Anomaly> badge1, ArrayList<Anomaly> badge2) {
		if(badge1.size() < badge2.size()) {
			return -1;
		} else if(badge1.size() > badge2.size()) {
			return 1;
		} else {
			return 0;
		}
	}
}
