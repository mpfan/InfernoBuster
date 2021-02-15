package infernobuster.model;

public enum Action {
	DENY("deny"),
	ALLOW("allow");
	
	private String text;

    Action(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Action fromString(String text) {
        for (Action a : Action.values()) {
            if (a.text.equalsIgnoreCase(text)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Invalid input " + text);
    }
    
    public static String[] getAll() {
    	String[] all = new String[Action.values().length];
    	
    	for (int i = 0; i < all.length; i++) {
            all[i] = Action.values()[i].text.toUpperCase();
        }
    	
    	return all;
    }
}
