package infernobuster.model;

public enum Direction {
	IN("in"),
	OUT("out"),
	DIRECTION("any");
	
	private String text;

    Direction(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Direction fromString(String text) {
        for (Direction d : Direction.values()) {
            if (d.text.equalsIgnoreCase(text)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid input " + text);
    }
    
    public static String[] getAll() {
    	String[] all = new String[Direction.values().length];
    	
    	for (int i = 0; i < all.length; i++) {
            all[i] = Direction.values()[i].text.toUpperCase();
        }
    	
    	return all;
    }
}	
