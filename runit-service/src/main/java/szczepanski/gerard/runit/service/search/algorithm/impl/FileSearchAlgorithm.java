package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.result.FileResult;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.service.search.loader.Settings;

public class FileSearchAlgorithm implements SearchAlgorithm {

	@Override
	public List<SearchResult> search(String searchTerm, Settings settings) {
		List<SearchResult> searchResults = new ArrayList<>();
		Spliterator<String> paths = settings.getRootDirectories();
		Spliterator<String> fileExtensions = settings.getFileExtensions();
		
		paths.forEachRemaining(p -> {
			searchResults.addAll(searchForRootPath(p, searchTerm, fileExtensions));
		});
		
		return searchResults;
	}
	
	private List<SearchResult> searchForRootPath(String rootPath, String searchTerm, Spliterator<String> fileExtensions) {
		List<File> filesFound = findFiles(rootPath, searchTerm, fileExtensions);
		return filesFound.stream().map(f -> FileResult.fromFile(f)).collect(Collectors.toList());
	}
	
	private List<File> findFiles(String rootPath, String searchTerm, Spliterator<String> fileExtensions) {
		try {
			return Files.walk(Paths.get(rootPath))
				.filter(Files::isRegularFile)
				.filter(Files::isExecutable)
				.map(Path::toFile)
				.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RunitRuntimeException(String.format("Cannot read files from path %s. Maybe path is invaid?", rootPath));
		}
	}

}
