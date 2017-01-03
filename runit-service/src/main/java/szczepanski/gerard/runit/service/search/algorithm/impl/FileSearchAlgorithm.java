package szczepanski.gerard.runit.service.search.algorithm.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.result.FileResult;
import szczepanski.gerard.runit.service.result.SearchResult;
import szczepanski.gerard.runit.service.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.service.search.loader.Settings;

public class FileSearchAlgorithm implements SearchAlgorithm {
	private static final Logger LOG = Logger.getLogger(FileSearchAlgorithm.class);

	@Override
	public List<SearchResult> search(String searchTerm, Settings settings) {
		List<SearchResult> searchResults = new ArrayList<>();
		List<String> paths = settings.getRootDirectioresToScan();
		List<String> fileExtensions = settings.getFileExtensions();
		
		LOG.debug(String.format("Searching started"));
		paths.forEach(p -> {
			searchResults.addAll(searchForRootPath(p, searchTerm, fileExtensions));
		});
		LOG.debug(String.format("Searching finished"));

		return searchResults;
	}

	private List<SearchResult> searchForRootPath(String rootPath, String searchTerm, List<String> fileExtensions) {
		List<File> filesFound = findFiles(rootPath, searchTerm, fileExtensions);
		return filesFound.stream().map(f -> FileResult.fromFile(f)).collect(Collectors.toList());
	}

	private List<File> findFiles(String rootPath, String searchTerm, List<String> fileExtensions) {
		List<File> suitableFiles = new ArrayList<>();

		try {
			findSuitableFiles(rootPath, searchTerm, fileExtensions, suitableFiles);
		} catch (IOException e) {
			throw new RunitRuntimeException(
					String.format("Cannot read files from path %s. Maybe path is invaid?", rootPath));
		}

		return suitableFiles;
	}

	private void findSuitableFiles(String rootPath, String searchTerm, List<String> fileExtensions, List<File> suitableFiles) throws IOException {
		Files.walkFileTree(Paths.get(rootPath), new FileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}
			
			// TODO Gerard Szczepanski 03.01.2017 to future refactor (IF conditions are very ugly)
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				
				if (attrs.isRegularFile() 
						&& Files.isExecutable(file)
						&& isFileNameContainsSearchTerm(file, searchTerm)
						&& hasFileAcceptableExtension(file, fileExtensions)) {
					File suitableFile = file.toFile();
					suitableFiles.add(suitableFile);
				}
				
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				LOG.debug(String.format("Visit file %s FAILED. File visiting is being continued...", file));
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}

		});
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
		return fileName.toLowerCase().contains(searchTerm.toLowerCase());
	}

}
