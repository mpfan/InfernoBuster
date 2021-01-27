package infernobuster.model;

public enum Direction {
	IN("in"),
	OUT("out");
	
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
}	
