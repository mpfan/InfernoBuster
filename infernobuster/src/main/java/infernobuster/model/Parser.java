package infernobuster.model;

import java.util.ArrayList;

/**
 * Abstract class for parsers. For each unique file, a concrete class should be made.
 *
 */
public interface Parser {
	public abstract ArrayList<Rule> parse(ArrayList<String> content) throws ParserException;
	public abstract String export(ArrayList<Rule> rules);
}
