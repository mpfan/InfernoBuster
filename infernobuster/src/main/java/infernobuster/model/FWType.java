package infernobuster.model;

/**
 * Used for tracking firewall types
 * 
 * @author Souheil
 *
 */
public enum FWType {
	UFW("UFW"),
	IPTABLES("IPTables");
	
	
	private String text;

	FWType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static FWType fromString(String text) {
        for (FWType fw : FWType.values()) {
            if (fw.text.equalsIgnoreCase(text)) {
                return fw;
            }
        }
        throw new IllegalArgumentException("Invalid input " + text);
    }
}
