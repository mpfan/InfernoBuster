package infernobuster.model;

public enum Protocol {
	TCP("TCP"), UDP("UDP"), ANY("ANY");
	
	private String text;

    Protocol(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Protocol fromString(String text) {
        for (Protocol p : Protocol.values()) {
            if (p.text.equalsIgnoreCase(text)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid input " + text);
    }
    
    public static String[] getAll() {
    	String[] all = new String[Protocol.values().length];
    	
    	for (int i = 0; i < all.length; i++) {
            all[i] = Protocol.values()[i].text.toUpperCase();
        }
    	
    	return all;
    }
}	
