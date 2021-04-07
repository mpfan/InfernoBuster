package infernobuster.model;

/**
 * Enumerator class for possible anomalies.
 * 
 */
public enum Anomaly {
	REDUNDANCY("Redundancy"), 
	INCONSISTENCY("Inconsistency"), 
	SHADOWING("Shadowing"), 
	DOWN_REDUNDANT("Down Redundant"), 
	GENERALIZIATION("Generalization"), 
	UP_REDUNDANT("Up Redundant"), 
	CORRELATION("Correlation"), 
	PARTIAL_REDUNDANCY("Partial Redundancy");
	
	private final String name;
	
	private Anomaly(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public static Anomaly fromString(String text) {
        for (Anomaly a : Anomaly.values()) {
            if (a.name.equalsIgnoreCase(text)) {
                return a;
            }
        }
		return null;
    }
}	
