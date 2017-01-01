package szczepanski.gerard.runit.service.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import lombok.Builder;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.service.search.loader.Settings;
import szczepanski.gerard.runit.service.search.loader.SettingsLoader;
import szczepanski.gerard.runit.service.service.SearchService;

@Builder
public class SearchServiceImpl implements SearchService {
	private static final Logger LOG = Logger.getLogger(SearchServiceImpl.class);

	private final List<SearchAlgorithm> searchAlgorithms;
	private final SettingsLoader settingsLoader;

	@Override
	public List<SearchResult> searchFor(String searchTerm) {
		if (isSearchTermEmpty(searchTerm)) {
			return new ArrayList<>();
		}
		
		LOG.debug("Fire search for searchTerm: " + searchTerm);
		Settings settings = settingsLoader.loadSettings();
		return triggerSearchAlgorithms(searchTerm, settings);
	}
	
	private boolean isSearchTermEmpty(String searchTerm) {
		return searchTerm == null || "".equals(searchTerm);
	}

	private List<SearchResult> triggerSearchAlgorithms(String searchTerm, Settings settings) {
		List<SearchResult> searchResults = new ArrayList<>();
		
		searchAlgorithms.forEach(a -> {
				List<SearchResult> searchResultsFromAlgorithm = a.search(searchTerm, settings);
				searchResults.addAll(searchResultsFromAlgorithm);
				});
		
		return searchResults;
	}

}
