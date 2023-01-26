package szczepanski.gerard.runit.search.cache;

import szczepanski.gerard.runit.search.result.SearchResult;

import java.util.List;
import java.util.Optional;

/**
 * Cache stores previous results (triggered at program runtime), to allow better performance.
 *
 * @author Gerard Szczepanski
 */
public interface Cache {

    void addSearchResultsToCache(String searchTerm, List<SearchResult> searchResults);

    Optional<List<SearchResult>> getFromCache(String searchTerm);

    void clear();

    void accept(CacheVisitor visitor);

}
