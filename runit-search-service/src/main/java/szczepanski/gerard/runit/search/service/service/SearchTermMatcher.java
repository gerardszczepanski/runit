package szczepanski.gerard.runit.search.service.service;

@FunctionalInterface
public interface SearchTermMatcher {

    boolean isMatch(String searchTerm, String searchResultName);

}
