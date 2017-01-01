package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.loader.Settings;

public class FileSearchAlgorithmTest {

	FileSearchAlgorithm searchAlgorithm;

	@BeforeTest
	public void beforeTest() {
		searchAlgorithm = new FileSearchAlgorithm();
	}

	@Test
	public void loadExecutableFilesSuccess() {
		// Arrange
		String fullRootDirPath = "C:\\Users\\User\\Documents";
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
		
		//	Act
		List<SearchResult> searchResults = searchAlgorithm.search(searchTerm, settings);
		
		// Assert
		Assert.assertEquals(searchResults.size(), numberOfExecutableFiles);
	}

}
