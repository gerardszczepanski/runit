package szczepanski.gerard.runit.search.algorithm.impl;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.search.service.SearchTermMatcher;
import szczepanski.gerard.runit.search.result.SearchResult;
import szczepanski.gerard.runit.settings.loader.Alias;
import szczepanski.gerard.runit.settings.loader.Settings;

import java.util.ArrayList;
import java.util.List;

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

        Settings settings = Settings.builder().webAliases(webAliasesDefinedInSettings).build();

        Mockito.when(searchTermMatcher.isMatch(searchTerm, "google")).thenReturn(true);

        // Act
        List<SearchResult> searchResults = searchAlgorithm.search(searchTerm, settings);

        // Assert
        Assert.assertEquals(searchResults.size(), 1);
    }

}
