package szczepanski.gerard.runit.service.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.service.Cache;

public class SimpleSearchCacheTest {

	final int cacheLimit = 10;
	final double cacheClearSizeInPercentage = 0.1; 

	Cache cache;

	@BeforeMethod
	public void beforeMethodO() {
		cache = new SimpleSearchCache(cacheLimit, cacheClearSizeInPercentage);
	}

	@Test
	public void addSearchResultsToCacheSuccess() {
		// Arrange
		List<SearchResult> searchResults = new ArrayList<>();
		String searchTerm = "runit";

		// Act
		cache.addSearchResultsToCache(searchTerm, searchResults);
		Optional<List<SearchResult>> cachedSearchResults = cache.getFromCache(searchTerm);

		// Assert
		Assert.assertEquals(cachedSearchResults.isPresent(), true);
	}

	@Test
	public void returnEmptyOptionalIfCacheDoNotHaveResultsForSearchTermSuccess() {
		// Arrange
		String searchTerm = "runit";

		// Act
		Optional<List<SearchResult>> cachedSearchResults = cache.getFromCache(searchTerm);

		// Assert
		Assert.assertEquals(cachedSearchResults.isPresent(), false);
	}

	@Test
	public void oldSearchtermResultsAreBeingRemovedSuccess() {
		// Arrange
		String firstSearchTerm = "oldSearchTerm";
		
		// Act
		cache.addSearchResultsToCache(firstSearchTerm, new ArrayList<>()); // Add searchTerm with results
		
		/**
		 * Add 10 new searchResults to cache. Firstly added searchTerm should now vanish from cache.
		 */
		for (int i = 0; i < cacheLimit; i++) {
			String nextSearchTerm = "nextSearchTerm" + i;
			cache.addSearchResultsToCache(nextSearchTerm, new ArrayList<>());
		}
		
		Optional<List<SearchResult>> searchResultsThatShouldVanished = cache.getFromCache(firstSearchTerm);

		// Assert
		Assert.assertEquals(searchResultsThatShouldVanished.isPresent(), false);
	}
	
	/**
	 * Search methods should at first check if Collection of SearchResult
	 * of given sarchTerm is stored in Cache object. If yes, then should return that 
	 * Collection instead of fire searching. So if there is no searching for Collection of results
	 * from Cache object, there is no need to add searchTerm results again to Cache until Cache stores results
	 * for that searchTerm.
	 * 
	 *  Exception if guarantee, that programmer do not made mistake by add cached results twice or more.
	 * 
	 */
	@Test(expectedExceptions = RunitRuntimeException.class)
	public void addSearchTermTwiceShouldThrowRuntimeException() {
		// Arrange
		String searchTerm = "runit";
		
		//Act
		cache.addSearchResultsToCache(searchTerm, new ArrayList<>());
		cache.addSearchResultsToCache(searchTerm, new ArrayList<>());
	}
	
	@Test
	public void addSearchTermResutsAgainIfItIsNoLongerInCacheSuccess() {
		// Arrange
		String searchTerm = "runit";
		
		// Act
		cache.addSearchResultsToCache(searchTerm, new ArrayList<>()); // Add searchTerm with results
		
		/**
		 * Add 10 new searchResults to cache. Firstly added searchTerm results collection should now vanish from cache.
		 */
		for (int i = 0; i < cacheLimit; i++) {
			String nextSearchTerm = "nextSearchTerm" + i;
			cache.addSearchResultsToCache(nextSearchTerm, new ArrayList<>());
		}
		
		//Add searchTerm results again, because latest searchTerm results vanished from cache
		cache.addSearchResultsToCache(searchTerm, new ArrayList<>()); 
		Optional<List<SearchResult>> searchResults = cache.getFromCache(searchTerm);
		
		// Assert 
		Assert.assertEquals(searchResults.isPresent(), true);
	}

}
