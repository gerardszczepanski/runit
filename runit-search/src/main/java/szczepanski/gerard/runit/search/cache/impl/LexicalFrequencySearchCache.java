package szczepanski.gerard.runit.search.cache.impl;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.collection.AdvancedCollectionFactory;
import szczepanski.gerard.runit.common.util.StringUtils;
import szczepanski.gerard.runit.search.cache.Cache;
import szczepanski.gerard.runit.search.cache.CacheVisitor;
import szczepanski.gerard.runit.search.result.SearchResult;

import java.util.*;

import static lombok.AccessLevel.*;

/**
 * Advanced, smart, less memory cost, refreshing cache.
 * <p>
 * This cache stores cached SearchResults in Buckets.
 * Each Bucket contains:
 * -> SearchResults
 * -> searchTerms that returned contained SearchResults
 * -> weight of the bucket.
 * <p>
 * This Cache checks if SearchResults returned by some searchTerm is actually in Cache. If yes,
 * then Cache updates existing Bucket with new searchTerm. So if searchTerms "runi" and "runit" returns the same
 * SearchResults, they will be in the same Bucket (for SearchResult). So basic difference between {@code SimpleSearchCache} and this
 * Cache is it allocates memory for SearchResults, not searchTerm.
 * <p>
 * SearchTerm in Bucket also contains frequency of the typed searchTerm. Phrase "runit" may be typed 28 times, while "runi" may be typed only once.
 * SearchTerm contains timestamp of last typed time. When searchTerm that is in Bucket is retyped again, frequency is increased for that searchTerm,
 * and searchTerm gets new timestamp.
 * <p>
 * Weight of the Bucket is counted after every Bucket update or split. Weight of the bucket formula is
 * W = [frequencies of the searchTerms] x [number of searchTerms in Bucket] x [size of the SearchResults for Bucket]
 * <p>
 * Buckets are stored in special Container. After each Container operation, Buckets are sorted by their weight from the
 * heavier Bucket to the lighter. When Container is full, then it removes X latest Buckets (with the smaller weights).
 * <p>
 * Container also have special 0 Weight bucket for empty SearchResults queries. This Bucket is always at the 0 position,
 * and it is not sorted.
 * <p>
 * This Cache assure, that most frequent, most heavier SarchResults are always at the first place, gives fast access for them.
 * <p>
 * This Cache is also smart. Cache can trick {@code SearchService}, which is intrested if there are SarchResults for given searchTerm, and
 * tell SearchRervice, that it doesn't contain cached results even if it does. This happens when Cache algorithm finds, that
 * stored SearchResults for given searchTerm may be outdated (for example timestamp is really old).
 * <p>
 * If Cache finds that, it tells {@SearchService} that it doesnt found cached results, and let SearchService search for them again.
 * After search (new/old) SearchReusults for stored searchTerm are returned to Cache. Then Cache check if given refreshed SearchResults
 * for stored searchTerm are different from that stored.
 * -> If yes, then Cache will split Bucket with given searchTerm, creates new one for given searchTerm
 * and new Results. Weight of the splitted Bucket will change.
 * -> If no, then current searchTerm timestamp is updated in current Bucket.
 * <p>
 * This mechanism lets Cache to refresh result for long term cached searchTerms.
 * <p>
 * In general, this Cache is lighter than {@code SimpleSearchCache}:
 * -> Less memory usage (no queue, but simple array),
 * -> no duplicated cached records for the same SearchResults but different searchTerms,
 * -> most valuable cached results are not cleaned from cache when Cache free memory,
 * -> quicker access elements,
 * -> smart algorithm for refreshing long term stored SearchResuts
 *
 * @author Gerard Szczepanski
 * @since Runit 1.2.0
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

        boolean updateSearchTermResults = CacheRefreshAlgorithm.areSearchResultsEligibleToBeUpdatedForThatSearchTerm(searchTerm, bucket);
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

    @Override
    public void clear() {
        cacheContainer.firstFreeSlot = 1;
        Arrays.fill(cacheContainer.buckets, null);
        cacheContainer.initializeBucketForEmptySearchResults();
    }

    @Override
    public void accept(CacheVisitor visitor) {
        visitor.visit(this);
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

    @NoArgsConstructor(access = PRIVATE)
    static class CacheRefreshAlgorithm {
        private static final int SEARCH_TERM_VALID_DURATION_IN_MS = 60000 * 20;

        private static boolean areSearchResultsEligibleToBeUpdatedForThatSearchTerm(String searchTerm, CachedSearchResultsBucket bucket) {
            LexicalSearchTerm lexicalSearchTerm = bucket.searchTermTimeStamps.get(searchTerm);
            return wasResultSearchedLongTimeAgo(lexicalSearchTerm);
        }

        private static boolean wasResultSearchedLongTimeAgo(LexicalSearchTerm lexicalSearchTerm) {
            return System.currentTimeMillis() - lexicalSearchTerm.timeStamp >= SEARCH_TERM_VALID_DURATION_IN_MS;
        }
    }

    static class CachedSearchResultsBucket {

        protected final Map<String, LexicalSearchTerm> searchTermTimeStamps = AdvancedCollectionFactory.map();
        protected final List<SearchResult> searchResults;
        protected final int searchResultsHash;
        protected int bucketWeight;
        protected int frequency;

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
