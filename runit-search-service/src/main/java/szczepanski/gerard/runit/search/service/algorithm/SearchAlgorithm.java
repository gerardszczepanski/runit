package szczepanski.gerard.runit.search.service.algorithm;

import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.settings.service.loader.Settings;

import java.util.List;

/**
 * Represents search algorithm for one of the {@code SearchResult}.
 *
 * @author Gerard Szczepanski
 */
@FunctionalInterface
public interface SearchAlgorithm {

    List<SearchResult> search(String searchTerm, Settings settings);
}
