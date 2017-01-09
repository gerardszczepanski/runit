package szczepanski.gerard.runit.service.service;

import java.util.List;
import java.util.Optional;

import szczepanski.gerard.runit.search.service.result.SearchResult;

/**
 * Cache stores previous results (triggered at program runtime), to allow better performance.
 * 
 * @author Gerard Szczepanski
 */
public interface Cache {
	
	void addSearchResultsToCache(String searchTerm, List<SearchResult> searchResults);
	
	Optional<List<SearchResult>> getFromCache(String searchTerm);
	
	
}
