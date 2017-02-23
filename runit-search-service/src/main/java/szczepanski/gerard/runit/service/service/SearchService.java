package szczepanski.gerard.runit.service.service;

import java.util.List;

import szczepanski.gerard.runit.search.service.result.SearchResult;

@FunctionalInterface
public interface SearchService {
	
	List<SearchResult> searchFor(String searchTerm);
	
}
