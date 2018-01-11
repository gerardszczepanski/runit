package szczepanski.gerard.runit.search.service.service.impl;

import lombok.Builder;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.advanced.collection.facade.AdvancedCollectionFactory;
import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.search.service.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.service.SearchService;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
public class SearchServiceImpl implements SearchService {
    private static final Logger LOG = Logger.getLogger(SearchServiceImpl.class);
    private static final List<SearchResult> EMPTY_SEARCH_RESULTS = new ArrayList<>(0);

    private final List<SearchAlgorithm> searchAlgorithms;
    private final SettingsLoader settingsLoader;
    private final Cache cache;

    @Override
    public List<SearchResult> searchFor(String searchTerm) {
        if (isSearchTermEmpty(searchTerm)) {
            return EMPTY_SEARCH_RESULTS;
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
        return StringUtils.EMPTY.equals(searchTerm);
    }

    private List<SearchResult> triggerSearchAlgorithms(String searchTerm, Settings settings) {
        List<SearchResult> searchResults = AdvancedCollectionFactory.list();

        searchAlgorithms.forEach(a -> {
            List<SearchResult> searchResultsFromAlgorithm = a.search(searchTerm, settings);
            searchResults.addAll(searchResultsFromAlgorithm);
        });
        return searchResults;
    }
}
