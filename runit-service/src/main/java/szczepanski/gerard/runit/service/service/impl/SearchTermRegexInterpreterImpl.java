package szczepanski.gerard.runit.service.service.impl;

import java.util.regex.Pattern;

import szczepanski.gerard.runit.service.service.SearchTermRegexInterpreter;

/**
 * This inperpreter interprete searchTerm to 
 * {@code Pattern}.
 * 
 * This regex Interpreter taes any searchTerm and looks for
 * '*' char, which means 'each'.
 * 
 * Example: 
 * 
 * *oogle.com -> google.com, donkey google.com, asjasdasdkoogle.com
 * 
 * *ire*ox -> Firefox, wiremox
 * 
 * @author Gerard Szczepanski
 */
public class SearchTermRegexInterpreterImpl implements SearchTermRegexInterpreter {

	private static final String ANY_SIGN = "*";
	
	@Override
	public Pattern fromSearchTerm(String searchTerm) {
			
		return null;
	}

}
