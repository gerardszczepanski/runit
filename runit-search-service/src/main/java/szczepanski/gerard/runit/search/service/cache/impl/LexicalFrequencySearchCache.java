package szczepanski.gerard.runit.search.service.cache.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import it.unimi.dsi.fastutil.objects.AbstractObject2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.search.service.cache.Cache;
import szczepanski.gerard.runit.search.service.result.SearchResult;

/**
 * Advanced, smart cache. //TODO update doc
 * 
 * @author Gerard Szczepanski
 */
public class LexicalFrequencySearchCache implements Cache {
	private static final Logger LOG = Logger.getLogger(LexicalFrequencySearchCache.class);

	protected final CacheContainer cacheContainer;

	public LexicalFrequencySearchCache(int size, int numberOfSlotsToRemoveWhenClear) {
		LOG.debug("Instantizing Cache with size: " + size);
		this.cacheContainer = new CacheContainer(size, numberOfSlotsToRemoveWhenClear);
	}

	@Override
	public void addSearchResultsToCache(String searchTerm, List<SearchResult> searchResults) {
		boolean finished = false;
		int searchResultsHash = searchResults.hashCode();

		for (int i = 0; i < cacheContainer.firstFreeSlot; i++) {
			CachedSearchResultsBucket bucket = cacheContainer.buckets[i];

			if (bucket.searchTermTimeStamps.containsKey(searchTerm)) {
				if (bucket.searchResultsHash == searchResultsHash) {
					LOG.debug(String.format("SearchTerm [%s] for SearchResults [%s] added to existing [%d]th bucket", searchResults.size(), searchTerm, i));
					cacheContainer.updateBucketWithSearchTerm(i, searchTerm);
				} else {
					LOG.debug(String.format("%s SearchResults for searchTerm [%s] added to cache. Split existing [%d]th bucket", searchResults.size(),
							searchTerm, i));
					cacheContainer.splitBucketForSearchTerm(i, searchTerm, searchResults);
				}
				finished = true;
				break;
			} else if (bucket.searchResultsHash == searchResultsHash) {
				LOG.debug(String.format("[%s] SearchResults for searchTerm [%s] added to cache. Update existing [%d]th bucket", searchResults.size(),
						searchTerm, i));
				cacheContainer.updateBucketWithSearchTerm(i, searchTerm);
				finished = true;
				break;
			}
		}

		if (!finished) {
			LOG.debug(String.format("[%s] SearchResults for searchTerm [%s] added to cache as new bucket.", searchResults.size(), searchTerm));
			cacheContainer.addNewSearchResultsToBucket(searchTerm, searchResults);
		}
	}

	@Override
	public Optional<List<SearchResult>> getFromCache(String searchTerm) {
		CachedSearchResultsBucket bucket = getBucketFromCache(searchTerm);

		if (bucket == null) {
			LOG.debug("Do not found SearchResults for searchTerm: " + searchTerm);
			return Optional.empty();
		}

		boolean updateSearchTermResults = CacheRefreshAlgorithm.shouldSerachResultsBeUpdatedForThatSearchTerm(searchTerm, bucket);
		if (updateSearchTermResults) {
			LOG.debug("Found cached SearchResults for searchTerm: " + searchTerm + "but Cache will look for them again. Returning EMPTY");
			return Optional.empty();
		} else {
			LOG.debug(String.format("Found cached SearchResults for searchTerm: [%s] in Bucket: %s", searchTerm, bucket));
			return Optional.of(bucket.searchResults);
		}
	}

	private CachedSearchResultsBucket getBucketFromCache(String searchTerm) {
		for (int i = 0; i < cacheContainer.firstFreeSlot; i++) {
			if (cacheContainer.buckets[i].searchTermTimeStamps.containsKey(searchTerm)) {
				return cacheContainer.buckets[i];
			}
		}
		return null;
	}

	static class CacheContainer {
		private static final int FIRST_ELEMENT_INDEX = 1;
		private static final Comparator<CachedSearchResultsBucket> SORT_COMPARATOR = (c1, c2) -> {
			int firstWeight = 0;
			int secondWeight = 0;

			if (c1 != null) {
				firstWeight = c1.bucketWeight;
			}

			if (c2 != null) {
				secondWeight = c2.bucketWeight;
			}

			return Integer.compare(secondWeight, firstWeight);
		};

		protected final int size;
		protected final int numberOfSlotsToRemoveWhenClear;
		protected final CachedSearchResultsBucket[] buckets;
		protected int firstFreeSlot = FIRST_ELEMENT_INDEX;

		protected CacheContainer(int size, int numberOfSlotsToRemoveWhenClear) {
			this.size = ++size;
			this.numberOfSlotsToRemoveWhenClear = numberOfSlotsToRemoveWhenClear;
			this.buckets = new CachedSearchResultsBucket[this.size];
			initializeBucketForEmptySearchResults();
		}

		private void initializeBucketForEmptySearchResults() {
			buckets[0] = new CachedSearchResultsBucket(StringUtils.EMPTY, new ArrayList<>(0));
		}

