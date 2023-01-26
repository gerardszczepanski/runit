package szczepanski.gerard.runit.search.algorithm.impl;

import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.collection.AdvancedCollectionFactory;
import szczepanski.gerard.runit.search.algorithm.SearchAlgorithm;
import szczepanski.gerard.runit.search.result.SearchResult;
import szczepanski.gerard.runit.search.result.impl.DirectoryResult;
import szczepanski.gerard.runit.search.service.SearchTermMatcher;
import szczepanski.gerard.runit.settings.loader.Alias;
import szczepanski.gerard.runit.settings.loader.Settings;

import java.util.List;

@RequiredArgsConstructor
public class DirectoryAliasSearchAlgorithm implements SearchAlgorithm {

    private final SearchTermMatcher searchtermMatcher;

    @Override
    public List<SearchResult> search(String searchTerm, Settings settings) {
        List<Alias> aliases = settings.getDirAliases();
        return convertFromAliases(searchTerm, aliases);
    }

    private List<SearchResult> convertFromAliases(String searchTerm, List<Alias> aliases) {
        List<SearchResult> searchResults = AdvancedCollectionFactory.list();

        aliases.forEach(a -> {
            if (isAliasContainsSearchTerm(a.getName(), searchTerm)) {
                searchResults.add(DirectoryResult.fromDirectoryAlias(a));
            }
        });
        return searchResults;
    }

    private boolean isAliasContainsSearchTerm(String alias, String searchTerm) {
        return searchtermMatcher.isMatch(searchTerm, alias);
    }
}
