package szczepanski.gerard.runit.service.service.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.apache.log4j.Logger;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.service.Cache;

public class SimpleSearchCache implements Cache {
	private static final Logger LOG = Logger.getLogger(SimpleSearchCache.class);

	private final int cacheLimit;
	private final Queue<CachedSearchResults> cachedSearchResults;

	public SimpleSearchCache(int cacheLimit) {
		LOG.debug("Instantizing Cache with size: " + cacheLimit);
		this.cacheLimit = cacheLimit;
		this.cachedSearchResults = new LinkedList<>();
	}

	@Override
	public void addSearchResultsToCache(String searchTerm, List<SearchResult> searchResults) {
		checkIfCacheContainsSearchTerm(searchTerm);
		removeOldestCacheValueIfCacheIsFull();
		addToCache(searchTerm, searchResults);
	}

	private void checkIfCacheContainsSearchTerm(String searchTerm) {
		if (getFromCache(searchTerm).isPresent()) {
			throw new RunitRuntimeException(
					String.format("Not Allowed Cache operation! Trying to add searchTerm: [%s] results that actually are stored in Cache!", searchTerm));
		}
	}

	private void removeOldestCacheValueIfCacheIsFull() {
		if (isCacheFull()) {
			LOG.debug("Cache is full. Removing oldest cached value.");
			CachedSearchResults oldestCachedSearchResults = cachedSearchResults.peek();
			cachedSearchResults.remove(oldestCachedSearchResults);
		}
	}
	
	private boolean isCacheFull() {
		return cacheLimit == cachedSearchResults.size();
	}

	private void addToCache(String searchTerm, List<SearchResult> searchResults) {
		LOG.debug(String.format("%s SearchResults for searchTerm [%s] added to cache.", searchResults.size(), searchTerm));
		CachedSearchResults newCachedSearchResults = new CachedSearchResults(searchTerm, searchResults);
		cachedSearchResults.add(newCachedSearchResults);
	}

	@Override
	public Optional<List<SearchResult>> getFromCache(String searchTerm) {
		Optional<List<SearchResult>> optionalCachedSearchResults = Optional.empty();
		Iterator<CachedSearchResults> iterator = cachedSearchResults.iterator();

		while (iterator.hasNext()) {
			CachedSearchResults cachedSearchResults = iterator.next();

			if (cachedSearchResults.isSearchTermMatch(searchTerm)) {
				LOG.debug("Found cached SearchResults for searchTerm: " + searchTerm);
				optionalCachedSearchResults = Optional.of(cachedSearchResults.searchResults);
				break;
			}
		}

		return optionalCachedSearchResults;
	}

	@RequiredArgsConstructor
	private static class CachedSearchResults {

		private final String searchTerm;
		private final List<SearchResult> searchResults;

		public boolean isSearchTermMatch(String searchTerm) {
			return this.searchTerm.equals(searchTerm);
		}

	}

}
