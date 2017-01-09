package szczepanski.gerard.runit.search.service.service.impl;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.search.service.service.impl.SimpleSearchTermMatcher;
import szczepanski.gerard.runit.service.service.SearchTermMatcher;

public class SimpleSearchTermMatcherTest {

	SearchTermMatcher matcher;

	@BeforeTest
	public void beforeTest() {
		matcher = new SimpleSearchTermMatcher();
	}

	@Test(dataProvider = "searchTermMatchDataProvider")
	public void searchTermMatch(String searchTerm, String searchResultName) {
		// Act
		boolean isMatch = matcher.isMatch(searchTerm, searchResultName);

		// Assert
		Assert.assertTrue(isMatch);
	}

	@Test(dataProvider = "searchTermDoNotMatchDataProvider")
	public void searchTermDoNotMatch(String searchTerm, String searchResultName) {
		// Act
		boolean isMatch = matcher.isMatch(searchTerm, searchResultName);

		// Assert
		Assert.assertFalse(isMatch);
	}
	
	 @DataProvider(name = "searchTermMatchDataProvider")
	 public Object[][] searchTermMatchDataProvider() {
	        return new Object[][] { 
	        	{ "g", "google" }, 
	        	{ "go", "google" },
	        	{ "goo", "google chrome" }, 
	        	{ "googl", "google chrome two two" },
	        	{ "google", "google" }
	        };
	    }
	 
	 @DataProvider(name = "searchTermDoNotMatchDataProvider")
	 public Object[][] searchTermDoNotMatchDataProvider() {
	        return new Object[][] { 
	        	{ "g", "fgoogle" }, 
	        	{ "go", "fgoogle" },
	        	{ "goo", "fgoogle chrome" }, 
	        	{ "googl", "fgoogle chrome two two" },
	        	{ "google", "fgoogle" }
	        };
	    }

}
