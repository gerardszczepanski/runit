package szczepanski.gerard.runit.search.service.service.impl;

import szczepanski.gerard.runit.service.service.SearchTermMatcher;

/**
 * Simple searchTerm matching algorithm. It checks, if splitted by space
 * searchResutlName starts with searchTerm, both in lower case. If yes, then
 * matching is positive.
 * 
 * Positive matching:
 * 
 * searchTerm - google positive: google chrome positive: chrome google
 * 
 * @author Gerard Szczepanski
 */
public class SimpleSearchTermMatcher implements SearchTermMatcher {

	private static final String SPACE_REGEX = "\\s+";

	@Override
	public boolean isMatch(String searchTerm, String searchResultName) {
		boolean isWholeSearchResultNameMatch = isResultNameStartsWithSearchTerm(searchTerm, searchResultName);

		if (isWholeSearchResultNameMatch) {
			return true;
		} else {
			String[] searchResultNameSplitted = searchResultName.split(SPACE_REGEX);
			return processSplitedSearchResultName(searchTerm, searchResultNameSplitted);
		}
	}

	private boolean processSplitedSearchResultName(String searchTerm, String[] searchResultNameSplitted) {
		for (int i = 0; i < searchResultNameSplitted.length; i++) {
			if (isResultNameStartsWithSearchTerm(searchTerm, searchResultNameSplitted[i])) {
				return true;
			}
		}

		return false;
	}

	private boolean isResultNameStartsWithSearchTerm(String searchTerm, String searchResultName) {
		return searchResultName.toLowerCase().startsWith(searchTerm.toLowerCase());
	}

}
