package szczepanski.gerard.runit.search.service;

@FunctionalInterface
public interface SearchTermMatcher {

    boolean isMatch(String searchTerm, String searchResultName);

}
