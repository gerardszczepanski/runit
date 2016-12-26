package szczepanski.gerard.runit.service.search.algorithm;

import java.util.List;

import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.loader.Settings;

/**
 * Represents search algorithm for one of the {@code SearchResult}. 
 * @author Gerard Szczepanski
 */
public interface SearchAlgorithm {
	
	List<SearchResult> search(String searchTerm, Settings settings);
	 
}
