package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.loader.Settings;
import szczepanski.gerard.runit.service.search.loader.Alias;
import szczepanski.gerard.runit.service.service.SearchTermMatcher;

public class WebAliasSearchAlgorithmTest {

	SearchTermMatcher searchTermMatcher;
	WebAliasSearchAlgorithm searchAlgorithm;

	@BeforeTest
	public void beforeTest() {
		searchTermMatcher = Mockito.mock(SearchTermMatcher.class);
		searchAlgorithm = new WebAliasSearchAlgorithm(searchTermMatcher);
	}

	@Test
	public void findWebAliasSuccess() {
		// Arrange
		String searchTerm = "google"; 
		List<Alias> webAliasesDefinedInSettings = new ArrayList<>();
		webAliasesDefinedInSettings.add(new Alias("google", "http://google.com"));
		webAliasesDefinedInSettings.add(new Alias("github", "http://github.com"));
		
		Settings settings = Settings.builder()
				.webAliases(webAliasesDefinedInSettings)
				.build();
		
		Mockito.when(searchTermMatcher.isMatch(searchTerm, "google")).thenReturn(true);
		
		// Act
		List<SearchResult> searchResults = searchAlgorithm.search(searchTerm, settings);
		
		// Assert
		Assert.assertEquals(searchResults.size(), 1);
	}

}
