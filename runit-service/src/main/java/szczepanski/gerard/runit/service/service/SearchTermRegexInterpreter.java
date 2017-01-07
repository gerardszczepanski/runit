package szczepanski.gerard.runit.service.service;

import java.util.regex.Pattern;

@FunctionalInterface
public interface SearchTermRegexInterpreter {
	
	Pattern fromSearchTerm(String searchTerm);
	
}
