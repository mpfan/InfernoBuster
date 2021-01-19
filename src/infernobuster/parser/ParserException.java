package infernobuster.parser;

public class ParserException extends Exception {
	private static final long serialVersionUID = -4330401354583437519L;
	
	public static final String MESSAGE = "Failed to parse file";
	public static final String MALF_MESSAGE = "Malformed rules in file";

	
	public ParserException (String message) {
		super(message);
	}

}
