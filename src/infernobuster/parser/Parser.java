package infernobuster.parser;

import java.util.ArrayList;

public abstract class Parser {
	public abstract ArrayList<Rule> parse(ArrayList<String> content) throws ParserException;
	public abstract String export(ArrayList<Rule> rules);
}
