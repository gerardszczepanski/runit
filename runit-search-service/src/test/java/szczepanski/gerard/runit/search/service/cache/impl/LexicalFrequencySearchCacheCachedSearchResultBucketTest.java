package szczepanski.gerard.runit.search.service.cache.impl;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.impl.WebPageResult;
import szczepanski.gerard.runit.settings.service.loader.Alias;

public class LexicalFrequencySearchCacheCachedSearchResultBucketTest {

	@Test
	public void createNewCorrectBucket() {
		// Arrange
		String searchTerm = "runit";
		List<SearchResult> searchResults = new ArrayList<>();
		searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));
		int expectedFrequency = 1;
		int expectedBucketWeight = expectedFrequency * searchResults.size() * 1;

		//Act
		LexicalFrequencySearchCache.CachedSearchResultsBucket bucket = new LexicalFrequencySearchCache.CachedSearchResultsBucket(
				searchTerm, searchResults);

		//Assert
		Assert.assertEquals(bucket.frequency, expectedFrequency);
		Assert.assertEquals(bucket.bucketWeight, expectedBucketWeight);
		Assert.assertEquals(bucket.searchResults, searchResults);
		Assert.assertEquals(bucket.searchResultsHash, searchResults.hashCode());
		Assert.assertEquals(bucket.searchTermTimeStamps.size(), 1);
		Assert.assertTrue(bucket.searchTermTimeStamps.containsKey(searchTerm));
	}
	
	@Test
	public void updateBucketWithNewSearchTerm() {
		// Arrange
		String searchTerm = "runit";
		List<SearchResult> searchResults = new ArrayList<>();
		searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));
		int expectedFrequency = 2;
		int expectedSearchTermSize = 2;
		int expectedBucketWeight = expectedFrequency * searchResults.size() * expectedSearchTermSize;
		
		String newSearchTerm = "run";

		//Act
		LexicalFrequencySearchCache.CachedSearchResultsBucket bucket = new LexicalFrequencySearchCache.CachedSearchResultsBucket(
				searchTerm, searchResults);
		
		bucket.updateBucketWithSearchTerm(newSearchTerm);

		//Assert
		Assert.assertEquals(bucket.frequency, expectedFrequency);
		Assert.assertEquals(bucket.bucketWeight, expectedBucketWeight);
		Assert.assertEquals(bucket.searchResults, searchResults);
		Assert.assertEquals(bucket.searchResultsHash, searchResults.hashCode());
		Assert.assertEquals(bucket.searchTermTimeStamps.size(), expectedSearchTermSize);
		Assert.assertTrue(bucket.searchTermTimeStamps.containsKey(searchTerm));
		Assert.assertTrue(bucket.searchTermTimeStamps.containsKey(newSearchTerm));
	}
	
	@Test
	public void updateBucketWithExistingSearchTerm() throws InterruptedException {
		// Arrange
		String searchTerm = "runit";
		List<SearchResult> searchResults = new ArrayList<>();
		searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));
		int expectedFrequency = 2;
		int expectedSearchTermSize = 1;
		int expectedBucketWeight = expectedFrequency * searchResults.size() * expectedSearchTermSize;
		
		LexicalFrequencySearchCache.CachedSearchResultsBucket bucket = new LexicalFrequencySearchCache.CachedSearchResultsBucket(
				searchTerm, searchResults);
		
		long firstTimeStamp = bucket.searchTermTimeStamps.get(searchTerm).frequency;
		//Act
		Thread.sleep(10); //Wait a few seconds for processor clock tick 
		bucket.updateBucketWithSearchTerm(searchTerm);
		
		//Assert
		Assert.assertEquals(bucket.frequency, expectedFrequency);
		Assert.assertEquals(bucket.bucketWeight, expectedBucketWeight);
		Assert.assertEquals(bucket.searchResults, searchResults);
		Assert.assertEquals(bucket.searchResultsHash, searchResults.hashCode());
		Assert.assertEquals(bucket.searchTermTimeStamps.size(), expectedSearchTermSize);
		Assert.assertTrue(bucket.searchTermTimeStamps.containsKey(searchTerm));
		Assert.assertNotEquals(bucket.searchTermTimeStamps.get(searchTerm), firstTimeStamp); //Update existing value -> update timestamp
	}
	
	@Test
	public void splitBucketBySearchTerm() {
		// Arrange
		String searchTerm = "runit";
		List<SearchResult> searchResults = new ArrayList<>();
		searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));
		searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));
		searchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));
		
		String newSearchTerm = "run";
		
		List<SearchResult> newSearchResults = new ArrayList<>();
		newSearchResults.add(WebPageResult.fromWebAlias(new Alias("", "")));
		
		LexicalFrequencySearchCache.CachedSearchResultsBucket bucket = new LexicalFrequencySearchCache.CachedSearchResultsBucket(
				searchTerm, searchResults);
		bucket.updateBucketWithSearchTerm(newSearchTerm);
		bucket.updateBucketWithSearchTerm(newSearchTerm);
		bucket.updateBucketWithSearchTerm(newSearchTerm); //Frequency 3 for that searchTerm
		
		int expectedBucketWeight = 3; // Before split it was 24  
		int expectedSplittedBucketWeight = 4; //Split is add operation so frequency up
		
		//Act
		LexicalFrequencySearchCache.CachedSearchResultsBucket splittedBucket = bucket.splitBucketForSearchTermAndNewSearchResults(newSearchTerm, newSearchResults);

		//Assert
		Assert.assertEquals(splittedBucket.bucketWeight, expectedSplittedBucketWeight);
		Assert.assertEquals(splittedBucket.frequency, expectedSplittedBucketWeight);
		Assert.assertEquals(splittedBucket.searchResults, newSearchResults);
		Assert.assertEquals(splittedBucket.searchTermTimeStamps.size(), 1);
		Assert.assertTrue(splittedBucket.searchTermTimeStamps.containsKey(newSearchTerm));
		
		Assert.assertEquals(bucket.bucketWeight, expectedBucketWeight);
		Assert.assertEquals(bucket.frequency, 1);
		Assert.assertEquals(bucket.searchResults, searchResults);
		Assert.assertEquals(bucket.searchTermTimeStamps.size(), 1);
		Assert.assertFalse(bucket.searchTermTimeStamps.containsKey(newSearchTerm));
		Assert.assertTrue(bucket.searchTermTimeStamps.containsKey(searchTerm));
}

}
