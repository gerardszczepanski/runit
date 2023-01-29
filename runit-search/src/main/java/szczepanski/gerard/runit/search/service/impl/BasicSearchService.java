package szczepanski.gerard.runit.search.service.impl;

import lombok.Builder;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.collection.AdvancedCollectionFactory;
import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.cache.Cache;
import szczepanski.gerard.runit.search.result.SearchResult;
import szczepanski.gerard.runit.search.service.SearchService;
import szczepanski.gerard.runit.settings.loader.Settings;
import szczepanski.gerard.runit.settings.loader.SettingsLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Builder
public class BasicSearchService implements SearchService {
    private static final Logger LOG = Logger.getLogger(BasicSearchService.class);
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
        return cachedSearchResults.orElseGet(() -> triggerNewSearchFor(searchTerm));
    }

    private List<SearchResult> triggerNewSearchFor(String searchTerm) {
        LOG.debug("Fire search for searchTerm: " + searchTerm);
        final Settings settings = settingsLoader.getSettings();
        final List<SearchResult> searchResults = triggerSearchAlgorithms(searchTerm, settings);
        final List<SearchResult> orderedSearchResults = toOrderedSearchResults(searchResults, searchTerm);
        cache.addSearchResultsToCache(searchTerm, orderedSearchResults);
        return orderedSearchResults;
    }

    private List<SearchResult> toOrderedSearchResults(List<SearchResult> searchResults, String searchTerm) {
        if (searchResults.isEmpty()) {
            return searchResults;
        }

        final List<SearchResult> orderedResults = AdvancedCollectionFactory.list();

        final List<SearchResult> orderedResultsWithSearchTerm = searchResults.stream()
                .filter(result -> result.title().toLowerCase().contains(searchTerm.toLowerCase()))
                .sorted(Comparator.comparing(SearchResult::title))
                .collect(toList());

        final List<SearchResult> orderedRestOfSearchResults = searchResults.stream()
                .filter(result -> !orderedResultsWithSearchTerm.contains(result))
                .sorted(Comparator.comparing(SearchResult::title))
                .collect(toList());

        orderedResults.addAll(orderedResultsWithSearchTerm);
        orderedResults.addAll(orderedRestOfSearchResults);
        return orderedResults;
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
