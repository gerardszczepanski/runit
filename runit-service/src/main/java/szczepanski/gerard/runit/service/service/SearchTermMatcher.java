package szczepanski.gerard.runit.service.service;

@FunctionalInterface
public interface SearchTermMatcher {
	
	boolean isMatch(String searchTerm, String searchResultName);
	
}
