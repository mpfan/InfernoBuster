package infernobuster.parser;

import java.io.File;

public interface ParserStrategy {
	public Rule[] parse(File file);
}
