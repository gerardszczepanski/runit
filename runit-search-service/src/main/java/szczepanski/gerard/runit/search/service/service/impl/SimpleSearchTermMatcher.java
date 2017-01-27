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
		String[] searchTermSplitted = searchTerm.split(SPACE_REGEX);
		String[] searchResultNameSplitted = searchResultName.split(SPACE_REGEX);
		return processSplitedSearchParts(searchTermSplitted, searchResultNameSplitted);
	}

	private boolean processSplitedSearchParts(String[] searchTermSplitted, String[] searchResultNameSplitted) {
		for (int i = 0; i < searchTermSplitted.length; i++) {
			for (int j = 0; j < searchResultNameSplitted.length; j++) {

				if (isSplittedResultNameStartsWithSplittedSearchTerm(searchTermSplitted[i], searchResultNameSplitted[j])) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isSplittedResultNameStartsWithSplittedSearchTerm(String splitedSearchTermPart, String spliiedSearchResultNamePart) {
		return spliiedSearchResultNamePart.toLowerCase().startsWith(splitedSearchTermPart.toLowerCase());
	}

}
