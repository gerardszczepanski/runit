package szczepanski.gerard.runit.search.service.cache.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.apache.log4j.Logger;

import lombok.AllArgsConstructor;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.search.service.cache.CacheVisitor;
import szczepanski.gerard.runit.search.service.result.SearchResult;

public class SimpleSearchCache implements Cache {
	private static final Logger LOG = Logger.getLogger(SimpleSearchCache.class);

	private final int cacheLimit;
	private final int clearCacheHeadSize;
	private final Queue<CachedSearchResults> cachedSearchResults;

	public SimpleSearchCache(int cacheLimit, double cachePercentageCleaningSize) {
		LOG.debug("Instantizing Cache with size: " + cacheLimit);
		this.cacheLimit = cacheLimit;
		this.clearCacheHeadSize = (int) (cacheLimit * cachePercentageCleaningSize);
		this.cachedSearchResults = new LinkedList<>();
	}

	@Override
	public void addSearchResultsToCache(String searchTerm, List<SearchResult> searchResults) {
		checkIfCacheContainsSearchTerm(searchTerm);
		removeOldCacheValuesIfCacheIsFull();
		addToCache(searchTerm, searchResults);
	}

	private void checkIfCacheContainsSearchTerm(String searchTerm) {
		if (getFromCache(searchTerm).isPresent()) {
			throw new RunitRuntimeException(ExceptionCode.R_009, searchTerm);
		}
	}

	private void removeOldCacheValuesIfCacheIsFull() {
		if (isCacheFull()) {
			LOG.debug(String.format("Cache is full. Removing %s oldest cached values.", clearCacheHeadSize));
			for (int i = 0; i < clearCacheHeadSize; i++) {
				CachedSearchResults oldestCachedSearchResults = cachedSearchResults.peek();
				cachedSearchResults.remove(oldestCachedSearchResults);
			}

			System.gc(); // Hey Garbage Collector, would you like to clean old
							// files?
		}
	}

	private boolean isCacheFull() {
		return cacheLimit == cachedSearchResults.size();
	}

	private void addToCache(String searchTerm, List<SearchResult> searchResults) {
		LOG.debug(String.format("%s SearchResults for searchTerm [%s] added to cache.", searchResults.size(), searchTerm));
		CachedSearchResults newCachedSearchResults = CachedSearchResults.of(searchTerm, searchResults);
		cachedSearchResults.add(newCachedSearchResults);
	}

	@Override
	public Optional<List<SearchResult>> getFromCache(String searchTerm) {
		Iterator<CachedSearchResults> iterator = cachedSearchResults.iterator();

		while (iterator.hasNext()) {
			CachedSearchResults cachedSearchResults = iterator.next();

			if (cachedSearchResults.isSearchTermMatch(searchTerm)) {
				LOG.debug("Found cached SearchResults for searchTerm: " + searchTerm);
				return Optional.of(cachedSearchResults.searchResults);
			}
		}

		return Optional.empty();
	}
	
	@Override
	public void accept(CacheVisitor visitor) {
		visitor.visit(this);
	}

	@AllArgsConstructor
	private static class CachedSearchResults {
		private static final List<SearchResult> EMPTY_LIST = new ArrayList<>(0);

		private final String searchTerm;
		private final List<SearchResult> searchResults;

		/**
		 * Create CachedSearchResults object from searchTerm and SearchResult
		 * List. If list is empty, then List pointer is changed to EMPTY LIST.
		 */
		private static CachedSearchResults of(String searchTerm, List<SearchResult> searchResults) {
			if (searchResults.isEmpty()) {
				searchResults = EMPTY_LIST;
			}

			return new CachedSearchResults(searchTerm, searchResults);
		}

		public boolean isSearchTermMatch(String searchTerm) {
			return this.searchTerm.equals(searchTerm);
		}
	}

}
