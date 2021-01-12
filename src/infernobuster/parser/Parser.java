package infernobuster.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public abstract class Parser {
	public abstract ArrayList<Rule> parse(ArrayList<String> content) throws ParserException;
}
