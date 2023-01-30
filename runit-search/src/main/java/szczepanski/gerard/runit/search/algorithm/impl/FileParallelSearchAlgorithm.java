package szczepanski.gerard.runit.search.algorithm.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import lombok.SneakyThrows;
import szczepanski.gerard.runit.collection.AdvancedCollectionFactory;
import szczepanski.gerard.runit.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.result.SearchResult;
import szczepanski.gerard.runit.search.service.SearchTermMatcher;
import szczepanski.gerard.runit.settings.loader.Settings;

import static java.util.stream.Collectors.toList;

public class FileParallelSearchAlgorithm extends FileSearchAlgorithm implements SearchAlgorithm {

    private static final Logger LOG = Logger.getLogger(FileParallelSearchAlgorithm.class);

    private final ExecutorService executorService;

    private FileParallelSearchAlgorithm(SearchTermMatcher searchTermMatcher, ExecutorService executorService) {
        super(searchTermMatcher);
        this.executorService = executorService;
    }

    public static FileParallelSearchAlgorithm create(SearchTermMatcher searchTermMatcher) {
        final int cores = Runtime.getRuntime().availableProcessors();
        return new FileParallelSearchAlgorithm(
                searchTermMatcher,
                Executors.newFixedThreadPool(cores)
        );
    }

    @Override
    @SneakyThrows
    public List<SearchResult> search(String searchTerm, Settings settings) {
        final List<SearchResult> searchResults = AdvancedCollectionFactory.list();
        final List<String> paths = settings.getRootDirectioresToScan();
        final List<String> fileExtensions = settings.getFileExtensions();

        long start = System.currentTimeMillis();
        LOG.debug("Searching started");
        List<Future<List<SearchResult>>> futureResults = paths.stream()
                .map(path -> executorService.submit(() -> searchForRootPath(path, searchTerm, fileExtensions)))
                .collect(toList());

        for (Future<List<SearchResult>> futureResult: futureResults) {
            searchResults.addAll(futureResult.get());
        }
        LOG.debug("Searching finished");
        long finish = System.currentTimeMillis();
        LOG.debug("Search time [ms]: " + (finish - start));
        return toSearchResultsWithoutDuplicates(searchResults);
    }

}
