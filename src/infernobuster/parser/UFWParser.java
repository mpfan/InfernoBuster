package infernobuster.parser;

import java.io.File;
import java.util.ArrayList;

/*
 * -A ufw-user-output -p tcp -d 10.0.2.5 –dport 55 -s 10.0.2.4 –sport 45 -j DROP
 *
 */

public class UFWParser extends Parser{
	public UFWParser() {}

	public ArrayList<Rule> parse(File file) {
		
		
		return null;
	}
	
}
