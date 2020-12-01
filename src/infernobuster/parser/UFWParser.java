package infernobuster.parser;

import java.util.ArrayList;

/*
 * -A ufw-user-output -p tcp -d 10.0.2.5 –dport 55 -s 10.0.2.4 –sport 45 -j DROP
 *
 */

public class UFWParser extends Parser{
	public UFWParser() {}

	@Override
	public ArrayList<Rule> parse(String file) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
