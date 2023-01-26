package szczepanski.gerard.runit.search.service;

import szczepanski.gerard.runit.search.result.SearchResult;

import java.util.List;

@FunctionalInterface
public interface SearchService {

    List<SearchResult> searchFor(String searchTerm);

}
