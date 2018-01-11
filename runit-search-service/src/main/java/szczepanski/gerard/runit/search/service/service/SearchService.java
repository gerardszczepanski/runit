package szczepanski.gerard.runit.search.service.service;

import szczepanski.gerard.runit.search.service.result.SearchResult;

import java.util.List;

@FunctionalInterface
public interface SearchService {

    List<SearchResult> searchFor(String searchTerm);

}
