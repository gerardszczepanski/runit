package szczepanski.gerard.runit.service.service;

import java.util.regex.Pattern;

public interface SearchTermRegexInterpreter {
	
	Pattern fromSearchTerm(String searchTerm);
	
}
