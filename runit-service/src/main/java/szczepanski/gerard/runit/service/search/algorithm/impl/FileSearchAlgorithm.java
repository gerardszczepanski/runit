package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.result.FileResult;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.service.search.loader.Settings;

public class FileSearchAlgorithm implements SearchAlgorithm {

	@Override
	public List<SearchResult> search(String searchTerm, Settings settings) {
		List<SearchResult> searchResults = new ArrayList<>();
		List<String> paths = settings.getRootDirectioresToScan();
		List<String> fileExtensions = settings.getFileExtensions();
		
		paths.forEach(p -> {
			searchResults.addAll(searchForRootPath(p, searchTerm, fileExtensions));
		});
		
		return searchResults;
	}
	
	private List<SearchResult> searchForRootPath(String rootPath, String searchTerm, List<String> fileExtensions) {
		List<File> filesFound = findFiles(rootPath, searchTerm, fileExtensions);
		return filesFound.stream().map(f -> FileResult.fromFile(f)).collect(Collectors.toList());
	}
	
	private List<File> findFiles(String rootPath, String searchTerm, List<String> fileExtensions) {
		try {
			return Files.walk(Paths.get(rootPath))
				.filter(Files::isRegularFile)
				.filter(Files::isExecutable)
				.filter(p -> hasFileAcceptableExtension(p, fileExtensions))
				.filter(p -> isFileNameContainsSearchTerm(p, searchTerm))
				.map(Path::toFile)
				.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RunitRuntimeException(String.format("Cannot read files from path %s. Maybe path is invaid?", rootPath));
		}
	}
	
	private boolean hasFileAcceptableExtension(Path path, List<String> acceptableFileExtensions) {
		String fullFileName = path.getFileName().toString();
		String extension = FilenameUtils.getExtension(fullFileName);
		return acceptableFileExtensions.contains(extension);
	}
	
	/**
	 * Temporary implementation. Will be changed in future to Regex patterns.
	 * TODO Gerard Szczepanski 26.12.2016 
	 */
	private boolean isFileNameContainsSearchTerm(Path path, String searchTerm) {
		String fullFileName = path.getFileName().toString();
		String fileName = FilenameUtils.getName(fullFileName);
		return fileName.contains(searchTerm);
	}

}
