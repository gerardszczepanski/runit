package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.loader.Settings;
import szczepanski.gerard.runit.service.search.loader.WebAlias;

public class WebAliasSearchAlgorithmTest {

	WebAliasSearchAlgorithm searchAlgorithm;

	@BeforeTest
	public void beforeTest() {
		searchAlgorithm = new WebAliasSearchAlgorithm();
	}

	@Test
	public void findWebAliasSuccess() {
		// Arrange
		String searchTerm = "google"; 
		List<WebAlias> webAliasesDefinedInSettings = new ArrayList<>();
		webAliasesDefinedInSettings.add(new WebAlias("google", "http://google.com"));
		webAliasesDefinedInSettings.add(new WebAlias("github", "http://github.com"));
		
		Settings settings = Settings.builder()
				.webAliases(webAliasesDefinedInSettings)
				.build();
		
		// Act
		List<SearchResult> searchResults = searchAlgorithm.search(searchTerm, settings);
		
		// Assert
		Assert.assertEquals(searchResults.size(), 1);
	}

}
