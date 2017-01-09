package szczepanski.gerard.runit.search.service.service.impl;

import szczepanski.gerard.runit.service.service.SearchTermMatcher;

/**
 * Simple searchTerm matching algorithm. 
 * It checks, if searchResutlName starts with searchTerm, both in lower case.
 * If yes, then matching is positive. 
 * 
 * @author Gerard Szczepanski
 */
public class SimpleSearchTermMatcher implements SearchTermMatcher {

	@Override
	public boolean isMatch(String searchTerm, String searchResultName) {
		return searchResultName.toLowerCase().startsWith(searchTerm.toLowerCase());
	}

}
