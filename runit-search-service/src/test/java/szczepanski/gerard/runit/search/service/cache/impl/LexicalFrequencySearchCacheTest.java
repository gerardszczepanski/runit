package szczepanski.gerard.runit.search.service.cache.impl;

import org.testng.Assert;
import org.testng.annotations.Test;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.impl.WebPageResult;
import szczepanski.gerard.runit.settings.service.loader.Alias;

import java.util.ArrayList;
import java.util.List;

public class LexicalFrequencySearchCacheTest {

    /**
     * ADD Method If Bucket from container contains searchTerm that we obtain
     * and hash doesn't changed that means, that Cache firstly found our
     * SearchResults in memory, but let the SearchService to find results again
     * (in case of outdated results for example), but SearchService returns the
     * same SearchResults (this means that results dont changed at using time).
     * <p>
     * We must update bucket for our results.
     */
    @Test
    public void updateBucketWhenBucketContainsSearchTermAndHashHasNotChanged() {
        // Arrange
        LexicalFrequencySearchCache cache = new LexicalFrequencySearchCache(10, 4);

        String searchTerm = "runit";
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));

        int expectedBucketWeightAfterUpdate = 2;

        // Value is in container
        cache.cacheContainer.addNewSearchResultsToBucket(searchTerm, searchResults);

        // Act
        cache.addSearchResultsToCache(searchTerm, searchResults);

        // Assert
        Assert.assertEquals(cache.cacheContainer.firstFreeSlot, 2);
        Assert.assertEquals(cache.cacheContainer.buckets[1].bucketWeight, expectedBucketWeightAfterUpdate);
    }

    /**
     * This happens if Cache lets SearchService to found SearchResults again,
     * and SearchResults changed. We must split new Bucket with only that
     * searchTerm and new SearchResults from bucket, where it belongs.
     */
    @Test
    public void splitBucketWhenBucketContainsSearchTermAndHashChanged() {
        // Arrange
        LexicalFrequencySearchCache cache = new LexicalFrequencySearchCache(10, 4);

        String searchTerm = "runit";
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(WebPageResult.fromWebAlias(new Alias("runitForever", "")));

        // Value is in container
        cache.cacheContainer.addNewSearchResultsToBucket(searchTerm, searchResults);

        // Act
        // SearchResultsChanged! More results!
        searchResults.add(WebPageResult.fromWebAlias(new Alias("runitForeverAndEver", "")));
        cache.addSearchResultsToCache(searchTerm, searchResults);

        // Assert
        Assert.assertEquals(cache.cacheContainer.firstFreeSlot, 3); // We have
        // two
        // buckets
        // occupied
        // (split
        // result)
    }

    /**
     * This will occur if Cache has not cached values for given searchTerm, but
     * hashCode of some cached SearchResults is the same as hashCode of the
     * results of new searchTerm. This means, that SearchResults are the same,
     * and we must add searchTerm to existing bucket (instead of creating new
     * one with the same results)
     */
    @Test
    public void updateBucketWhenBucketDoNotContainsSearchTermButHashIsTheSameAsBucketHash() {
        // Arrange
        LexicalFrequencySearchCache cache = new LexicalFrequencySearchCache(10, 4);

        String searchTerm = "runit";
        String newSearchTerm = "bunnyit";
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(WebPageResult.fromWebAlias(new Alias("runitForever", "")));

        // Value is in container
        cache.cacheContainer.addNewSearchResultsToBucket(searchTerm, searchResults);

        // Act
        // SearchResults are the same, but searchTerm is not in Cache yet
        cache.addSearchResultsToCache(newSearchTerm, searchResults);

        // Assert
        Assert.assertEquals(cache.cacheContainer.firstFreeSlot, 2);
        Assert.assertTrue(cache.cacheContainer.buckets[1].searchTermTimeStamps.containsKey(searchTerm));
        Assert.assertTrue(cache.cacheContainer.buckets[1].searchTermTimeStamps.containsKey(newSearchTerm));
    }

    /**
     * Trivial example. Given SearchTerm and SearchResult do not belong to any
     * bucket, so we need to create one.
     */
    @Test
    public void addBucketIfContainerDontHaveIt() {
        // Arrange
        LexicalFrequencySearchCache cache = new LexicalFrequencySearchCache(10, 4);

        String searchTerm = "runit";
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(WebPageResult.fromWebAlias(new Alias("runitForever", "")));

        // Act
        cache.addSearchResultsToCache(searchTerm, searchResults);

        // Assert
        Assert.assertEquals(cache.cacheContainer.firstFreeSlot, 2);
        Assert.assertTrue(cache.cacheContainer.buckets[1].searchTermTimeStamps.containsKey(searchTerm));
    }

    /**
     * Cache container 0 index is designed for empty results searchTerm.
     */
    @Test
    public void addEmptySearchResultToZeroSlot() {
        // Arrange
        LexicalFrequencySearchCache cache = new LexicalFrequencySearchCache(10, 4);

        String first = "runnit";
        String second = "bunnyit";

        // Act
        cache.addSearchResultsToCache(first, new ArrayList<>());
        cache.addSearchResultsToCache(second, new ArrayList<>());

        // Assert
        Assert.assertEquals(cache.cacheContainer.firstFreeSlot, 1);
        Assert.assertTrue(cache.cacheContainer.buckets[0].searchTermTimeStamps.containsKey(first));
        Assert.assertTrue(cache.cacheContainer.buckets[0].searchTermTimeStamps.containsKey(second));
    }

}
