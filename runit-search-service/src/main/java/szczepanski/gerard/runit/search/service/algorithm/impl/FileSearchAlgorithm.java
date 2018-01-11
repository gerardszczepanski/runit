package szczepanski.gerard.runit.search.service.algorithm.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.advanced.collection.facade.AdvancedCollectionFactory;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.search.service.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.impl.FileResult;
import szczepanski.gerard.runit.search.service.service.SearchTermMatcher;
import szczepanski.gerard.runit.settings.service.loader.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@RequiredArgsConstructor
public class FileSearchAlgorithm implements SearchAlgorithm {
    private static final Logger LOG = Logger.getLogger(FileSearchAlgorithm.class);

    private final SearchTermMatcher searchTermMatcher;

    @Override
    public List<SearchResult> search(String searchTerm, Settings settings) {
        List<SearchResult> searchResults = AdvancedCollectionFactory.list();
        List<String> paths = settings.getRootDirectioresToScan();
        List<String> fileExtensions = settings.getFileExtensions();

        LOG.debug("Searching started");
        paths.forEach(p -> searchResults.addAll(searchForRootPath(p, searchTerm, fileExtensions)));
        LOG.debug("Searching finished");

        return searchResults;
    }

    private List<SearchResult> searchForRootPath(String rootPath, String searchTerm, List<String> fileExtensions) {
        List<File> foundFiles = findFiles(rootPath, searchTerm, fileExtensions);
        return convertFromFilesToSearchResults(foundFiles);
    }

    private List<File> findFiles(String rootPath, String searchTerm, List<String> fileExtensions) {
        List<File> suitableFiles = AdvancedCollectionFactory.list();

        try {
            findSuitableFiles(rootPath, searchTerm, fileExtensions, suitableFiles);
        } catch (IOException e) {
            throw new RunitRuntimeException(ExceptionCode.B_003, rootPath);
        }

        return suitableFiles;
    }

    private void findSuitableFiles(String rootPath, String searchTerm, List<String> fileExtensions, List<File> suitableFiles) throws IOException {
        Files.walkFileTree(Paths.get(rootPath), new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            // TODO Gerard Szczepanski 03.01.2017 to future refactor (IF
            // conditions are very ugly)
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                if (attrs.isRegularFile() && Files.isExecutable(file) && isFileNameContainsSearchTerm(file, searchTerm)
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

    private boolean isFileNameContainsSearchTerm(Path path, String searchTerm) {
        String fullFileName = path.getFileName().toString();
        String fileName = FilenameUtils.getName(fullFileName);
        String baseFileName = FilenameUtils.getBaseName(fileName);
        return searchTermMatcher.isMatch(searchTerm, baseFileName);
    }

    private List<SearchResult> convertFromFilesToSearchResults(List<File> filesFound) {
        List<SearchResult> results = AdvancedCollectionFactory.list(filesFound.size());
        filesFound.forEach(f -> results.add(FileResult.fromFile(f)));
        return results;
    }

}
