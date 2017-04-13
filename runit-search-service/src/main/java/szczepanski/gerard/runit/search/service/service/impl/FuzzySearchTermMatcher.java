package szczepanski.gerard.runit.search.service.service.impl;

import org.apache.commons.lang3.StringUtils;

/**
 * This SearchTermMatcher impl works like SimpleSearchTermMatcher. 
 * 
 * The only difference is that this algorithm accepts user typing errors, and 
 * based on Levenshtein Distance can return result even if searchTerm is a bit different than result name.
 * 
 * Example -> 'Runit' search results matches searchTerm 'unit', 'rnit', 'ruit' rtc.
 * @author Gerard Szczepañski
 */
public class FuzzySearchTermMatcher extends SimpleSearchTermMatcher {
	
	private static final int ACCEPTABLE_DISTANCE = 1;
	
	@Override
	public boolean isMatch(String searchTerm, String searchResultName) {
		boolean isMatch = super.isMatch(searchTerm, searchResultName);
		
		if (isMatch) {
			return true;
		}
		
		return isFuzzyMatch(searchTerm, searchResultName);
	}
	
	private boolean isFuzzyMatch(String searchTerm, String searchResultName) {
		if (searchTerm.length() > searchResultName.length() || searchTerm.length() <= ACCEPTABLE_DISTANCE) {
			return false;
		}
		
		return processFuzzyMatching(searchTerm, searchResultName);
	}
	
	private boolean processFuzzyMatching(String searchTerm, String searchResultName) {
		String[] splittedSearchTermArray = searchTerm.split(FuzzySearchTermMatcher.SPACE_REGEX);
		String[] splittedSearchResultNameArray = searchResultName.split(FuzzySearchTermMatcher.SPACE_REGEX);
		
		for (String splittedSearchTerm: splittedSearchTermArray) {
			for (String splittedSearchResultName: splittedSearchResultNameArray) {
				int levenshteinDistance = StringUtils.getLevenshteinDistance(splittedSearchTerm, splittedSearchResultName);
				if (levenshteinDistance <= ACCEPTABLE_DISTANCE) {
					return true;
				}
			}
		}
		
		return false;
	}

}
