package szczepanski.gerard.runit.search.service.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import lombok.Builder;
import szczepanski.gerard.runit.search.service.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.service.service.Cache;
import szczepanski.gerard.runit.service.service.SearchService;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;

@Builder
public class SearchServiceImpl implements SearchService {
	private static final Logger LOG = Logger.getLogger(SearchServiceImpl.class);

	private final List<SearchAlgorithm> searchAlgorithms;
	private final SettingsLoader settingsLoader;
	private final Cache cache;

	@Override
	public List<SearchResult> searchFor(String searchTerm) {
		if (isSearchTermEmpty(searchTerm)) {
			return new ArrayList<>();
		}

		Optional<List<SearchResult>> cachedSearchResults = cache.getFromCache(searchTerm);
		if (cachedSearchResults.isPresent()) {
			return cachedSearchResults.get();
		}

		return triggerNewSearchFor(searchTerm);
	}

	private List<SearchResult> triggerNewSearchFor(String searchTerm) {
		LOG.debug("Fire search for searchTerm: " + searchTerm);
		Settings settings = settingsLoader.getSettings();
		List<SearchResult> searchResults = triggerSearchAlgorithms(searchTerm, settings);
		cache.addSearchResultsToCache(searchTerm, searchResults);
		return searchResults;
	}

	private boolean isSearchTermEmpty(String searchTerm) {
		return "".equals(searchTerm);
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
