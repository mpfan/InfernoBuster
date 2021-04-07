package infernobuster.view;

import java.util.Comparator;

public class FocusedRuleSorter {
	public Comparator<Boolean> getComparator() {
		return new Comparator<Boolean>() {
			public int compare(Boolean rule1, Boolean rule2) {
				if(rule1) {
					return -1;
				}
				
				return 1;
			}
		};
	}
	
	public Comparator<Boolean> getBackComparator() {
		return new Comparator<Boolean>() {
			public int compare(Boolean rule1, Boolean rule2) {
				if(rule1) {
					return 1;
				}
				
				return -1;
			}
		};
	}
}