		protected void addNewSearchResultsToBucket(String searchTerm, List<SearchResult> searchResults) {
			freeContainerSlotsIfItsFull();
			buckets[firstFreeSlot] = new CachedSearchResultsBucket(searchTerm, searchResults);
			firstFreeSlot++;
			sortBuckets();
		}

		protected void updateBucketWithSearchTerm(int bucketIndex, String searchTerm) {
			buckets[bucketIndex].updateBucketWithSearchTerm(searchTerm);
			sortBuckets();
		}

		protected void splitBucketForSearchTerm(int bucketIndex, String searchTerm, List<SearchResult> searchResults) {
			freeContainerSlotsIfItsFull();
			buckets[firstFreeSlot] = buckets[bucketIndex].splitBucketForSearchTermAndNewSearchResults(searchTerm, searchResults);
			firstFreeSlot++;
			sortBuckets();
		}

		private void sortBuckets() {
			Arrays.sort(buckets, FIRST_ELEMENT_INDEX, buckets.length, SORT_COMPARATOR);
		}

		private void freeContainerSlotsIfItsFull() {
			if (firstFreeSlot == buckets.length) {
				firstFreeSlot -= numberOfSlotsToRemoveWhenClear;

				for (int i = firstFreeSlot; i < buckets.length; i++) {
					buckets[i] = null;
				}

				LOG.debug(String.format("CLEAR CACHE: %d latest slots was removed", numberOfSlotsToRemoveWhenClear));
			}
		}
	}

	static class CacheRefreshAlgorithm {
		private static final long SEARCH_TERM_VALID_DURATION_IN_MS = 60000 * 20;

		private static boolean shouldSerachResultsBeUpdatedForThatSearchTerm(String searchTerm, CachedSearchResultsBucket bucket) {
			LexicalSearchTerm lexicalSearchTerm = bucket.searchTermTimeStamps.get(searchTerm);

			if (resultWasSearchedLongTimeAgo(lexicalSearchTerm)) {
				return true;
			}

			return false;
		}

		private static boolean resultWasSearchedLongTimeAgo(LexicalSearchTerm lexicalSearchTerm) {
			return System.currentTimeMillis() - lexicalSearchTerm.timeStamp >= SEARCH_TERM_VALID_DURATION_IN_MS;
		}
	}

	static class CachedSearchResultsBucket {

		protected int bucketWeight;

		protected int frequency;
		protected final AbstractObject2ObjectMap<String, LexicalSearchTerm> searchTermTimeStamps = new Object2ObjectOpenHashMap<>();
		protected final List<SearchResult> searchResults;
		protected final int searchResultsHash;

		protected CachedSearchResultsBucket(String searchTerm, List<SearchResult> searchResults) {
			this.searchResults = searchResults;
			this.searchResultsHash = searchResults.hashCode();
			this.searchTermTimeStamps.put(searchTerm, new LexicalSearchTerm(searchTerm));
			this.frequency = 1;
			updateBucketWeigth();
		}

		protected CachedSearchResultsBucket(String searchTerm, List<SearchResult> searchResults, LexicalSearchTerm lexicalSearchTerm) {
			this.searchResults = searchResults;
			this.searchResultsHash = searchResults.hashCode();
			this.searchTermTimeStamps.put(searchTerm, lexicalSearchTerm);
			this.frequency = lexicalSearchTerm.frequency;
			updateBucketWeigth();
		}

		protected void updateBucketWithSearchTerm(String searchTerm) {
			if (searchTermTimeStamps.containsKey(searchTerm)) {
				searchTermTimeStamps.get(searchTerm).update();
			} else {
				searchTermTimeStamps.put(searchTerm, new LexicalSearchTerm(searchTerm));
			}

			frequency++;
			updateBucketWeigth();
		}

		protected CachedSearchResultsBucket splitBucketForSearchTermAndNewSearchResults(String searchTerm, List<SearchResult> searchResults) {
			LexicalSearchTerm lexicalSearchTerm = searchTermTimeStamps.get(searchTerm);
			frequency -= lexicalSearchTerm.frequency;
			searchTermTimeStamps.remove(searchTerm);
			updateBucketWeigth();

			return createBucketFromSplit(searchTerm, searchResults, lexicalSearchTerm);
		}

		private CachedSearchResultsBucket createBucketFromSplit(String searchTerm, List<SearchResult> searchResults, LexicalSearchTerm lexicalSearchTerm) {
			lexicalSearchTerm.update();
			return new CachedSearchResultsBucket(searchTerm, searchResults, lexicalSearchTerm);
		}

		private void updateBucketWeigth() {
			bucketWeight = searchResults.size() * searchTermTimeStamps.size() * frequency;
		}

		@Override
		public String toString() {
			return "[W:" + bucketWeight + ", F:" + frequency + ", St:" + searchTermTimeStamps.size() + ", R:" + searchResults.size() + "]";
		}

	}

	static class LexicalSearchTerm {
		protected String searchTerm;
		protected long timeStamp = System.currentTimeMillis();
		protected int frequency = 1;

		protected LexicalSearchTerm(String searchTerm) {
			this.searchTerm = searchTerm;
		}

		protected void update() {
			timeStamp = System.currentTimeMillis();
			frequency++;
		}
	}

}
