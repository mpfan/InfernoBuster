package infernobuster.parser;

import java.io.File;

public interface Parser {
	public Rule[] parse(File file);
}
