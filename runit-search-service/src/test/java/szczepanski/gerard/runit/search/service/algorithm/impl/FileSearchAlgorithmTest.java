package szczepanski.gerard.runit.search.service.algorithm.impl;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.algorithm.impl.FileSearchAlgorithm;
import szczepanski.gerard.runit.service.service.SearchTermMatcher;
import szczepanski.gerard.runit.settings.service.loader.Settings;

public class FileSearchAlgorithmTest {

	SearchTermMatcher searchTermMatcher;
	FileSearchAlgorithm searchAlgorithm;

	@BeforeTest
	public void beforeTest() {
		searchTermMatcher = Mockito.mock(SearchTermMatcher.class);
		searchAlgorithm = new FileSearchAlgorithm(searchTermMatcher);
	}

	@Test(enabled = false)
	public void loadExecutableFilesSuccess() {
		// Arrange
		String fullRootDirPath = "C:\\Users\\Ziemniak\\Documents\\runit\\runit-search-service\\src\\test\\resources\\fileAlgorithmTest";
		List<String> rootDirectoriesToScan = new ArrayList<>();
		rootDirectoriesToScan.add(fullRootDirPath);
		List<String> fileExtensions = new ArrayList<>();
		fileExtensions.add("exe");
		
		String searchTerm = "runit";
		Settings settings = Settings.builder()
									.rootDirectioresToScan(rootDirectoriesToScan)
									.fileExtensions(fileExtensions)
									.build();
		int numberOfExecutableFiles = 2; //From path and second level path
		
		Mockito.when(searchTermMatcher.isMatch(searchTerm, "runit")).thenReturn(true);
		
		//	Act
		List<SearchResult> searchResults = searchAlgorithm.search(searchTerm, settings);
		
		// Assert
		Assert.assertEquals(searchResults.size(), numberOfExecutableFiles);
	}

}
