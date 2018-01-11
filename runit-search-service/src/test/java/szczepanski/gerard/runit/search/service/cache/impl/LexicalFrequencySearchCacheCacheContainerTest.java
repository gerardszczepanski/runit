package szczepanski.gerard.runit.search.service.cache.impl;

import org.testng.Assert;
import org.testng.annotations.Test;
import szczepanski.gerard.runit.search.service.cache.impl.LexicalFrequencySearchCache.CacheContainer;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.impl.WebPageResult;
import szczepanski.gerard.runit.settings.service.loader.Alias;

import java.util.ArrayList;
import java.util.List;

public class LexicalFrequencySearchCacheCacheContainerTest {

    @Test
    public void testInitializeWithEmptyBucket() {
        // Arrange
        int size = 20;
        int expectedSize = 21; // Free 0 slot for EMPTY RESULTS

        // Act
        LexicalFrequencySearchCache.CacheContainer container = new CacheContainer(size, 2);

        // Assert
        Assert.assertEquals(container.buckets.length, expectedSize);
        Assert.assertEquals(container.firstFreeSlot, 1);
        Assert.assertNotNull(container.buckets[0]); // 0 Slot for EMPTY RESULTS
        // is initialized
        Assert.assertNull(container.buckets[1]); // But first slot is null
    }

    @Test
    public void addNewSearchResultToBucket() {
        // Arrange
        int size = 20;
        String searchTerm = "test";
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));

        LexicalFrequencySearchCache.CacheContainer container = new CacheContainer(size, 3);

        // Act
        container.addNewSearchResultsToBucket(searchTerm, searchResults);

        // Assert
        Assert.assertEquals(container.firstFreeSlot, 2);
        Assert.assertEquals(container.buckets[1].searchResults, searchResults);
    }

    @Test
    public void containerSortFromHeaviestToLightest() {
        // Arrange
        int size = 20;
        String firstSearchTerm = "test"; // Expected weight 4
        String secondSearchTerm = "runit"; // Expected weight 6
        String thirdSearchTerm = "awaken"; // Expected weight 5

        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));

        LexicalFrequencySearchCache.CacheContainer container = new CacheContainer(size, 3);

        // Act
        container.addNewSearchResultsToBucket(firstSearchTerm, searchResults); // b1
        // =
        // 1
        container.addNewSearchResultsToBucket(secondSearchTerm, searchResults); // b2
        // =
        // 1
        container.addNewSearchResultsToBucket(thirdSearchTerm, searchResults); // b3
        // =
        // 1

        container.updateBucketWithSearchTerm(1, firstSearchTerm); // b1 = 2
        container.updateBucketWithSearchTerm(1, firstSearchTerm); // b1 = 3
        container.updateBucketWithSearchTerm(1, firstSearchTerm); // b1 = 4

        container.updateBucketWithSearchTerm(2, secondSearchTerm); // b2 = 2
        container.updateBucketWithSearchTerm(2, secondSearchTerm); // b2 = 3
        container.updateBucketWithSearchTerm(2, secondSearchTerm); // b2 = 4
        container.updateBucketWithSearchTerm(2, secondSearchTerm); // b2 = 5 ->
        // b2
        // switched
        // index to
        // 1 (sort)
        container.updateBucketWithSearchTerm(1, secondSearchTerm); // b2 = 6

        container.updateBucketWithSearchTerm(3, thirdSearchTerm); // b3 = 2
        container.updateBucketWithSearchTerm(3, thirdSearchTerm); // b3 = 3
        container.updateBucketWithSearchTerm(3, thirdSearchTerm); // b3 = 4
        container.updateBucketWithSearchTerm(3, thirdSearchTerm); // b3 = 5

        // Assert
        Assert.assertEquals(container.firstFreeSlot, 4);

        Assert.assertEquals(container.buckets[1].bucketWeight, 6); // The
        // heavier
        // b2 is
        // first
        Assert.assertEquals(container.buckets[2].bucketWeight, 5); // b3 is
        // second
        Assert.assertEquals(container.buckets[3].bucketWeight, 4); // b1 is last
    }

    @Test
    public void freeContainerMemoryIfNeeded() {
        // Arrange
        int size = 8;
        int bucketsToRelease = 4;
        int expectedNextPointerAfterClearContainerMemory = 6;

        String searchTerm = "freeMemory!";
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));

        LexicalFrequencySearchCache.CacheContainer container = new CacheContainer(size, bucketsToRelease); //nextPointer = 1

        // Act
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //nextPointer = 2
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //nextPointer = 3
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //nextPointer = 4
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //nextPointer = 5
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //nextPointer = 6
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //nextPointer = 7
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //nextPointer = 8 (LAST FREE SLOT)
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //nextPointer = 9 (OUT OF MEMORY) nextPointer =  5, add next bucket nextPointer = 6
        container.addNewSearchResultsToBucket(searchTerm, searchResults); //(CLEAR) nextPointer =  5, add next bucket nextPointer = 6

        // Assert
        Assert.assertEquals(container.firstFreeSlot, expectedNextPointerAfterClearContainerMemory);
        Assert.assertNull(container.buckets[6]);
        Assert.assertNull(container.buckets[7]);
        Assert.assertNull(container.buckets[8]);
    }

    @Test
    public void splitBucketsSuccess() {
        // Arrange
        int size = 8;
        int bucketsToRelease = 4;

        String searchTerm = "first";
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));

        String secondSearchTerm = "second";
        List<SearchResult> secondSearchResults = new ArrayList<>();
        secondSearchResults.add(WebPageResult.fromWebAlias(new Alias("hello", "")));

        LexicalFrequencySearchCache.CacheContainer container = new CacheContainer(size, bucketsToRelease);
        container.addNewSearchResultsToBucket(searchTerm, searchResults);
        container.updateBucketWithSearchTerm(1, secondSearchTerm);
        container.updateBucketWithSearchTerm(1, secondSearchTerm); //It will be heavier after split

        // Act
        container.splitBucketForSearchTerm(1, secondSearchTerm, secondSearchResults);

        // Assert
        Assert.assertEquals(container.firstFreeSlot, 3);
        Assert.assertEquals(container.buckets[1].searchResults, secondSearchResults);
        Assert.assertEquals(container.buckets[2].searchResults, searchResults);
    }


}
